package org.aarquelle.probenplan_pa.dto;

import java.util.HashMap;
import java.util.Map;

public class RehearsalSceneTable {
    Map<RehearsalDTO, Map<SceneDTO, Integer>> map = new HashMap<>();

    public void set(RehearsalDTO rehearsal, SceneDTO scene, int count) {
        if (map.containsKey(rehearsal)) {
            map.get(rehearsal).put(scene, count);
        } else {
            Map<SceneDTO, Integer> sceneMap = new HashMap<>();
            sceneMap.put(scene, count);
            map.put(rehearsal, sceneMap);
        }
    }

    public int get(RehearsalDTO rehearsal, SceneDTO scene) {
        if (map.containsKey(rehearsal)) {
            if (map.get(rehearsal).containsKey(scene)) {
                return map.get(rehearsal).get(scene);
            }
        }
        throw new IllegalArgumentException("Map has not been fully set, no entry for " + rehearsal + " and " + scene);
    }
}
