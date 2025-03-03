package org.aarquelle.probenplan_pa.business.suggest;

import org.aarquelle.probenplan_pa.dto.*;

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
    Map<SceneDTO, Integer> allRoles;
    Map<SceneDTO, Integer> majorRoles;
    RehearsalSceneTable allMissing;
    RehearsalSceneTable majorMissing;
    RehearsalSceneTable allUncertain;
    RehearsalSceneTable majorUncertain;

    double totalLength;

    public Evaluator (PlanDTO plan, ParamsDTO params) {
        this.plan = plan;
        this.params = params;
        this.rehearsals = plan.getRehearsals();
        this.scenes = plan.getScenes();
        this.totalLength = plan.totalLength();
        this.allRoles = Analyzer.tableNumberOfRoles(false);
        this.majorRoles = Analyzer.tableNumberOfRoles(true);
        this.allMissing = Analyzer.missingActors();
        this.majorMissing = Analyzer.majorMissingActors();
        this.allUncertain = Analyzer.uncertainActors();
        this.majorUncertain = Analyzer.majorUncertainActors();

        durchlaufprobe = findDurchlaufprobe();
    }

    public double evaluate () {
        return 0;
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



    private double proportionalImportance (SceneDTO scene) {
        return scene.getLength() / totalLength;
    }
}
