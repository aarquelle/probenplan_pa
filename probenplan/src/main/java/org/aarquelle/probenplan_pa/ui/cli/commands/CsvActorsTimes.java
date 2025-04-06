package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.ui.cli.CancelCommandException;
import org.aarquelle.probenplan_pa.util.CsvUtils;
import org.aarquelle.probenplan_pa.util.DateUtils;

import java.util.ArrayList;
import java.util.Comparator;
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
    public void execute(String[] args) throws CancelCommandException {
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

        if (!new HashSet<>(existingActors).containsAll(actors)) {
            error("Die Schauspielenden sind nicht in der Datenbank vorhanden. "
                    + "Bitte erstelle sie zuerst, z.B. mit dem Befehl 'csv-actors-roles'.");
            return;
        }


        List<RehearsalDTO> rehearsalDates = new ArrayList<>();
        System.out.println("table.length: " + table.length);
        for (int i = 1; i < table.length; i++) {
            RehearsalDTO rehearsalDate = new RehearsalDTO();
            rehearsalDate.setDate(DateUtils.getDate(table[i][0]));
            rehearsalDates.add(rehearsalDate);
        }



        for(int i = 0; i < table.length - 1; i++) {
            for (int j = 0; j < table[i+1].length - 1; j++) {
                String content = table[i + 1][j + 1];
                if (content.equals("x")) {
                    ActorDTO actor = actors.get(j);
                    RehearsalDTO rehearsalDate = rehearsalDates.get(i);
                    info("Schauspieler: " + actor + ", Probentermin: " + rehearsalDate);
                }
            }
        }

    }
}
