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

import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.Plan;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * The infos calculated here are intended for human consumption, not for the evaluator.
 */
public class InfoService {
    public static List<Role> missingRoles(Scene s, Rehearsal rehearsal) {
        Set<Role> l = new HashSet<>(s.getSmallRoles());
        l.addAll(s.getBigRoles());
        return l.stream().filter(role -> rehearsal.getMissingActors().contains(role.getActor()) ||
                rehearsal.getMaybeActors().contains(role.getActor())).toList();
    }

    /**
     * Create a String that can be copied into a program like LibreOffice Calc or Google Sheets
     * for a human-friendly representation of the plan.
     */
    public static String[][] csvPlan(Plan plan) {
        String[][] table;
        List<Rehearsal> rehearsals = BasicService.getRehearsals().toList();

        table = new String[rehearsals.size() + 1][4];
        table[0] = new String[]{"Datum", "Szenen", "Schauspielende"};
        for (int i = 0; i < rehearsals.size(); i++) {
            Rehearsal rehearsal = rehearsals.get(i);
            table[i + 1][0] = rehearsal.getDate().toString();

            List<Scene> scenes = plan.get(rehearsal).stream().sorted().toList();
            StringBuilder scenesString = new StringBuilder();
            scenes.forEach(scene -> {
                if (!scenesString.isEmpty()) {
                    scenesString.append(", ");
                }
                scenesString.append(scene.getName());
            });
            table[i + 1][1] = scenesString.toString();

            StringBuilder actorsString = new StringBuilder();
            Set<Role> roles = new HashSet<>();
            for (Scene s : scenes) {
                roles.addAll(s.getBigRoles());
                roles.addAll(s.getSmallRoles());
            }
            Set<Actor> neededActors = roles.stream().map(Role::getActor).collect(Collectors.toSet());
            Set<Actor> presentActors = new HashSet<>();
            Set<Actor> missingActors = new HashSet<>();

            neededActors.forEach(actor -> {
                if (rehearsal.getMissingActors().contains(actor)) {
                    missingActors.add(actor);
                } else {
                    presentActors.add(actor);
                }
            });

            for (Actor actor : presentActors) {
                if (!actorsString.isEmpty()) {
                    actorsString.append(", ");
                }
                actorsString.append(actor.getName());
            }
            if (!missingActors.isEmpty()) {
                actorsString.append(". Falls doch möglich: ");
                for (Actor actor : missingActors) {
                    actorsString.append(actor.getName()).append(", ");
                }
                actorsString.delete(actorsString.length() - 2, actorsString.length());
            }
            table[i + 1][2] = actorsString.toString();
            double rehearsalLength = 0;
            for (Scene s : scenes) {
                rehearsalLength += s.getLength();
            }
            table[i + 1][3] = String.valueOf(rehearsalLength);
        }
        return table;
    }
}
