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
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

import java.util.ArrayList;
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
        Group timesImportGroup = createImportRow();
        Composite tableComp = new OptionTable(this, actorNames, rehearsalNames, null,
                null,
                d.getSystemColor(SWT.COLOR_YELLOW),
                d.getSystemColor(SWT.COLOR_RED));
        addListener(SWT.Resize, e -> tableComp.pack());
    }

    private Group createImportRow() {
        Group importGroup = new Group(this, SWT.BORDER);
        importGroup.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));
        importGroup.setLayout(new GridLayout(4, true));
        importGroup.setText("Import times");

        Button clipboardButton = new Button(importGroup, SWT.PUSH);
        clipboardButton.setText("From Clipboard");

        Composite urlComp = new Composite(importGroup, SWT.BORDER);
        urlComp.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false, 2, 1));
        GridLayout urlLayout = new GridLayout(2, false);
        urlComp.setLayout(urlLayout);
        Text urlText = new Text(urlComp, SWT.BORDER);
        urlText.setLayoutData(new GridData(SWT.FILL, SWT.END, true, false));
        Button urlButton = new Button(urlComp, SWT.PUSH);
        urlButton.setText("From URL");

        Button importFileButton = new Button(importGroup, SWT.PUSH);
        importFileButton.setLayoutData(new GridData(SWT.END, SWT.CENTER, false, false));
        importFileButton.setText("From file");

        return importGroup;
    }


}
