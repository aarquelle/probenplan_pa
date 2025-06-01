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

package org.aarquelle.probenplan_pa.entity;

import org.aarquelle.probenplan_pa.business.TestResults;
import org.aarquelle.probenplan_pa.util.Pair;

import java.util.*;

public class Plan {
    private final Map<Rehearsal, Set<Scene>> plan = new HashMap<>();
    private TestResults testResults;

    public void put(Rehearsal r, Scene s) {
        if (!plan.containsKey(r)) {
            plan.put(r, new HashSet<>(List.of(s)));
        } else {
            plan.get(r).add(s);
        }
    }

    public void remove(Rehearsal r, Scene s) {
        if (plan.containsKey(r)) {
            plan.get(r).remove(s);
            if (plan.get(r).isEmpty()) {
                plan.remove(r);
            }
        }
    }

    public Set<Scene> get(Rehearsal r) {
        Set<Scene> scenes = plan.get(r);
        return Objects.requireNonNullElse(scenes, Collections.emptySet());
    }

    public double totalLength() {
        double length = 0;
        for (Rehearsal r : plan.keySet()) {
            Set<Scene> sceneList = plan.get(r);
            for (Scene scene : sceneList) {
                length += scene.getLength();
            }
        }
        return length;
    }

    public Set<Rehearsal> getRehearsals() {
        return plan.keySet();
    }

    public List<Pair<Rehearsal, Scene>> getAllPairs() {
        List<Pair<Rehearsal, Scene>> pairs = new ArrayList<>();
        plan.forEach((r, value) -> value.forEach(s -> pairs.add(new Pair<>(r, s))));
        return pairs;
    }

    public double getLengthOfRehearsal(Rehearsal rehearsal) {
        double length = 0;
        Set<Scene> scenes = get(rehearsal);
        for (Scene scene : scenes) {
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

    public boolean hasScene(Rehearsal rehearsal, Scene scene) {
        Set<Scene> scenes = get(rehearsal);
        if (scenes == null) {
            return false;
        } else return scenes.contains(scene);
    }

    public Plan copy() {
        Plan clone = new Plan();
        for (Map.Entry<Rehearsal, Set<Scene>> entry : plan.entrySet()) {
            Rehearsal rehearsal = entry.getKey();
            Set<Scene> scenes = new HashSet<>(entry.getValue());
            clone.plan.put(rehearsal, scenes);
        }
        return clone;
    }
}
