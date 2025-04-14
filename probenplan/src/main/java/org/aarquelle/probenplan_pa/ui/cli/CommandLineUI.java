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
        putCommand(Help.class);
        putCommand(CsvLocks.class);
        putCommand(CsvScenes.class);
        putCommand(CsvTimes.class);
        putCommand(Exit.class);
        putCommand(Generate.class);
        putCommand(ShowData.class);
        putCommand(ClearData.class);
        putCommand(Test.class);
        putCommand(Overview.class);
        putCommand(SaveToCsv.class);
        putCommand(ShowParams.class);
        putCommand(SetParam.class);
        // Add more commands as needed


        INSTANCE = this;
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
                error("Ein fataler Fehler ist aufgetreten, und das Programm muss beendet werden: " + e.getMessage());
                Stream.of(e.getStackTrace()).forEach(
                        element -> error(element.toString())
                );
                throw new RuntimeException(e);
            }
        }
    }

    public HashMap<String, AbstractCommand> getCommands() {
        return commands;
    }
}
