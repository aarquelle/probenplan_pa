package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.business.create.Creator;

public class ClearData extends AbstractCommand {

    public ClearData() {
        super("clear-data", "Lösche alle Daten aus der Datenbank. ");
    }

    @Override
    public void execute(String[] args) {
        Creator.clearDB();
    }
}
