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
        this.allRoles = Possibilities.tableNumberOfRoles(false);
        this.majorRoles = Possibilities.tableNumberOfRoles(true);
        this.allMissing = Possibilities.missingActors();
        this.majorMissing = Possibilities.majorMissingActors();
        this.allUncertain = Possibilities.uncertainActors();
        this.majorUncertain = Possibilities.majorUncertainActors();

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

    /**
     * Returns a value between 0 and 1 that represents how many actors are present in the rehearsal.
     * 1 means that all actors are definitely present, 0 means that no actors are present.
     */
    double completenessScore (RehearsalDTO rehearsal, SceneDTO scene) {
        int numberOfTotalRoles = allRoles.get(scene);
        int numberOfMajorRoles = majorRoles.get(scene);
        int numberOfMissing = allMissing.get(rehearsal, scene);
        int numberOfUncertain = allUncertain.get(rehearsal, scene);
        int numberOfMajorMissing = majorMissing.get(rehearsal, scene);
        int numberOfMajorUncertain = majorUncertain.get(rehearsal, scene);

        int numberOfMinorRoles = numberOfTotalRoles - numberOfMajorRoles;
        int numberOfMinorMissing = numberOfMissing - numberOfMajorMissing;
        int numberOfMinorUncertain = numberOfUncertain - numberOfMajorUncertain;

        double majorPoints = ratio(2 * numberOfMajorRoles, 2 * numberOfMajorRoles + numberOfMinorRoles);
        double minorPoints = 1 - majorPoints;

        double score = ratio(numberOfMajorMissing, numberOfMajorRoles) * majorPoints
                + ratio(numberOfMinorMissing, numberOfMinorRoles) * minorPoints
                + ratio(numberOfMajorUncertain, numberOfTotalRoles) * 0.5 * majorPoints
                + ratio(numberOfMinorUncertain, numberOfTotalRoles) * 0.5 * minorPoints;

        return 1 - score;

        /*return ratio(numberOfMajorCertain, numberOfTotalRoles)
                + ratio(numberOfMinorCertain, numberOfTotalRoles)
                + ratio(numberOfMajorUncertain, numberOfTotalRoles) * 0.5
                + ratio(numberOfMinorMissing, numberOfTotalRoles) * 0.5
                + ratio(numberOfMinorUncertain, numberOfTotalRoles) * 0.75;*/
    }

    private double ratio (int a, int b) {
        if (b != 0) {
            return a / (double) b;
        } else {
            if (a == 0) {
                return 0;
            } else {
                throw new IllegalArgumentException("Dividing " + a + " by 0");
            }
        }
    }

    private double proportionalImportance (SceneDTO scene) {
        return scene.getLength() / totalLength;
    }
}
