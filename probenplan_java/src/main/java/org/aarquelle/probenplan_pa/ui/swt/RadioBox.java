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

package org.aarquelle.probenplan_pa.ui.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

public class RadioBox extends Composite {
    private final Button[] buttons;

    public RadioBox(Composite parent, Color... colors) {
        super(parent, SWT.BORDER);
        GridLayout cellLayout = new GridLayout(colors.length, true);
        cellLayout.horizontalSpacing = 0;
        setLayout(cellLayout);
        buttons = new Button[colors.length];
        for (int i = 0; i < colors.length; i++) {
            Button b = new Button(this, SWT.RADIO);
            buttons[i] = b;
        }

    }

    public void setSelection(int index, boolean selection) {
        buttons[index].setSelection(true);
    }

    public int getSelection() {
        for (int i = 0; i < buttons.length; i++) {
            if (buttons[i].getSelection()) {
                return i;
            }
        }
        return -1;
    }
}
