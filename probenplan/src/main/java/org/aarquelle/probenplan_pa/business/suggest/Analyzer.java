package org.aarquelle.probenplan_pa.business.suggest;

import org.aarquelle.probenplan_pa.data.dao.ReadDAO;
import org.aarquelle.probenplan_pa.data.dao.Transaction;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.RehearsalSceneTable;
import org.aarquelle.probenplan_pa.dto.SceneDTO;

import java.util.*;
import java.util.function.BiFunction;

public class Analyzer {

    static Map<SceneDTO, Integer> allRoles;
    static Map<SceneDTO, Integer> majorRoles;
    static RehearsalSceneTable<Integer> allMissing;
    static RehearsalSceneTable<Integer> majorMissing;
    static RehearsalSceneTable<Integer> allUncertain;
    static RehearsalSceneTable<Integer> majorUncertain;

    static RehearsalSceneTable<Double> scoreTable;

    public static void runAnalysis() {
        allRoles = tableNumberOfRoles(false);
        majorRoles = tableNumberOfRoles(true);
        allMissing = missingActors();
        majorMissing = majorMissingActors();
        allUncertain = uncertainActors();
        majorUncertain = majorUncertainActors();
        scoreTable = completenessScores();
    }

    public static Map<SceneDTO, Integer> tableNumberOfRoles(boolean onlyMajorRoles) {
        try (Transaction t = new Transaction()) {
            ReadDAO dao = t.getReadDAO();
            Map<SceneDTO, Integer> results = new HashMap<>();
            List<SceneDTO> scenes = dao.getScenes();
            for (SceneDTO scene : scenes) {
                if (onlyMajorRoles) {
                    results.put(scene, dao.getRolesForScene(scene, false).size());
                } else {
                    results.put(scene, dao.getRolesForScene(scene).size());
                }
            }
            return results;
        }
    }


    public static int getNumberOfMissingActorsForScene(RehearsalDTO rehearsal, SceneDTO scene, boolean maybe, boolean major) {
        try (Transaction t = new Transaction()) {
            ReadDAO dao = t.getReadDAO();
            return dao.getNumberOfMissingActorsForScene(rehearsal, scene, maybe, major);
        }
    }

    public static int getNumberOfMinorMissingActorsForScene(RehearsalDTO rehearsal, SceneDTO scene) {
        try (Transaction t = new Transaction()) {
            ReadDAO dao = t.getReadDAO();
            return dao.getNumberOfMissingActorsForScene(rehearsal, scene, false, true);
        }
    }

    /**
     * Returns a table with the number of missing actors for each scene in each rehearsal. This only counts actors
     * who definitely don't have time, not including those who maybe have time.
     */
    public static RehearsalSceneTable<Integer> missingActors() {
        return mapFunctionToSceneTable((rehearsal, scene)
                -> getNumberOfMissingActorsForScene(rehearsal, scene, false, false));
    }

    /**
     * Returns a table with the number of actors who maybe have time for each scene in each rehearsal.
     */
    public static RehearsalSceneTable<Integer> uncertainActors() {
        return mapFunctionToSceneTable((rehearsal, scene)
                -> getNumberOfMissingActorsForScene(rehearsal, scene, true, false));
    }

    public static RehearsalSceneTable<Integer> majorMissingActors() {
        return mapFunctionToSceneTable(
                (rehearsal, scene) -> getNumberOfMissingActorsForScene(rehearsal, scene, false, true));
    }

    public static RehearsalSceneTable<Integer> majorUncertainActors() {
        return mapFunctionToSceneTable(
                (rehearsal, scene) -> getNumberOfMissingActorsForScene(rehearsal, scene, true, true));
    }

    public static RehearsalSceneTable<Double> completenessScores() {
        return mapFunctionToSceneTable(
                (Analyzer::completenessScore)
        );
    }

    /**
     * Returns a value between 0 and 1 that represents how many actors are present in the rehearsal.
     * 1 means that all actors are definitely present, 0 means that no actors are present.
     */
    static double completenessScore (RehearsalDTO rehearsal, SceneDTO scene) {
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
    }

    private static double ratio (int a, int b) {
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

    private static <N> RehearsalSceneTable<N> mapFunctionToSceneTable(
            BiFunction<RehearsalDTO, SceneDTO, N> function) {
        try (Transaction t = new Transaction()) {
            ReadDAO dao = t.getReadDAO();
            RehearsalSceneTable<N> table = new RehearsalSceneTable<>();
            List<SceneDTO> scenes = dao.getScenes();
            List<RehearsalDTO> rehearsals = dao.getRehearsals();
            for (RehearsalDTO rehearsal : rehearsals) {
                for (SceneDTO scene : scenes) {
                    table.set(rehearsal, scene, function.apply(rehearsal, scene));
                }
            }
            return table;
        }
    }

}
