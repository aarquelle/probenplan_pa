package org.aarquelle.probenplan_pa.dto;

import java.util.HashMap;
import java.util.Map;

public class RehearsalSceneTable <N> {
    Map<RehearsalDTO, Map<SceneDTO, N>> map = new HashMap<>();

    public void set(RehearsalDTO rehearsal, SceneDTO scene, N count) {
        if (map.containsKey(rehearsal)) {
            map.get(rehearsal).put(scene, count);
        } else {
            Map<SceneDTO, N> sceneMap = new HashMap<>();
            sceneMap.put(scene, count);
            map.put(rehearsal, sceneMap);
        }
    }

    public N get(RehearsalDTO rehearsal, SceneDTO scene) {
        if (map.containsKey(rehearsal)) {
            if (map.get(rehearsal).containsKey(scene)) {
                return map.get(rehearsal).get(scene);
            }
        }
        throw new IllegalArgumentException("Map has not been fully set, no entry for " + rehearsal + " and " + scene);
    }
}
