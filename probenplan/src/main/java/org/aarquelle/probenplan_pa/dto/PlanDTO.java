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

package org.aarquelle.probenplan_pa.dto;

import org.aarquelle.probenplan_pa.dto.entity.Rehearsal;
import org.aarquelle.probenplan_pa.util.Pair;

import java.util.*;

public class PlanDTO implements Cloneable {
    private final Map<RehearsalDTO, List<SceneDTO>> plan = new HashMap<>();
    private TestResults testResults;

    public void put(RehearsalDTO r, SceneDTO s) {
        if (!plan.containsKey(r)) {
            plan.put(r, new ArrayList<>(List.of(s)));
        } else {
            plan.get(r).add(s);
        }
    }

    public void remove(RehearsalDTO r, SceneDTO s) {
        if (plan.containsKey(r)) {
            plan.get(r).remove(s);
            if (plan.get(r).isEmpty()) {
                plan.remove(r);
            }
        }
    }

    public List<SceneDTO> get(RehearsalDTO r) {
        List<SceneDTO> scenes = plan.get(r);
        if (scenes == null) {
            return Collections.emptyList();
        } else {
            scenes.sort(Comparator.comparing(SceneDTO::getPosition));
            return scenes;
        }
    }

    public List<SceneDTO> get(Rehearsal r) {
        //TODO DUMMY!
        return null;
    }

    public double totalLength() {
        double length = 0;
        for (RehearsalDTO r : plan.keySet()) {
            List<SceneDTO> sceneList = plan.get(r);
            for (SceneDTO scene : sceneList) {
                length += scene.getLength();
            }
        }
        return length;
    }

    public List<RehearsalDTO> getRehearsals() {
        List<RehearsalDTO> rehearsals = new ArrayList<>(plan.keySet());
        rehearsals.sort(Comparator.comparing(RehearsalDTO::getDate));
        return rehearsals;
    }

    public List<SceneDTO> getScenes() {
        Set<SceneDTO> scenes = new HashSet<>();
        for (List<SceneDTO> sceneList : plan.values()) {
            scenes.addAll(sceneList);
        }
        List<SceneDTO> l = new ArrayList<>(scenes);
        l.sort(Comparator.comparing(SceneDTO::getPosition));
        return l;
    }

    public String verboseToString() {
        StringBuilder sb = new StringBuilder();
        List<RehearsalDTO> rehearsals = getRehearsals();

        for (RehearsalDTO r : rehearsals) {
            sb.append(r.getDate()).append(": ");
            List<SceneDTO> scenes = get(r);
            for (SceneDTO scene : scenes) {
                sb.append(scene.getName()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length());
            sb.append("\n");
        }
        sb.append(testResults);
        return sb.toString();
    }

    public List<Pair<RehearsalDTO, SceneDTO>> getAllPairs() {
        List<Pair<RehearsalDTO, SceneDTO>> pairs = new ArrayList<>();
        plan.forEach((r, value) -> value.forEach(s -> pairs.add(new Pair<>(r, s))));
        return pairs;
    }

    public double getLengthOfRehearsal(RehearsalDTO rehearsal) {
        double length = 0;
        List<SceneDTO> scenes = get(rehearsal);
        for (SceneDTO scene : scenes) {
            length += scene.getLength();
        }
        return length;
    }

    public TestResults getTestResults() {
        return testResults;
    }

    public void setTestResults(TestResults testResults) {
        this.testResults = testResults;
    }

    public boolean hasScene(RehearsalDTO rehearsal, SceneDTO scene) {
        List<SceneDTO> scenes = get(rehearsal);
        if (scenes == null) {
            return false;
        } else return scenes.contains(scene);
    }

    public PlanDTO copy() {
        PlanDTO clone = new PlanDTO();
        //clone.testResults = testResults; //TODO deep copy
        for (Map.Entry<RehearsalDTO, List<SceneDTO>> entry : plan.entrySet()) {
            RehearsalDTO rehearsal = entry.getKey();
            List<SceneDTO> scenes = new ArrayList<>(entry.getValue());
            clone.plan.put(rehearsal, scenes);
        }
        return clone;
    }
}
