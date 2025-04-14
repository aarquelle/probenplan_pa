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

import org.aarquelle.probenplan_pa.ui.cli.CommandLineUI;

import java.util.Map;

import static org.aarquelle.probenplan_pa.ui.cli.out.Out.*;

public class Help extends AbstractCommand {
    public Help() {
        super("help", "Zeigt diese Hilfe an.");
    }

    @Override
    public void execute(String[] args) {
        info("Die folgenden Befehle sind verfügbar:");
        CommandLineUI.INSTANCE.getCommands().entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> {
                    String name = entry.getKey();
                    String description = entry.getValue().getDescription();
                    info("  " + name + ": " + description);
                });
    }
}
