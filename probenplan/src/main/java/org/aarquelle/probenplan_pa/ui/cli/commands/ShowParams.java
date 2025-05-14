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
import org.aarquelle.probenplan_pa.business.Para;
import org.aarquelle.probenplan_pa.business.Params;
import org.aarquelle.probenplan_pa.ui.cli.out.Out;

import java.util.List;

public class ShowParams extends AbstractCommand {
    public ShowParams() {
        super("show-params", "Zeigt die aktuellen Parameter an.");
    }


    @Override
    public void execute(String[] args) throws BusinessException {
        List<Para<?>> params = Params.getAllParams();
        for (Para<?> p : params) {
            Out.infoPr(p.getName() + "=");
            Out.line(p.getValue() + " (default: " + p.getDefaultValue() + ")");
        }
    }
}
