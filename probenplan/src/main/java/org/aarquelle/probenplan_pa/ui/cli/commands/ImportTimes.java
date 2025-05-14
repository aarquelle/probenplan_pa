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
import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.util.CsvUtils;

import java.util.ArrayList;
import java.util.List;

import static org.aarquelle.probenplan_pa.ui.cli.out.Out.*;

public class ImportTimes extends AbstractCommand {
    public ImportTimes() {
        super("import-times", "Importiere copy-paste-Daten aus einer Tabelle, um Schauspielende, "
                + "Probentermine und Verfügbarkeiten anzulegen. Schaue in die README-Datei auf " + Main.REPOSITORY_URL
                + ", um zu erfahren, wie die Daten aussehen müssen.");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        String[][] table = CsvUtils.importFromClipboard();
        if (table.length == 0) {
            error("Die Tabelle ist leer.");
            return;
        }
        List<Actor> actors = new ArrayList<>();
        for (int i = 1; i < table[0].length; i++) {
            Actor actor = BasicService.getActorByName(table[0][i]);
            if (actor == null) {
                actor = BasicService.createActor();
                actor.setName(table[0][i]);
            }
            actors.add(actor);
        }

        List<Rehearsal> rehearsals = CsvUtils.getRehearsalsFromTable(table);


        for (int i = 0; i < table.length - 1; i++) {
            for (int j = 0; j < table[i + 1].length - 1; j++) {
                String content = table[i + 1][j + 1].toLowerCase();
                if (content.equals("x") || content.equals("?")) {
                    Actor actor = actors.get(j);
                    Rehearsal rehearsalDate = rehearsals.get(i);
                    if (content.equals("?")) {
                        rehearsalDate.addMaybeActor(actor);
                    } else {
                        rehearsalDate.addMissingActor(actor);
                    }
                }
            }
        }
        infoPr("Probentermine und Zeiten wurden erfolgreich importiert. Prüfe mit dem Befehl ");
        pr("'show-data'");
        info(", ob alles korrekt importiert wurde.");
    }
}
