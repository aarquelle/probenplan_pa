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
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;

import java.util.ArrayList;
import java.util.List;

public class OptionTable extends Composite {

    int columnWidth = 150;
    int rowHeight = 30;

    List<List<RadioBox>> boxes;

    public OptionTable(Composite parent, List<String> colNames, List<String> rowNames, List<String> secondaryCols,
                       Color... colors) {
        super(parent, SWT.NONE);
        boxes = new ArrayList<>();
        setLayout(new GridLayout());
        setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
        Table table = new Table(this, SWT.BORDER);
        table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));
        table.setHeaderVisible(true);
        table.setLinesVisible(true);
        table.addListener(SWT.MeasureItem, event -> {
            event.height = rowHeight; // gewünschte Zeilenhöhe
        });


        List<TableColumn> columns = new ArrayList<>();
        TableColumn rehearsalCol = new TableColumn(table, SWT.NONE);
        rehearsalCol.setText("Date:");
        rehearsalCol.setWidth(columnWidth + 100);
        for (int i = 0; i < colNames.size(); i++) {
            TableColumn c = new TableColumn(table, SWT.NONE);
            c.setText(colNames.get(i));
            c.setWidth(columnWidth);
            columns.add(c);
        }

        for (int i = 0; i < rowNames.size(); i++) {
            TableItem item = new TableItem(table, SWT.NONE);
            item.setText(0, rowNames.get(i));
            List<RadioBox> thisRow = new ArrayList<>();
            boxes.add(thisRow);

            for (int j = 0; j < colNames.size(); j++) {
                TableEditor editor = new TableEditor(table);
                RadioBox cellComposite = new RadioBox(table, colors);
                thisRow.add(cellComposite);
                editor.grabHorizontal = true;
                editor.setEditor(cellComposite, item, j + 1);
            }
        }
    }

    public void setRadioBox(int row, int col, int index) {
        boxes.get(row).get(col).setSelection(index, true);
    }
}
