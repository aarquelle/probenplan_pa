package org.aarquelle.probenplan_pa.ui.cli.completers;

import org.aarquelle.probenplan_pa.ui.cli.CommandLineUI;
import org.jline.builtins.Completers;
import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;
import java.util.Set;

public class CommandCompleter implements Completer {
    Set<String> commands = CommandLineUI.INSTANCE.getCommands().keySet();

    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        for (String command : commands) {
            candidates.add(new Candidate(command));
        }
    }
}
