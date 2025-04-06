package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.business.create.Creator;
import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.aarquelle.probenplan_pa.dto.RoleDTO;
import org.aarquelle.probenplan_pa.ui.cli.CancelCommandException;
import org.aarquelle.probenplan_pa.util.CsvUtils;

import java.util.ArrayList;
import java.util.List;

import static org.aarquelle.probenplan_pa.ui.cli.out.Out.error;

public class CsvActorsRoles extends AbstractCommand{

    public CsvActorsRoles() {
        super("csv_actors_roles", "Importiere Schauspielende und Rollen aus einem CSV-copy-paste");
    }

    @Override
    public void execute(String[] args) throws CancelCommandException {
        String[][] table = CsvUtils.parseArgs(args);
        if (table.length == 0) {
            error("Die Tabelle ist leer.");
            return;
        }

        List<ActorDTO> existingActors = BasicService.getActors();
        List<RoleDTO> existingRoles = BasicService.getRoles();
        for (int i = 0; i < table[0].length; i++) {
            ActorDTO actor = new ActorDTO();
            actor.setName(table[0][i]);

            if (!existingActors.contains(actor)) {
                try {
                    Creator.createActor(actor);
                } catch (BusinessException e) {
                    error("Fehler beim Erstellen des Schauspielers: " + actor.getName() + ": ");
                    error(e.getMessage());
                    return;
                }
            }
            for (int j = 1; j < table.length; j++) {
                if (table[j].length <= i) {
                    continue;
                }
                String roleName = table[j][i];
                if (roleName != null && !roleName.isEmpty()) {
                    RoleDTO role = new RoleDTO();
                    role.setName(roleName);
                    role.setActor(actor);
                    if (!existingRoles.contains(role)) {
                        try {
                            Creator.createRole(role);
                        } catch (BusinessException e) {
                            error("Fehler beim Erstellen der Rolle: " + role.getName() + " für Schauspieler: "
                                    + actor.getName() + ": ");
                            error(e.getMessage());
                            return;
                        }
                    } else {
                        try {
                            Creator.updateRole(role);
                        } catch (BusinessException e) {
                            error("Fehler beim Aktualisieren der Rolle: " + role.getName() + " für Schauspieler: "
                                    + actor.getName() + ": ");
                            error(e.getMessage());
                            return;
                        }
                    }
                }
            }
        }

    }
}
