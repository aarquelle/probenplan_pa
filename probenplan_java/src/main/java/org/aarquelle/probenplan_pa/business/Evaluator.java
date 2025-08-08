/*
 * Copyright (c) 2025, Aaron Prott
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.aarquelle.probenplan_pa.business;

import org.aarquelle.probenplan_pa.entity.Plan;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;
import org.aarquelle.probenplan_pa.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Evaluator {

    Rehearsal durchlaufprobe;
    Plan plan;
    Set<Rehearsal> rehearsals;
    Set<Scene> scenes;

    double expectedNumberOfRepeats;
    Map<Scene, Double> numberOfRepeats;


    double totalLengthOfRehearsals;

    public Evaluator(Plan plan) {
        this.plan = plan;
        this.rehearsals = BasicService.getRehearsals();
        this.scenes = BasicService.getScenes();
        this.totalLengthOfRehearsals = plan.totalLength();
        this.expectedNumberOfRepeats = getExpectedNumberOfRepeats();

        durchlaufprobe = findDurchlaufprobe();
        this.numberOfRepeats = getNumberOfRepeats();

    }

    public double evaluate() {
        double totalCompleteness = totalCompleteness();
        double dlpCompleteness = dlpCompleteness();
        double completenessBeforeDLP = completenessBeforeDLP();
        double lumpiness = lumpiness();
        double minimumRepeats = getMinimumRepeats();
        double medianRepeats = getMedianRepeats();
        double averageRepeats = getAverageRepeats();
        double overSize = overSize();
        double roleNumberScore = getRoleNumberScore();
        double enforcedScenesScore = getEnforcedScenesScore();

        double totalScore = (totalCompleteness * Params.getCompletenessWeight()
                + dlpCompleteness * Params.getDlpCompletenessWeight()
                + completenessBeforeDLP * Params.getCompletenessBeforeDLPWeight()
                + lumpiness * Params.getLumpinessWeight()
                + minimumRepeats * Params.getMinimumRepeatsWeight()
                + medianRepeats * Params.getMedianRepeatsWeight()
                + averageRepeats * Params.getAverageRepeatsWeight()
                + overSize * Params.getOverSizeWeight()
                + roleNumberScore * Params.getNumberOfRolesWeight())
                + enforcedScenesScore
                / Params.getTotalWeight();

        plan.setTestResults(
                new TestResults(totalScore, totalCompleteness, dlpCompleteness, completenessBeforeDLP,
                        lumpiness, minimumRepeats, medianRepeats, averageRepeats,
                        overSize, expectedNumberOfRepeats, roleNumberScore, enforcedScenesScore));
        return totalScore;
    }

    boolean allScenesBeforeDurchlaufprobe() {
        Set<Scene> allScenes = new HashSet<>(scenes);
        for (Rehearsal rehearsal : rehearsals) {
            if (rehearsal.equals(durchlaufprobe)) {
                break;
            }
            plan.get(rehearsal).forEach(allScenes::remove);
        }
        return allScenes.isEmpty();
    }

    private Rehearsal findDurchlaufprobe() {
        for (Rehearsal rehearsal : rehearsals) {
            if (plan.get(rehearsal).size() == scenes.size()) {
                return rehearsal;
            }
        }
        throw new IllegalStateException("No Durchlaufprobe found");
    }

    double completenessBeforeDLP() {
        double result = 0;
        List<Pair<Rehearsal, Set<Scene>>> rehearsalsBeforeDLP = new ArrayList<>();
        for (Rehearsal r : rehearsals) {
            if (r.equals(durchlaufprobe)) {
                break;
            }
            rehearsalsBeforeDLP.add(new Pair<>(r, plan.get(r)));
        }

        Map<Scene, Double> scenesBeforeDLP = new HashMap<>();
        for (Pair<Rehearsal, Set<Scene>> pair : rehearsalsBeforeDLP) {
            Set<Scene> scenes = pair.second();
            Rehearsal rehearsal = pair.first();
            for (Scene scene : scenes) {
                double sceneResult = Analyzer.completenessScore(rehearsal, scene) * scene.getLength(); //TODO Schon ältere Analysen verwenden?
                if (!scenesBeforeDLP.containsKey(scene)) {
                    scenesBeforeDLP.put(scene, sceneResult);
                    result += sceneResult;
                } else if (scenesBeforeDLP.get(scene) < sceneResult) {
                    scenesBeforeDLP.put(scene, sceneResult);
                    result += sceneResult - scenesBeforeDLP.get(scene);
                }
            }
        }
        return result / Analyzer.lengthOfPlay;
    }

    double dlpCompleteness() {
        double result = 0;
        for (Scene scene : plan.get(durchlaufprobe)) {
            result += Analyzer.completenessScore(durchlaufprobe, scene) * scene.getLength() / Analyzer.lengthOfPlay;
        }
        return result;
    }

    double totalCompleteness() {
        double result = 0;
        double planLength = 0;
        for (Pair<Rehearsal, Scene> pair : plan.getAllPairs()) {
            result += Analyzer.completenessScore(pair.first(), pair.second())
                    * pair.second().getLength();
            planLength += pair.second().getLength();
            //        (Params.getAverageRehearsalLength() * (rehearsals.size() -1) + Analyzer.lengthOfPlay); //lengthOfPlay für DLP
        }

        return result / planLength;
    }

    /**
     * Calculates the lumpiness of the rehearsal plan.
     * The lumpiness higher, the fewer different lumps there are.
     * A lump is a sequence of continuous scenes within a rehearsal.
     *
     * @return the lumpiness of the rehearsal plan
     */
    double lumpiness() {
        double result = 0;

        for (Rehearsal r : rehearsals) {
            List<Scene> scenes = new ArrayList<>(plan.get(r));
            int numberOfLumps = Analyzer.getNumberOfLumps(scenes.toArray(new Scene[0]));
            if (numberOfLumps <= 2) {
                numberOfLumps = 1;
            }
            result += 1.0 / numberOfLumps;
        }
        return result / rehearsals.size();
    }

    double overSize() {
        double result = 0;
        for (Rehearsal r : rehearsals) {
            if (r.equals(durchlaufprobe)) {
                continue;
            }
            double rehearsalLength = plan.getLengthOfRehearsal(r);
            double oversize = Math.max(0,
                    (rehearsalLength - Params.getAverageRehearsalLength()) / Params.getAverageRehearsalLength());
            result += Math.pow(oversize, 2);
        }
        return 1 - result / (rehearsals.size() - 1);
    }

    double getExpectedNumberOfRepeats() {
        double lengthOfPlay = scenes.stream()
                .mapToDouble(Scene::getLength)
                .sum();
        return ((rehearsals.size() - 1) * Params.getAverageRehearsalLength()) / lengthOfPlay;
    }

    double getMinimumRepeats() {
        return Math.min(1, numberOfRepeats.values().stream()
                .mapToDouble(Double::doubleValue)
                .min()
                .orElse(0) / expectedNumberOfRepeats);
    }

    double getMedianRepeats() {
        return Math.min(1, numberOfRepeats.values().stream()
                .mapToDouble(Double::doubleValue)
                .sorted()
                .skip(numberOfRepeats.size() / 2)
                .findFirst()
                .orElse(0) / expectedNumberOfRepeats);
    }

    double getAverageRepeats() {
        return Math.min(1, numberOfRepeats.values().stream()
                .mapToDouble(Double::doubleValue)
                .average()
                .orElse(0) / expectedNumberOfRepeats);
    }

    public Map<Scene, Double> getNumberOfRepeats() {
        if (numberOfRepeats == null) {
            numberOfRepeats = new HashMap<>();
            for (Scene s : scenes) {
                numberOfRepeats.put(s, 0.0);
            }
            for (Pair<Rehearsal, Scene> pair : plan.getAllPairs()) {
                if (pair.first().equals(durchlaufprobe)) {
                    continue;
                }
                double completeness = Analyzer.completenessScore(pair.first(), pair.second());
                numberOfRepeats.put(pair.second(),
                        numberOfRepeats.getOrDefault(pair.second(), 0.0)
                                + completeness);
            }
        }
        return numberOfRepeats;
    }

    int getNumberOfRolesInRehearsal(Rehearsal rehearsal) {
        Set<Role> roles = new HashSet<>();
        Set<Scene> scenes = plan.get(rehearsal);
        for (Scene scene : scenes) {
            roles.addAll(scene.getBigRoles());
            roles.addAll(scene.getSmallRoles());
        }
        return roles.size();
    }

    double getRoleNumberScore() {
        double result = 0;
        for (Rehearsal r : rehearsals) {
            result += Math.max(0, getNumberOfRolesInRehearsal(r) - Params.getOptimalNumberOfActors());
        }
        result /= rehearsals.size();
        result /= Analyzer.numberOfRoles -  Params.getOptimalNumberOfActors();
        return 1 - result;
    }

    double getEnforcedScenesScore() {
        double result = 0;
        for (Scene s : Analyzer.getEnforcedScenes()) {
            if (!plan.hasSceneAnywhere(s, durchlaufprobe)) {
                result -= 10000;
            }
        }
        return result;
    }
}
