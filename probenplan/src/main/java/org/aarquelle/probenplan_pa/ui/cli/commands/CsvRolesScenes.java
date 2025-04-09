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

public class CsvRolesScenes extends AbstractCommand {

    public CsvRolesScenes() {
        super("csv-scenes", "Importiere Rollen, Schauspielende" +
                " und Szenen aus einem CSV-copy-paste. Schaue ins README, um zu erfahren, " +
                "wie die Daten aussehen müssen.");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        String[][] table = CsvUtils.parseArgs(args);

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

        for (int i = 2; i < table.length; i++) {
            SceneDTO scene = createSceneDTO(table[i], i);
            Creator.createScene(scene);
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
    }

    private static @NotNull SceneDTO createSceneDTO(String[] table, int position) throws BusinessException {
        SceneDTO scene = new SceneDTO();
        scene.setName(table[0]);
        scene.setPosition(position);
        double length = Double.parseDouble(table[1].replace(",", "."));
        try {
            scene.setLength(length);
        } catch (NumberFormatException e) {
            throw new BusinessException("Die Länge der Szene \"" + scene.getName()
                    + "\" ist ungültig (eingegebener Wert: " + table[1] + "). Bitte gebe eine Zahl ein." +
                    "Erlaubt sind nur Ziffern von 0-9 und ein Punkt oder ein Komma als Dezimaltrennzeichen.");
        }
        return scene;
    }
}
