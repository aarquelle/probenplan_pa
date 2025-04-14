package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.business.create.Creator;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;
import org.aarquelle.probenplan_pa.util.CsvUtils;
import org.aarquelle.probenplan_pa.util.DateUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class CsvLocks extends AbstractCommand {

    public CsvLocks() {
        super("csvlocks", "Importiere die festgelegten Proben und Szenen aus CSV-Daten.");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        String[][] table = CsvUtils.parseArgs(args);

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
                throw new BusinessException("Rehearsal on " + rehearsalDTO.getDate() + " does not exist.");
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
