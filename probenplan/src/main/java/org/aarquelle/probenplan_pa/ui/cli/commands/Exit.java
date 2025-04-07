package org.aarquelle.probenplan_pa.ui.cli.commands;

public class Exit extends AbstractCommand {
    public Exit() {
        super("exit", "Beendet das Programm.");
    }

    @Override
    public void execute(String[] args) {
        System.exit(0);
    }
}
