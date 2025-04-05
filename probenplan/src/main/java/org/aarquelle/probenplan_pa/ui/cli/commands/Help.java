package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.ui.cli.CancelCommandException;
import static org.aarquelle.probenplan_pa.ui.cli.out.Out.*;

public class Help extends AbstractCommand {
    public Help() {
        super("help", "Displays help information for commands.");
    }

    @Override
    public void execute(String[] args) throws CancelCommandException {
        info("Super nützliche Hilfe:");
    }
}
