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
import java.util.stream.Collectors;

public class Plan {
    private final Map<Rehearsal, List<Scene>> plan = new HashMap<>();
    private TestResults testResults;

    public void put(Rehearsal r, Scene s) {
        if (!plan.containsKey(r)) {
            plan.put(r, new ArrayList<>(List.of(s)));
        } else {
            //TODO entfernen
            if (plan.get(r).contains(s)) {
                System.out.println("BÖÖÖÖSEEE!!!");
            }
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

    public List<Scene> get(Rehearsal r) {
        List<Scene> scenes = plan.get(r);
        if (scenes == null) {
            return Collections.emptyList();
        } else {
            return scenes.stream().sorted().collect(Collectors.toList()); //TODO Nur einmal sortieren? Beim Einfügen sortieren?
        }
    }

    public double totalLength() {
        double length = 0;
        for (Rehearsal r : plan.keySet()) {
            List<Scene> sceneList = plan.get(r);
            for (Scene scene : sceneList) {
                length += scene.getLength();
            }
        }
        return length;
    }

    public List<Rehearsal> getRehearsals() {
        return plan.keySet().stream().sorted().toList();
        /*List<Rehearsal> rehearsals = new ArrayList<>(plan.keySet());
        rehearsals.sort(Comparator.comparing(Rehearsal::getDate)); //TODO Nur beim einsortieren auf Reihenfolge achten
        return rehearsals;*/
    }

    public List<Scene> getScenes() {
        Set<Scene> scenes = new HashSet<>();
        for (List<Scene> sceneList : plan.values()) {
            scenes.addAll(sceneList);
        }
        List<Scene> l = new ArrayList<>(scenes);
        l.sort(Comparator.comparing(Scene::getPosition)); //TODO Beim einfügen sortieren
        return l;
    }

    public List<Pair<Rehearsal, Scene>> getAllPairs() {
        List<Pair<Rehearsal, Scene>> pairs = new ArrayList<>();
        plan.forEach((r, value) -> value.forEach(s -> pairs.add(new Pair<>(r, s))));
        return pairs;
    }

    public double getLengthOfRehearsal(Rehearsal rehearsal) {
        double length = 0;
        List<Scene> scenes = get(rehearsal);
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
        List<Scene> scenes = get(rehearsal);
        if (scenes == null) {
            return false;
        } else return scenes.contains(scene);
    }

    public Plan copy() {
        Plan clone = new Plan();
        //clone.testResults = testResults; //TODO deep copy
        for (Map.Entry<Rehearsal, List<Scene>> entry : plan.entrySet()) {
            Rehearsal rehearsal = entry.getKey();
            List<Scene> scenes = new ArrayList<>(entry.getValue());
            clone.plan.put(rehearsal, scenes);
        }
        return clone;
    }
}
