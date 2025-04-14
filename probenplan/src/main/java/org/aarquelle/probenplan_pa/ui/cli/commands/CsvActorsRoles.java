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
import org.aarquelle.probenplan_pa.util.CsvUtils;

import java.util.List;

@Deprecated
public class CsvActorsRoles extends AbstractCommand {

    public CsvActorsRoles() {
        super("csv-actors-roles", "Importiere Schauspielende und Rollen aus einem CSV-copy-paste");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        String[][] table = CsvUtils.parseArgs(args);
        if (table.length == 0) {
            throw new BusinessException("Die Tabelle ist leer.");
        }

        List<ActorDTO> existingActors = BasicService.getActors();
        List<RoleDTO> existingRoles = BasicService.getRoles();
        for (int i = 0; i < table[0].length; i++) {
            ActorDTO actor = new ActorDTO();
            actor.setName(table[0][i]);

            if (!existingActors.contains(actor)) {
                Creator.createActor(actor);
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
                        Creator.createRole(role);
                    } else {
                        Creator.updateRole(role);
                    }
                }
            }
        }
    }
}
