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

package org.aarquelle.probenplan_pa.ui.cli.in;

import org.aarquelle.probenplan_pa.ui.cli.completers.CommandCompleter;
import org.aarquelle.probenplan_pa.ui.cli.completers.DynamicCompleter;
import org.aarquelle.probenplan_pa.ui.cli.out.Out;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.NullCompleter;

public class In {

    private static final DynamicCompleter dynamicCompleter = new DynamicCompleter();
    private static final CommandCompleter commandCompleter = new CommandCompleter();
    private static final Completer nullCompleter = new NullCompleter();

    private static final LineReader reader = LineReaderBuilder.builder().completer(dynamicCompleter).build();

    /**
     * Read user input. All other input methods should be based on this.
     * Specific actions that should be able to be fired anywhere can be defined here.
     * This method is also utilized by macros.
     *
     * @param prompt    The prompt to be displayed before the input field.
     * @param buffer    The default input.
     * @param trimmed   If true, the input is trimmed.
     * @param completer The completer to be used.
     * @return The unmodified input.
     */
    public static String getString(String prompt, String buffer, boolean trimmed, Completer completer) {
        String input;


        setCompleter(completer);
        input = reader.readLine(Out.prompt(prompt), null, buffer);

        setCompleter(nullCompleter);
        if (trimmed) {
            input = input.trim();
        }
        return input;
    }

    public static String getCommandString() {
        String s = getString("probenplan_pa", "",
                true, commandCompleter);
        System.out.println(s);
        return s;
    }

    private static void setCompleter(Completer completer) {
        dynamicCompleter.setCompleter(completer);
    }
}
