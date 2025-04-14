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
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.util.CsvUtils;
import org.aarquelle.probenplan_pa.util.DateUtils;
import org.aarquelle.probenplan_pa.util.Pair;

import java.util.ArrayList;
import java.util.List;

import static org.aarquelle.probenplan_pa.ui.cli.out.Out.*;

public class CsvTimes extends AbstractCommand {
    public CsvTimes() {
        super("csv-times", "Importiere copy-paste-Daten aus einer Tabelle, um Schauspielende, "
                + "Probentermine und Verfügbarkeiten anzulegen. Schaue in die README-Datei im Repository, "
                + "um zu erfahren, wie die Daten aussehen müssen.");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        String[][] table = CsvUtils.parseArgs(args);
        if (table.length == 0) {
            error("Die Tabelle ist leer.");
            return;
        }
        List<ActorDTO> actors = new ArrayList<>();
        for (int i = 1; i < table[0].length; i++) {
            ActorDTO actor = new ActorDTO();
            actor.setName(table[0][i]);
            actors.add(actor);
        }

        List<ActorDTO> existingActors = BasicService.getActors();

        for (ActorDTO a : actors) {
            if (!existingActors.contains(a)) {
                throw new BusinessException("\"" + a.getName() + "\" ist nicht in der Datenbank vorhanden. "
                        + "Bitte erstelle sie zuerst, z.B. mit dem Befehl 'csv-actors-roles'.");
            }
        }

        List<RehearsalDTO> existingRehearsals = BasicService.getRehearsals();

        List<RehearsalDTO> rehearsalDates = new ArrayList<>();
        for (int i = 1; i < table.length; i++) {
            RehearsalDTO rehearsal = new RehearsalDTO();
            rehearsal.setDate(DateUtils.getDate(table[i][0]));
            rehearsalDates.add(rehearsal);
            if (!existingRehearsals.contains(rehearsal)) {
                Creator.createRehearsal(rehearsal);
            }
        }


        for (int i = 0; i < table.length - 1; i++) {
            for (int j = 0; j < table[i + 1].length - 1; j++) {
                String content = table[i + 1][j + 1].toLowerCase();
                if (content.equals("x") || content.equals("?")) {
                    boolean maybe = content.equals("?");
                    ActorDTO actor = actors.get(j);
                    RehearsalDTO rehearsalDate = rehearsalDates.get(i);
                    Creator.hasNoTime(rehearsalDate, new Pair<>(actor, maybe));
                }
            }
        }
    }
}
