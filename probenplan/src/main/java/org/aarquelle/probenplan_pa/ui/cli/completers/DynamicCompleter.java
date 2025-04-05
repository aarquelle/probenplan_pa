package org.aarquelle.probenplan_pa.ui.cli.completers;

import org.jline.reader.Candidate;
import org.jline.reader.Completer;
import org.jline.reader.LineReader;
import org.jline.reader.ParsedLine;

import java.util.List;

public class DynamicCompleter implements Completer {

    private Completer completer;

    public void setCompleter(Completer completer) {
        this.completer = completer;
    }


    @Override
    public void complete(LineReader reader, ParsedLine line, List<Candidate> candidates) {
        completer.complete(reader, line, candidates);
    }
}
