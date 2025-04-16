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

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.business.create.Creator;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;
import org.aarquelle.probenplan_pa.ui.cli.out.Out;
import org.aarquelle.probenplan_pa.util.CsvUtils;
import org.aarquelle.probenplan_pa.util.DateUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ImportLocks extends AbstractCommand {

    public ImportLocks() {
        super("import-locks", "Importiere die festgelegten Proben und " +
                "Szenen aus Tabellen-Daten.");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        String[][] table = CsvUtils.importFromClipboard();

        List<SceneDTO> scenes = getSceneDTOS(table);

        List<RehearsalDTO> rehearsals = new ArrayList<>();
        for (int i = 1; i < table.length; i++) {
            RehearsalDTO rehearsal = new RehearsalDTO();
            rehearsal.setDate(DateUtils.getDate(table[i][0]));
            rehearsals.add(rehearsal);
        }

        List<RehearsalDTO> existingRehearsals = BasicService.getRehearsals();
        for (RehearsalDTO rehearsalDTO : rehearsals) {
            if (!existingRehearsals.contains(rehearsalDTO)) {
                throw new BusinessException("Es gibt keine Probe am "
                        + DateUtils.getString(rehearsalDTO.getDate()) + ", erstelle sie zuerst mit" +
                        " import-times.");
            }
        }

        for (int i = 1; i < table.length; i++) {
            if (table[i].length < 2) {
                continue;
            }
            String cell = table[i][1].toLowerCase().trim();
            Creator.lockRehearsal(rehearsals.get(i-1), cell.equals("x"));
            for (int j = 2; j < table[i].length; j++) {
                String cell2 = table[i][j].toLowerCase().trim();
                if (cell2.equals("x")) {
                    Creator.lockScene(scenes.get(j-2), rehearsals.get(i-1));
                }
            }
        }
        Out.info("Locks wurden erfolgreich importiert.");
    }

    private static @NotNull List<SceneDTO> getSceneDTOS(String[][] table) throws BusinessException {
        List<SceneDTO> scenes = new ArrayList<>();
        for (int i = 2; i < table[0].length; i++) {
            SceneDTO scene = new SceneDTO();
            scene.setName(table[0][i]);
            scenes.add(scene);
        }

        List<SceneDTO> existingScenes = BasicService.getScenes();
        for (SceneDTO sceneDTO : scenes) {
            if (!existingScenes.contains(sceneDTO)) {
                throw new BusinessException("Scene " + sceneDTO.getName() + " does not exist.");
            }
        }
        return scenes;
    }
}
