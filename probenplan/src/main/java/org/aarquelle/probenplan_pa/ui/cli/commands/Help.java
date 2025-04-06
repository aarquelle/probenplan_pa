package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.ui.cli.CancelCommandException;
import org.aarquelle.probenplan_pa.ui.cli.CommandLineUI;

import java.util.Comparator;
import java.util.Map;

import static org.aarquelle.probenplan_pa.ui.cli.out.Out.*;

public class Help extends AbstractCommand {
    public Help() {
        super("help", "Zeigt diese Hilfe an.");
    }

    @Override
    public void execute(String[] args) throws CancelCommandException {
        info("Die folgenden Befehle sind verfügbar:");
        CommandLineUI.INSTANCE.getCommands().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String name = entry.getKey();
                    String description = entry.getValue().getDescription();
                    info("  " + name + ": " + description);
                });
    }
}
