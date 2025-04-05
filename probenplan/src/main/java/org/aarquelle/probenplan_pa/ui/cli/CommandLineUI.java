package org.aarquelle.probenplan_pa.ui.cli;

import org.aarquelle.probenplan_pa.ui.cli.commands.AbstractCommand;
import org.aarquelle.probenplan_pa.ui.cli.commands.*;
import org.aarquelle.probenplan_pa.ui.cli.commands.create.Echo;
import org.aarquelle.probenplan_pa.ui.cli.in.In;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static org.aarquelle.probenplan_pa.ui.cli.out.Out.*;

public class CommandLineUI {

    HashMap<String, AbstractCommand> commands = new HashMap<>();

    public static void main(String[] args) {
        CommandLineUI cli = new CommandLineUI();
        cli.start();
    }

    public CommandLineUI() {
        // Initialize commands
        putCommand(Help.class);
        putCommand(Echo.class);
        // Add more commands as needed
    }

    private void putCommand(Class<? extends AbstractCommand> commandClass) {
        try {
            AbstractCommand instance = commandClass.getDeclaredConstructor().newInstance();
            commands.put(instance.getName(), instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        info("Interaktiver Modus für Probenplan_PA. Gebe 'help' ein, um Hilfe zu erhalten.");
        // Add more functionality here as needed
        while(true) {
            try {
                String input = In.getCommandString();
                int spaceIndex = input.indexOf(" ");
                String firstWord = input.substring(0, spaceIndex == -1 ? input.length() : spaceIndex);
                String[] args = spaceIndex == -1 ? new String[0] : input.substring(spaceIndex + 1).split(" ");
                AbstractCommand c = commands.get(firstWord);
                if (c != null) {
                    c.execute(args);
                } else {
                    error("Unbekannter Befehl: " + firstWord);
                }
            } catch (CancelCommandException e) {
                // Handle cancel command exception
                info("Befehl abgebrochen.");
            } catch (Exception e) {
                // Handle other exceptions
                error("Ein Fehler ist aufgetreten: " + e.getMessage());
            }
        }
    }
}
