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

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.ui.swt.widgets.CustomElements;
import org.aarquelle.probenplan_pa.ui.swt.widgets.option_tables.InputTable;
import org.aarquelle.probenplan_pa.ui.swt.widgets.option_tables.OptionTable;
import org.aarquelle.probenplan_pa.ui.swt.widgets.option_tables.TimesTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;

import java.util.List;

public class TimesTab extends Composite {

    OptionTable<Rehearsal, Actor> optionTable;

    public TimesTab(Composite parent) {
        super(parent, SWT.NONE);
        setLayout(new GridLayout());
        Display d = Display.getCurrent();
        Group timesImportGroup = CustomElements.createImportRow(this, "Import times",
                List.of("From Clipboard", "From URL", "From File"),
                List.of(false, true, false),
                List.of(() -> System.out.println("Clippy"), () -> System.out.println("NOT IMPLEMENTED"), () -> System.out.println("NOT IMPLEMENTED")));
        optionTable = new TimesTable(this);
        addListener(SWT.Resize, e -> optionTable.pack());
    }

    public void updateData() {
        optionTable.updateData();
    }
}
