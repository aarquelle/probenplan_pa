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

package org.aarquelle.probenplan_pa.ui.swt.widgets.input;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Widget;

import java.util.List;
import java.util.function.Function;

public class DropdownMenu<I> extends Composite{
    private List<I> items;
    private I selected;
    private final Combo combo;

    public DropdownMenu(Composite parent, List<I> items, String defaultValue,
                        Function<I, String> displayNameFunction) {
        super(parent, 0);
        setLayout(new GridLayout(1, true));
        this.items = List.copyOf(items);

        combo = new Combo(this, SWT.DROP_DOWN | SWT.READ_ONLY);
        combo.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true));
        String[] names = new String[items.size() + 1];
        names[0] = defaultValue;
        for (int i = 0; i < items.size(); i++) {
            names[i + 1] = displayNameFunction.apply(items.get(i));
        }
        combo.setItems(names);
        combo.select(0);

    }

    public I getSelected() {
        int index = combo.getSelectionIndex();
        assert index >= 0 && index <= items.size();
        if (index == 0) {
            return null;
        } else {
            return items.get(index - 1);
        }
    }

    public void select(int index) {
        combo.select(index);
    }

    public void selectLast() {
        combo.select(items.size() - 1);
    }

    public void select(I item) {
        combo.select(items.indexOf(item) + 1);
    }
}
