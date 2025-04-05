package org.aarquelle.probenplan_pa.ui.cli.in;

import org.aarquelle.probenplan_pa.ui.cli.CancelCommandException;
import org.aarquelle.probenplan_pa.ui.cli.completers.CommandCompleter;
import org.aarquelle.probenplan_pa.ui.cli.completers.DynamicCompleter;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.LineReaderBuilder;
import org.jline.reader.impl.completer.NullCompleter;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.IntStream;

public class In {

    private static final DynamicCompleter dynamicCompleter = new DynamicCompleter();
    private static final CommandCompleter commandCompleter = new CommandCompleter();
    private static final Completer nullCompleter = new NullCompleter();

    private static final String INTERRUPT_COMMAND = ":q";
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
     * @throws CancelCommandException Thrown when the user types in {@link In#INTERRUPT_COMMAND}.
     */
    public static String getString(String prompt, String buffer, boolean trimmed, Completer completer) {
        String input;


        setCompleter(completer);
        input = reader.readLine(prompt(prompt), null, buffer);

        setCompleter(nullCompleter);
        if (trimmed) {
            input = input.trim();
        }
        if (input.equals(INTERRUPT_COMMAND)) {
            throw new CancelCommandException();
        }
        return input;
    }

    public static String getCommandString() {
        return getString(prompt("propenplan_pa"), "", true, commandCompleter);
    }

    private static String prompt(String prompt) {
        return prompt + ":>";
    }

    private static void setCompleter(Completer completer) {
        dynamicCompleter.setCompleter(completer);
    }
}
