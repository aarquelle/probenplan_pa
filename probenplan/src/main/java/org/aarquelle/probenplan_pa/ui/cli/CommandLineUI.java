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

package org.aarquelle.probenplan_pa.ui.cli;

import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.ui.cli.commands.AbstractCommand;
import org.aarquelle.probenplan_pa.ui.cli.commands.*;
import org.aarquelle.probenplan_pa.ui.cli.in.In;
import org.jline.reader.UserInterruptException;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.stream.Stream;

import static org.aarquelle.probenplan_pa.ui.cli.out.Out.*;

public class CommandLineUI {

    public static CommandLineUI INSTANCE;
    private final HashMap<String, AbstractCommand> commands = new HashMap<>();


    public CommandLineUI() {
        // Initialize commands
        INSTANCE = this;

        putCommand(Help.class);
        putCommand(ImportLocks.class);
        putCommand(ImportScenes.class);
        putCommand(ImportTimes.class);
        putCommand(Exit.class);
        putCommand(Generate.class);
        putCommand(ShowData.class);
        putCommand(ClearData.class);
        //putCommand(Test.class);
        putCommand(Overview.class);
        putCommand(ExportToClipboard.class);
        putCommand(ShowParams.class);
        putCommand(SetParam.class);


    }

    private void putCommand(Class<? extends AbstractCommand> commandClass) {
        try {
            AbstractCommand instance = commandClass.getDeclaredConstructor().newInstance();
            commands.put(instance.getName(), instance);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    public void start() {
        info("Interaktiver Modus für Probenplan_PA. Gebe 'help' ein, um Hilfe zu erhalten.");
        while (true) {
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
            } catch (UserInterruptException e) {
                line("Ciao.");
                System.exit(0);
            } catch (BusinessException e) {
                // Handle business exception
                error("Ein Fehler ist aufgetreten: " + e.getMessage());
            } catch (Exception e) {
                // Handle other exceptions
                error("Ein fataler Fehler ist aufgetreten, und das Programm muss beendet werden: ");
                for (Throwable t = e; t != null; t = t.getCause()) {
                    error(t.getClass().getName() + ": " +t.getMessage());
                    Stream.of(t.getStackTrace()).forEach(
                            element -> error("\t" + element.toString())
                    );
                    if (t.getCause() != null) {
                        error("Caused by: ");
                    }
                }

                In.getString("Drücke Enter, um das Programm zu beenden.", "", false, null);
                System.exit(-1);
            }
        }
    }

    public HashMap<String, AbstractCommand> getCommands() {
        return commands;
    }
}
