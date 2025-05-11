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

package org.aarquelle.probenplan_pa.business.suggest;

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.dto.*;
import org.aarquelle.probenplan_pa.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Evaluator {

    RehearsalDTO durchlaufprobe;
    PlanDTO plan;
    ParamsDTO params;
    List<RehearsalDTO> rehearsals;
    List<SceneDTO> scenes;

    double expectedNumberOfRepeats;
    Map<SceneDTO, Double> numberOfRepeats;


    double totalLengthOfRehearsals;

    public Evaluator(PlanDTO plan, ParamsDTO params) {
        this.plan = plan;
        this.params = params;
        this.rehearsals = BasicService.getRehearsals();
        this.scenes = BasicService.getScenes();
        this.totalLengthOfRehearsals = plan.totalLength();
        this.expectedNumberOfRepeats = getExpectedNumberOfRepeats();

        this.numberOfRepeats = getNumberOfRepeats();

        durchlaufprobe = findDurchlaufprobe();
    }

    public double evaluate() {
        double totalCompleteness = totalCompleteness();
        double dlpCompleteness = dlpCompleteness();
        double completenessBeforeDLP = completenessBeforeDLP();
        double lumpiness = lumpiness();
        double minimumRepeats = getMinimumRepeats();
        double medianRepeats = getMedianRepeats();
        double overSize = overSize();
        double roleNumberScore = getRoleNumberScore();

        double totalScore = (totalCompleteness * params.getCompletenessWeight()
                + dlpCompleteness * params.getDlpCompletenessWeight()
                + completenessBeforeDLP * params.getCompletenessBeforeDLPWeight()
                + lumpiness * params.getLumpinessWeight()
                + minimumRepeats * params.getMinimumRepeatsWeight()
                + medianRepeats * params.getMedianRepeatsWeight()
                + overSize * params.getOverSizeWeight()
                + roleNumberScore * params.getNumberOfRolesWeight())
                / params.getTotalWeight();

        plan.setTestResults(
                new TestResults(totalScore, totalCompleteness, dlpCompleteness, completenessBeforeDLP,
                        lumpiness, minimumRepeats, medianRepeats, overSize, expectedNumberOfRepeats, roleNumberScore));
        return totalScore;
    }

    boolean allScenesBeforeDurchlaufprobe() {
        Set<SceneDTO> allScenes = new HashSet<>(scenes);
        for (RehearsalDTO rehearsal : rehearsals) {
            if (rehearsal.equals(durchlaufprobe)) {
                break;
            }
            plan.get(rehearsal).forEach(allScenes::remove);
        }
        return allScenes.isEmpty();
    }

    private RehearsalDTO findDurchlaufprobe() {
        for (RehearsalDTO rehearsal : rehearsals) {
            if (plan.get(rehearsal).size() == scenes.size()) {
                return rehearsal;
            }
        }
        throw new IllegalStateException("No Durchlaufprobe found");
    }

    double completenessBeforeDLP() {
        double result = 0;
        List<Pair<RehearsalDTO, List<SceneDTO>>> rehearsalsBeforeDLP = new ArrayList<>();
        for (RehearsalDTO rehearsalDTO : rehearsals) {
            if (rehearsalDTO.equals(durchlaufprobe)) {
                break;
            }
            rehearsalsBeforeDLP.add(new Pair<>(rehearsalDTO, plan.get(rehearsalDTO)));
        }

        Map<SceneDTO, Double> scenesBeforeDLP = new HashMap<>();
        for (Pair<RehearsalDTO, List<SceneDTO>> pair : rehearsalsBeforeDLP) {
            List<SceneDTO> scenes = pair.second();
            RehearsalDTO rehearsal = pair.first();
            for (SceneDTO scene : scenes) {
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
        for (SceneDTO scene : plan.get(durchlaufprobe)) {
            result += Analyzer.completenessScore(durchlaufprobe, scene) * scene.getLength() / Analyzer.lengthOfPlay;
        }
        return result;
    }

    double totalCompleteness() {
        double result = 0;
        double planLength = 0;
        for (Pair<RehearsalDTO, SceneDTO> pair : plan.getAllPairs()) {
            result += Analyzer.completenessScore(pair.first(), pair.second())
                    * pair.second().getLength();
            planLength += pair.second().getLength();
            //        (params.getAverageRehearsalLength() * (rehearsals.size() -1) + Analyzer.lengthOfPlay); //lengthOfPlay für DLP
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

        for (RehearsalDTO rehearsalDTO : rehearsals) {
            List<SceneDTO> scenes = new ArrayList<>(plan.get(rehearsalDTO));
            int numberOfLumps = Analyzer.getNumberOfLumps(scenes.toArray(new SceneDTO[0]));
            result += 1.0 / numberOfLumps;
        }
        return result / rehearsals.size();
    }

    double overSize() {
        double result = 0;
        for (RehearsalDTO r : rehearsals) {
            if (r.equals(durchlaufprobe)) {
                continue;
            }
            double rehearsalLength = plan.getLengthOfRehearsal(r);
            double oversize = Math.max(0,
                    (rehearsalLength - params.getAverageRehearsalLength()) / params.getAverageRehearsalLength());
            result += Math.pow(oversize, 2);
        }
        return 1 - result / (rehearsals.size() - 1);
    }

    double getExpectedNumberOfRepeats() {
        //return (double) plan.getAllPairs().size() / scenes.size();
        double lengthOfPlay = scenes.stream()
                .mapToDouble(SceneDTO::getLength)
                .sum();
        int amountOfAllScenes = (int)((params.getAverageRehearsalLength() * scenes.size() * rehearsals.size())
                / lengthOfPlay);
        return (double) (amountOfAllScenes + scenes.size()) / scenes.size(); //scenes.size() is added to reflect DLP
    }

    double getMinimumRepeats() {
        return numberOfRepeats.values().stream()
                .mapToDouble(Double::doubleValue)
                .min()
                .orElse(0) / expectedNumberOfRepeats;
    }

    double getMedianRepeats() {
        return numberOfRepeats.values().stream()
                .mapToDouble(Double::doubleValue)
                .sorted()
                .skip(numberOfRepeats.size() / 2)
                .findFirst()
                .orElse(0) / expectedNumberOfRepeats;
    }

    public Map<SceneDTO, Double> getNumberOfRepeats() {
        if (numberOfRepeats == null) {
            numberOfRepeats = new HashMap<>();
            for (Pair<RehearsalDTO, SceneDTO> pair : plan.getAllPairs()) {
                double completeness = Analyzer.completenessScore(pair.first(), pair.second());
                numberOfRepeats.put(pair.second(),
                        numberOfRepeats.getOrDefault(pair.second(), 0.0)
                                + completeness);
            }
        }
        return numberOfRepeats;
    }

    int getNumberOfRolesInRehearsal(RehearsalDTO rehearsal) {
        List<RoleDTO> roles = new ArrayList<>();
        List<SceneDTO> scenes = plan.get(rehearsal);
        for (SceneDTO scene : scenes) {
            List<Pair<RoleDTO, Boolean>> sceneRoles = Analyzer.rolesForScene.get(scene);
            for (Pair<RoleDTO, Boolean> role : sceneRoles) {
                if (!roles.contains(role.first())) {
                    roles.add(role.first());
                }
            }
        }
        return roles.size();
    }

    double getRoleNumberScore() {
        double result = 0;
        for (RehearsalDTO rehearsalDTO : rehearsals) {
            result += Math.max(0, getNumberOfRolesInRehearsal(rehearsalDTO) - 4);//TODO magic number 4
        }
        result /= rehearsals.size();
        result /= Analyzer.numberOfRoles -  4; //TODO magic number 4
        return 1 - result;
    }
}
