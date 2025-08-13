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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;

import java.util.List;

public class CustomGroups {
    public static Group createImportRow(Composite parent, String title, List<String> buttonNames,
                                        List<Boolean> withText,
                                        List<Runnable> functions) {
        if (buttonNames.size() != withText.size() || buttonNames.size() != functions.size()) {
            throw new IllegalArgumentException("Wrong number of arguments in List: "
                    + buttonNames + "; " + withText + "; " + functions.size());
        }
        Group g = new Group(parent, SWT.BORDER);
        g.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));

        int numCols = 0;
        for (Boolean b : withText) {
            numCols += b ? 2 : 1;
        }

        g.setLayout(new GridLayout(numCols, true));
        g.setText(title);

        for (int i = 0; i < buttonNames.size(); i++) {
            Button button;
            if (withText.get(i)) {
                Composite textComp = new Composite(g, SWT.BORDER);
                textComp.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false, 2, 1));
                GridLayout textLayout = new GridLayout(2, false);
                textComp.setLayout(textLayout);
                Text text = new Text(textComp, SWT.BORDER);
                text.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));
                button = new Button(textComp, SWT.PUSH);
            } else {
                button = new Button(g, SWT.PUSH);
                button.setLayoutData(new GridData((i < buttonNames.size() - 1) ? SWT.BEGINNING : SWT.END, SWT.CENTER,
                        false, false));
            }
            button.setText(buttonNames.get(i));
            Runnable r = functions.get(i);
            button.addListener(SWT.Selection, e -> r.run());
        }
        return g;
    }
}
