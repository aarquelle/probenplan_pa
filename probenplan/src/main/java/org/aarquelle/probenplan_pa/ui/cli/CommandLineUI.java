package org.aarquelle.probenplan_pa.ui.cli;

import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.ui.cli.commands.AbstractCommand;
import org.aarquelle.probenplan_pa.ui.cli.commands.*;
import org.aarquelle.probenplan_pa.ui.cli.commands.create.Echo;
import org.aarquelle.probenplan_pa.ui.cli.in.In;
import org.jline.reader.UserInterruptException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import static org.aarquelle.probenplan_pa.ui.cli.out.Out.*;

public class CommandLineUI {

    public static CommandLineUI INSTANCE;
    private final HashMap<String, AbstractCommand> commands = new HashMap<>();


    public CommandLineUI() {
        // Initialize commands
        putCommand(Help.class);
        putCommand(Echo.class);
        putCommand(CsvActorsTimes.class);
        putCommand(CsvActorsRoles.class);
        // Add more commands as needed


        INSTANCE = this;
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
            } catch (UserInterruptException e) {
                line("Ciao.");
                System.exit(0);
            } catch (BusinessException e) {
                // Handle business exception
                error("Ein Fehler ist aufgetreten: " + e.getMessage());
            }
            catch (Exception e) {
                // Handle other exceptions
                error("Ein Fehler ist aufgetreten: " + e.getMessage());
                throw new RuntimeException(e);
            }
        }
    }

    public HashMap<String, AbstractCommand> getCommands() {
        return commands;
    }
}
