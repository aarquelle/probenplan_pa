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

package org.aarquelle.probenplan_pa.business;

import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Scene;

import java.util.HashMap;
import java.util.Map;

public class RehearsalSceneTable <N> {
    Map<Rehearsal, Map<Scene, N>> map = new HashMap<>();

    public void set(Rehearsal rehearsal, Scene scene, N count) {
        if (map.containsKey(rehearsal)) {
            map.get(rehearsal).put(scene, count);
        } else {
            Map<Scene, N> sceneMap = new HashMap<>();
            sceneMap.put(scene, count);
            map.put(rehearsal, sceneMap);
        }
    }

    public N get(Rehearsal rehearsal, Scene scene) {
        if (map.containsKey(rehearsal)) {
            if (map.get(rehearsal).containsKey(scene)) {
                return map.get(rehearsal).get(scene);
            }
        }
        throw new IllegalArgumentException("Map has not been fully set, no entry for " + rehearsal + " and " + scene);
    }
}
