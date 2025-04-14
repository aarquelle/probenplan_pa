package org.aarquelle.probenplan_pa.ui.cli.commands;

import org.aarquelle.probenplan_pa.Main;
import org.aarquelle.probenplan_pa.business.BusinessException;

import static org.aarquelle.probenplan_pa.ui.cli.out.Out.*;

public class SetParam extends AbstractCommand {
    public SetParam() {
        super("set-param", "Setzt den Wert eines Parameters.");
    }

    @Override
    public void execute(String[] args) throws BusinessException {
        if (args.length != 2) {
            throw new BusinessException("Syntax: set-param <param-name> <value>");
        }
        String paramName = args[0];
        String value = args[1];
        try {
            Main.params.setValueFromString(paramName, value);
            infoPr("Parameter " + paramName + " gesetzt auf " + value);
        } catch (NumberFormatException e) {
            throw new BusinessException("Ungültiger Wert: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Ungültiger Parametername: " + e.getMessage());
        }
    }
}
