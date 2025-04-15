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
import org.aarquelle.probenplan_pa.business.suggest.Analyzer;
import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.aarquelle.probenplan_pa.dto.PlanDTO;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.RoleDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;
import org.aarquelle.probenplan_pa.util.CsvUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ExportToClipboard extends AbstractCommand {
    public ExportToClipboard() {
        super("export-to-clipboard", "Speichert den Plan als " +
                "Tabellendaten ins Clipboard.");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        List<RehearsalDTO> rehearsals = BasicService.getRehearsals();
        PlanDTO plan = Main.plan;
        if (plan == null) {
            throw new BusinessException("Erstelle zuerst einen Plan mit generate");
        }

        String[][] table = null;
        if (args.length < 1 || !Objects.equals(args[0], "1")) {
            table = new String[rehearsals.size() + 1] [3];
            table[0] = new String[]{"Datum", "Szenen", "Schauspielende"};
            for (int i = 0; i < rehearsals.size(); i++) {
                RehearsalDTO rehearsal = rehearsals.get(i);
                table[i + 1][0] = rehearsal.getDate().toString();

                List<SceneDTO> scenes = plan.get(rehearsal);
                StringBuilder scenesString = new StringBuilder();
                scenes.forEach(scene -> {
                    if (!scenesString.isEmpty()) {
                        scenesString.append(", ");
                    }
                    scenesString.append(scene.getName());
                });
                table[i + 1][1] = scenesString.toString();

                List<RoleDTO> fullRoles = BasicService.getRoles();
                StringBuilder actorsString = new StringBuilder();
                Set<RoleDTO> roles = new HashSet<>();
                for (SceneDTO s : scenes) {
                    roles.addAll(Analyzer.getRolesForScene(s));
                }
                Set<ActorDTO> actors = new HashSet<>();
                for (RoleDTO role : roles) {
                    for (RoleDTO fullRole : fullRoles) { //Nötig, weil getRolesForScene keine Actors auffüllt.
                        if (role.getId() == fullRole.getId()) {
                            actors.add(fullRole.getActor());
                        }
                    }
                }
                for (ActorDTO actor : actors) {
                    if (!actorsString.isEmpty()) {
                        actorsString.append(", ");
                    }
                    actorsString.append(actor.getName());
                }
                table[i + 1][2] = actorsString.toString();
            }
        } else {
            List<SceneDTO> scenes = BasicService.getScenes();
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

        String csv = CsvUtils.tableToCsv(table);
        CsvUtils.copyTextToClipboard(csv);

    }
}
