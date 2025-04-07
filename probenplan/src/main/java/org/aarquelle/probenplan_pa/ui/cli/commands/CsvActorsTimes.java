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
import java.util.HashSet;
import java.util.List;

import static org.aarquelle.probenplan_pa.ui.cli.out.Out.*;

public class CsvActorsTimes extends AbstractCommand {
    public CsvActorsTimes() {
        super("csv-actors-times", "Importiere copy-paste-Daten aus einer Tabelle, um Schauspielende, "
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

        actors.sort(ActorDTO::compareTo);
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
                    info(actor.getName() + " kann am " + rehearsalDate + (maybe ? " vielleicht." : "nicht."));
                    Creator.hasNoTime(rehearsalDate, new Pair<>(actor, maybe));
                }
            }
        }
    }
}
