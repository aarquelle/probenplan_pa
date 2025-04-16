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
import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.aarquelle.probenplan_pa.dto.RoleDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;
import org.aarquelle.probenplan_pa.util.CsvUtils;
import org.aarquelle.probenplan_pa.util.Pair;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import static org.aarquelle.probenplan_pa.ui.cli.out.Out.info;
import static org.aarquelle.probenplan_pa.ui.cli.out.Out.infoPr;
import static org.aarquelle.probenplan_pa.ui.cli.out.Out.pr;

public class ImportScenes extends AbstractCommand {

    public ImportScenes() {
        super("import-scenes", "Importiere Rollen, Schauspielende" +
                " und Szenen aus einem Tabellen-copy-paste. Schaue ins README, um zu erfahren, " +
                "wie die Daten aussehen müssen.");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        String[][] table = CsvUtils.importFromClipboard();

        List<RoleDTO> roles = new ArrayList<>();
        List<RoleDTO> existingRoles = BasicService.getRoles();
        List<ActorDTO> existingActors = BasicService.getActors();

        for (int i = 2; i < table[0].length; i++) {
            boolean roleExists = false;
            for (RoleDTO r : existingRoles) {
                if (r.getName().equals(table[0][i])) {
                    roles.add(r);
                    roleExists = true;
                    break;
                }
            }
            if (!roleExists) {
                RoleDTO r = new RoleDTO();
                r.setName(table[0][i]);
                ActorDTO actor = new ActorDTO();
                actor.setName(table[1][i]);
                boolean actorExists = false;
                for (ActorDTO a : existingActors) {
                    if (a.getName().equals(actor.getName())) {
                        actor = a;
                        actorExists = true;
                        break;
                    }
                }
                if (!actorExists) {
                    Creator.createActor(actor);
                    existingActors.add(actor);
                }
                r.setActor(actor);
                Creator.createRole(r);
                roles.add(r);
            }
        }

        List<SceneDTO> existingScenes = BasicService.getScenes();
        for (int i = 2; i < table.length; i++) {
            SceneDTO scene = createSceneDTO(table[i], i);
            if (existingScenes.contains(scene)) {
                scene = existingScenes.get(existingScenes.indexOf(scene));
            } else {
                Creator.createScene(scene);
            }
            for (int j = 2; j < table[i].length; j++) {
                String cell = table[i][j];
                if (cell != null && !cell.isEmpty()) {
                    RoleDTO role = roles.get(j - 2);
                    boolean small;
                    switch (cell){
                        case "x" -> small = false;
                        case "?" -> small = true;
                        default -> throw new BusinessException("Die Rolle \"" + role.getName() + "\" in der Szene \""
                                + scene.getName() + "\" ist ungültig. Bitte gebe \"x\" oder \"?\" ein.");
                    }
                    Creator.takesPart(scene, new Pair<>(role, small));
                }
            }
        }
        infoPr("Szenen und Rollen wurden erfolgreich importiert. Prüfe mit dem Befehl ");
        pr("'show-data'");
        info(", ob alles korrekt importiert wurde.");
    }

    private static @NotNull SceneDTO createSceneDTO(String[] table, int position) throws BusinessException {
        SceneDTO scene = new SceneDTO();
        scene.setName(table[0]);
        scene.setPosition(position);
        try {
            double length = Double.parseDouble(table[1].replace(",", "."));
            scene.setLength(length);
            if (length <= 0) {
                throw new BusinessException("Die Länge der Szene \"" + scene.getName()
                        + "\" ist ungültig (eingegebener Wert: " + length + "). Bitte gebe eine positive Zahl ein.");
            }
        } catch (NumberFormatException e) {
            throw new BusinessException("Die Länge der Szene \"" + scene.getName()
                    + "\" ist ungültig (eingegebener Wert: " + table[1] + "). Bitte gebe eine positive Zahl ein." +
                    "Erlaubt sind nur Ziffern von 0-9 und ein Punkt oder ein Komma als Dezimaltrennzeichen.");
        }
        return scene;
    }
}
