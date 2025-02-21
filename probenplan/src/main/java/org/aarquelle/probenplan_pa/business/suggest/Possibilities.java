package org.aarquelle.probenplan_pa.business.suggest;

import org.aarquelle.probenplan_pa.data.dao.ReadDAO;
import org.aarquelle.probenplan_pa.data.dao.Transaction;
import org.aarquelle.probenplan_pa.dto.SceneDTO;

import java.util.*;

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
