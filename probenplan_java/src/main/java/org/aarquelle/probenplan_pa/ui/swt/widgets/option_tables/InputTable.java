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

package org.aarquelle.probenplan_pa.ui.swt.widgets.option_tables;

import org.aarquelle.probenplan_pa.entity.Entity;
import org.aarquelle.probenplan_pa.util.SortedUniqueList;
import org.aarquelle.probenplan_pa.util.TriConsumer;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.widgets.Composite;

import java.util.List;

public abstract class InputTable<ROW extends Entity & Comparable<ROW>, COL extends Entity & Comparable<COL>>
        extends OptionTable<ROW, COL> {

    protected final int contentStates;
    protected final TriConsumer<COL, ROW, Integer> clickConsumer;

    public InputTable(Composite parent, SortedUniqueList<COL> syncedColEntities, SortedUniqueList<ROW> syncedRowEntities, List<String> tooltips, int numberOfMarginRows, int numberOfMarginCols, int contentStates, TriConsumer<COL, ROW, Integer> clickConsumer, Color... colors) {
        super(parent, syncedColEntities, syncedRowEntities, tooltips, numberOfMarginRows, numberOfMarginCols, colors);
        this.contentStates = contentStates;
        this.clickConsumer = clickConsumer;
    }

    @Override
    protected void drawMargin(TableCell<COL, ROW> cell, GC gc) {
        if (cell.row == 0) {
            if (cell.col > 0) {
                drawString(cell.colEntity.displayName(), cell, gc);
            }
        } else if (cell.col == 0) {
            drawString(cell.rowEntity.displayName(), cell, gc);
        } else {
            throw new IllegalArgumentException("This is not in the margins: col: " + cell.col + ", row: " + cell.row);
        }
    }

    @Override
    protected void drawContent(TableCell<COL, ROW> cell, GC gc) {
        fillBackground(cell, cell.getColor(), gc);
    }

    @Override
    protected String additionalCommonTooltip(COL colEntity, ROW rowEntity) {
        return "";
    }

    @Override
    protected TableCell<COL, ROW> createContentCell(int col, int row) {
        COL colEntity = getColEntity(col);
        ROW rowEntity = getRowEntity(row);
        return new TableCell<>(colEntity, rowEntity, col, row, colors.length,
                getTooltips(colEntity, rowEntity), clickConsumer, colors);
    }

    @Override
    protected TableCell<COL, ROW> createMarginCell(int col, int row) {
        if (col == 0) {
            if (row == 0) {
                return new TableCell<>(col, row);
            } else {
                return new TableCell<>(null, getRowEntity(row), col, row, 1, null,
                        (a,b,c) -> System.out.println(b.displayName()));
            }
        } else if (row == 0) {
            return new TableCell<>(getColEntity(col), null, col, row, 1, null,
                    (a,b,c) -> System.out.println(a.displayName()));
        } else {
            throw new IllegalArgumentException("This is not in the margins: col: " + col + ", row: " + row);
        }
    }
}
