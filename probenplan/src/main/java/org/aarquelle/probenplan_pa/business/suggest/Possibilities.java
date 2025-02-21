package org.aarquelle.probenplan_pa.business.suggest;

import org.aarquelle.probenplan_pa.data.dao.ReadDAO;
import org.aarquelle.probenplan_pa.data.dao.Transaction;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.RehearsalSceneTable;
import org.aarquelle.probenplan_pa.dto.SceneDTO;

import java.util.*;
import java.util.function.BiFunction;

public class Possibilities {

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


    public static int getNumberOfMissingActorsForScene(RehearsalDTO rehearsal, SceneDTO scene, boolean maybe) {
        try (Transaction t = new Transaction()) {
            ReadDAO dao = t.getReadDAO();
            return dao.getNumberOfMissingActorsForScene(rehearsal, scene, maybe);
        }
    }

    /**
     * Returns a table with the number of missing actors for each scene in each rehearsal. This only counts actors
     * who definitely don't have time, not including those who maybe have time.
     */
    public static RehearsalSceneTable missingActors() {
        return mapFunctionToSceneTable((rehearsal, scene)
                -> getNumberOfMissingActorsForScene(rehearsal, scene, false));
    }

    /**
     * Returns a table with the number of actors who maybe have time for each scene in each rehearsal.
     */
    public static RehearsalSceneTable uncertainActors() {
        return mapFunctionToSceneTable((rehearsal, scene)
                -> getNumberOfMissingActorsForScene(rehearsal, scene, true));
    }

    private static RehearsalSceneTable mapFunctionToSceneTable(
            BiFunction<RehearsalDTO, SceneDTO, Integer> function) {
        try (Transaction t = new Transaction()) {
            ReadDAO dao = t.getReadDAO();
            RehearsalSceneTable table = new RehearsalSceneTable();
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

    /*public RehearsalSceneTable tableNumberOfRoles() {
        try (Transaction t = new Transaction()) {
            ReadDAO dao = t.getReadDAO();
            RehearsalSceneTable table = new RehearsalSceneTable();
            List<RehearsalDTO> rehearsals = dao.getRehearsals();
            List<SceneDTO> scenes = dao.getScenes();
            for (RehearsalDTO rehearsal : rehearsals) {
                for (SceneDTO scene : scenes) {
                    table.set(reh)
                }
            }
            return table;
        }
    }
    */

}
