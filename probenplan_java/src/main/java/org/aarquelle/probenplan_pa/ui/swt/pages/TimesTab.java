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

package org.aarquelle.probenplan_pa.ui.swt.pages;

import org.aarquelle.probenplan_pa.ui.API;
import org.aarquelle.probenplan_pa.ui.swt.widgets.CustomGroups;
import org.aarquelle.probenplan_pa.ui.swt.widgets.OptionTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import java.util.List;

public class TimesTab extends Composite {
    List<String> actorNames;
    List<String> rehearsalNames;

    public TimesTab(Composite parent, List<String> actorNames, List<String> rehearsalNames) {
        super(parent, SWT.NONE);
        this.actorNames = actorNames;
        this.rehearsalNames = rehearsalNames;
        setLayout(new GridLayout());
        Display d = Display.getCurrent();
        Group timesImportGroup = CustomGroups.createImportRow(this, "Import times",
                List.of("From Clipboard", "From URL", "From File"),
                List.of(false, true, false),
                List.of(() -> System.out.println("Clippy"), () -> System.out.println("NOT IMPLEMENTED"), () -> System.out.println("NOT IMPLEMENTED")));
        Composite tableComp = new OptionTable(this, x -> API.getActorNames(), x -> API.getRehearsalNames(),
                (col, row) -> API.hasTime(col, row),
                List.of("Hat Zeit", "Hat vielleicht Zeit", "Hat keine Zeit"),
                d.getSystemColor(SWT.COLOR_GREEN),
                d.getSystemColor(SWT.COLOR_YELLOW),
                d.getSystemColor(SWT.COLOR_RED));
        addListener(SWT.Resize, e -> tableComp.pack());
    }
}
