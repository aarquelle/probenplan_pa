package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.Main;
import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.ui.cli.out.Out;

public class ShowParams extends AbstractCommand {
    public ShowParams() {
        super("show-params", "Zeigt die aktuellen Parameter an.");
    }


    @Override
    public void execute(String[] args) throws BusinessException {
        String[] params = Main.params.getParamNames();
        for (String s : params) {
            Out.infoPr(s + "=");
            Out.line(Main.params.getValueFromString(s));
        }
    }
}
