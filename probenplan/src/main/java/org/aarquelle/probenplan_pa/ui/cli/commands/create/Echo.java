package org.aarquelle.probenplan_pa.ui.cli.commands.create;

import org.aarquelle.probenplan_pa.ui.cli.commands.AbstractCommand;
import static org.aarquelle.probenplan_pa.ui.cli.out.Out.*;

public class Echo extends AbstractCommand {
    public Echo() {
        super("echo", "Echos the given arguments.");
    }

    @Override
    public void execute(String[] args) {
        if (args.length == 0) {
            error("No arguments provided.");
            return;
        }
        for (String arg : args) {
            info(arg);
        }
    }
}
