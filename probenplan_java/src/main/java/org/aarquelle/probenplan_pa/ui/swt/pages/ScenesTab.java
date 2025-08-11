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

import org.aarquelle.probenplan_pa.ui.swt.widgets.CustomGroups;
import org.aarquelle.probenplan_pa.ui.swt.widgets.OptionTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import java.util.List;

public class ScenesTab extends Composite {
    List<String> roleNames;
    List<String> sceneNames;
    List<String> actorNames;

    public ScenesTab(Composite parent, List<String> roleNames, List<String> sceneNames, List<String> actorNames) {
        super(parent, SWT.NONE);
        this.roleNames = roleNames;
        this.sceneNames = sceneNames;
        this.actorNames = actorNames;

        setLayout(new GridLayout());
        Display d = Display.getCurrent();

        Group importRow = CustomGroups.createImportRow(this, "Import scenes",
                List.of("From Clipboard", "From File"), List.of(false, false));
        OptionTable table = new OptionTable(this, roleNames, sceneNames, actorNames,
                List.of("Kommt nicht vor.", "Kleine Rolle", "Große Rolle"),
                null, d.getSystemColor(SWT.COLOR_YELLOW), d.getSystemColor(SWT.COLOR_GREEN));
        //addListener(SWT.Resize, e -> pack());

    }
}
