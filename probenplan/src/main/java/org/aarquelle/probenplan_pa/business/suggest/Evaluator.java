package org.aarquelle.probenplan_pa.business.suggest;

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
        this.rehearsals = plan.getRehearsals();
        this.scenes = plan.getScenes();
        this.totalLengthOfRehearsals = plan.totalLength();
        this.expectedNumberOfRepeats = getExpectedNumberOfRepeats();

        this.numberOfRepeats = getNumberOfRepeats();

        durchlaufprobe = findDurchlaufprobe();
    }

    public double evaluate() {
        return totalCompleteness() * 2
                + dlpCompleteness() * 1
                + completenessBeforeDLP() * 1
                + lumpiness() * 0.5
                + getMinimumRepeats() * 1
                + getMedianRepeats() * 0.5;
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
        for (Pair<RehearsalDTO, SceneDTO> pair : plan.getAllPairs()) {
            result += Analyzer.completenessScore(pair.first(), pair.second())
                    * pair.second().getLength() / totalLengthOfRehearsals;
        }

        return result;
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
}
