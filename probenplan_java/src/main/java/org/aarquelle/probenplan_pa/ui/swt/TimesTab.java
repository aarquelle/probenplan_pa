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
    Color evenColumns;
    Color oddColumns;
    int columnWidth = 150;

    public TimesTab(Composite parent, List<String> actorNames, List<String> rehearsalNames) {
        super(parent, SWT.NONE);
        this.actorNames = actorNames;
        this.rehearsalNames = rehearsalNames;
        setLayout(new GridLayout());
        Display d = Display.getCurrent();
        evenColumns = d.getSystemColor(SWT.COLOR_WIDGET_NORMAL_SHADOW);
        oddColumns = d.getSystemColor(SWT.COLOR_WIDGET_HIGHLIGHT_SHADOW);
        Group timesImportGroup = createImportRow();
        Composite tableComp = createTableComp();
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

    private Table createTableComp() {

        Table table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.addListener(SWT.MeasureItem, event -> {
            event.height = 30; // gewünschte Zeilenhöhe
        });


        List<TableColumn> columns = new ArrayList<>();
        TableColumn rehearsalCol = new TableColumn(table, SWT.NONE);
        rehearsalCol.setText("Date:");
        rehearsalCol.setWidth(columnWidth + 100);
        for (int i = 0; i < actorNames.size(); i++) {
            TableColumn c = new TableColumn(table, SWT.NONE);
            c.setText(actorNames.get(i));
            c.setWidth(columnWidth);
            columns.add(c);
        }

        // Beispielzeilen hinzufügen
        for (int i = 0; i < rehearsalNames.size(); i++) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, rehearsalNames.get(i));

            for (int j = 0; j < actorNames.size(); j++) {
                TableEditor editor = new TableEditor(table);
                Composite cellComposite = new Composite(table, SWT.BORDER);
                GridLayout cellLayout = new GridLayout(3, true);
                cellLayout.horizontalSpacing = 0;
                cellComposite.setLayout(cellLayout);
                Button hasTimeButton = new Button(cellComposite, SWT.RADIO | SWT.INHERIT_FORCE);
                Button maybeButton = new Button(cellComposite, SWT.RADIO);
                Button noTimeButton = new Button(cellComposite, SWT.RADIO);
                //cellComposite.setTr
                editor.grabHorizontal = true;
                editor.setEditor(cellComposite, item, j + 1);

            }


        }

        return table;
    }
}
