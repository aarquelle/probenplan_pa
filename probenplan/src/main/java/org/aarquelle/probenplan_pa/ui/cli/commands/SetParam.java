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
