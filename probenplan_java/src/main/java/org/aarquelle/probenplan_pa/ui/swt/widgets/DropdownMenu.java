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

package org.aarquelle.probenplan_pa.ui.swt.widgets;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class DropdownMenu<I> {
    private List<I> items;
    private I selected;
    private String defaultValue;
    private final Combo combo;

    public DropdownMenu(Composite parent, List<I> items, String defaultValue,
                        Function<I, String> displayNameFunction) {
        this.items = List.copyOf(items);
        this.defaultValue = defaultValue;

        combo = new Combo(parent, SWT.DROP_DOWN | SWT.READ_ONLY);
        String[] names = new String[items.size() + 1];
        names[0] = defaultValue;
        for (int i = 0; i < items.size(); i++) {
            names[i + 1] = displayNameFunction.apply(items.get(i));
        }
        combo.setItems(names);
        combo.select(0);



        /*Button button = new Button(parent, SWT.PUSH);
        button.setText(defaultValue);

        Menu menu = new Menu(button);
        if (defaultValue != null) {
            MenuItem defaultItem = new MenuItem(menu, SWT.PUSH);
            defaultItem.setText(defaultValue);
            defaultItem.addListener(SWT.Selection, event -> {
                selected = null;
                button.setText(defaultValue);
            });
        }
        for (I i : items) {
            MenuItem menuItem = new MenuItem(menu, SWT.PUSH);
            String displayName = displayNameFunction.apply(i);
            menuItem.setText(displayName);
            menuItem.addListener(SWT.Selection, event -> {
                selected = i;
                button.setText(displayName);
            });
        }

        //button.setMenu(menu);
        menu.addListener(SWT.Activate, e -> menu.setVisible(true));
        button.addListener(SWT.Selection, e -> menu.notifyListeners(SWT.Activate, null));*/
    }

    public Optional<I> getSelected() {
        //return Optional.ofNullable(selected);
        int index = combo.getSelectionIndex();
        assert index >= 0 && index <= items.size();
        if (index == 0) {
            return Optional.empty();
        } else {
            return Optional.of(items.get(index - 1));
        }
    }
}
