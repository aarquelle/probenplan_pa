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

package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.Main;
import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.entity.Plan;
import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;
import org.aarquelle.probenplan_pa.util.CsvUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ExportToClipboard extends AbstractCommand {
    public ExportToClipboard() {
        super("export-to-clipboard", "Speichert den Plan als " +
                "Tabellendaten ins Clipboard.");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        List<Rehearsal> rehearsals = BasicService.getRehearsals().stream().sorted().toList();
        Plan plan = Main.plan; //TODO Zu DataState ändern
        if (plan == null) {
            throw new BusinessException("Erstelle zuerst einen Plan mit generate");
        }

        String[][] table;
        if (args.length < 1 || !Objects.equals(args[0], "1")) {
            table = new String[rehearsals.size() + 1] [4];
            table[0] = new String[]{"Datum", "Szenen", "Schauspielende"};
            for (int i = 0; i < rehearsals.size(); i++) {
                Rehearsal rehearsal = rehearsals.get(i);
                table[i + 1][0] = rehearsal.getDate().toString();

                List<Scene> scenes = plan.get(rehearsal);
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
        } else {
            List<Scene> scenes = BasicService.getScenes().stream().sorted().toList();
            table = new String[rehearsals.size() + 1] [scenes.size() + 2];
            for (int i = 2; i < table[0].length; i++) {
                table[0][i] = scenes.get(i - 2).getName();
            }
            for (int i = 1; i < table.length; i++) {
                table[i][0] = rehearsals.get(i - 1).getDate().toString();
                table[i][1] = "x";
                for (int j = 2; j < table[j].length; j++) {
                    if (plan.get(rehearsals.get(i - 1)).contains(scenes.get(j - 2))) {
                        table[i][j] = "x";
                    } else {
                        table[i][j] = "";
                    }
                }
            }
        }
        CsvUtils.copyToClipboard(table);
    }
}
