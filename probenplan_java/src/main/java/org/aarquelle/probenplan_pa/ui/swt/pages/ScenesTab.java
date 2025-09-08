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

import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;
import org.aarquelle.probenplan_pa.ui.swt.widgets.CustomElements;
import org.aarquelle.probenplan_pa.ui.swt.widgets.option_tables.OptionTable;
import org.aarquelle.probenplan_pa.ui.swt.widgets.option_tables.ScenesTable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import java.util.List;

public class ScenesTab extends Composite {

    OptionTable<Scene, Role> optionTable;

    public ScenesTab(Composite parent) {
        super(parent, SWT.NONE);

        setLayout(new GridLayout(2, false));

        /*Group importRow = CustomElements.createImportRow(this, "Import scenes",
                List.of("From Clipboard", "From File"),
                List.of(false, false),
                List.of(() -> System.out.println("Clipboard"), () -> System.out.println("file")));
        ((GridData) (importRow.getLayoutData())).horizontalSpan = 2;*/
        Group modColumn = new Group(this, 0);
        modColumn.setText("Modify table");
        modColumn.setLayoutData(new GridData(GridData.FILL, GridData.FILL, false, true));
        modColumn.setLayout(new RowLayout(SWT.VERTICAL));

        Button addSceneButton = CustomElements.createAddSceneButton(modColumn);

        Button addRoleButton = CustomElements.createAddRoleButton(modColumn);

        optionTable = new ScenesTable(this);


    }

    @Override
    public void redraw() {
        super.redraw();
        optionTable.redraw();
    }
}
