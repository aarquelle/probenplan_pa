package org.aarquelle.probenplan_pa.business.suggest;

import org.aarquelle.probenplan_pa.dto.*;
import org.aarquelle.probenplan_pa.util.Pair;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Evaluator {

    RehearsalDTO durchlaufprobe;
    PlanDTO plan;
    ParamsDTO params;
    List<RehearsalDTO> rehearsals;
    List<SceneDTO> scenes;


    double totalLengthOfRehearsals;

    public Evaluator (PlanDTO plan, ParamsDTO params) {
        this.plan = plan;
        this.params = params;
        this.rehearsals = plan.getRehearsals();
        this.scenes = plan.getScenes();
        this.totalLengthOfRehearsals = plan.totalLength();

        durchlaufprobe = findDurchlaufprobe();
    }

    public double evaluate () {
        return totalCompleteness() * 2 + dlpCompleteness();
    }

    boolean allScenesBeforeDurchlaufprobe () {
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
            result += Analyzer.completenessScore(pair.getFirst(), pair.getSecond())
                    * pair.getSecond().getLength() / totalLengthOfRehearsals;
        }

        return result;
    }
}
