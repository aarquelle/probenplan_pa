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
import org.aarquelle.probenplan_pa.util.TriConsumer;
import org.eclipse.swt.graphics.Color;

public class TableCell<COL extends Entity, ROW extends Entity> {
    private final Color[] colors;
    private int state;
    private int states;
    private final String[] tooltips;
    final COL colEntity;
    final ROW rowEntity;
    final int col;
    final int row;
    private final TriConsumer<COL, ROW, Integer> clickConsumer;

    /**
     * @param colEntity The entity associated with the column of this cell.
     * @param rowEntity The entity associated with the row of this cell.
     * @param col The col position in the table.
     * @param row The row position in the table.
     * @param states The number of states this may have.
     * @param tooltips The tooltips according to state. Must be null, or of the same size as {@code states}.
     * @param colors Usually the colors according to state. In a
     * {@link org.aarquelle.probenplan_pa.ui.swt.pages.PlanTab}, however, TODO
     */
    public TableCell(COL colEntity, ROW rowEntity, int col, int row, int states, String[] tooltips, TriConsumer<COL, ROW, Integer> clickConsumer,
                     Color... colors) {
        assert tooltips == null || tooltips.length == states;
        this.colEntity = colEntity;
        this.rowEntity = rowEntity;
        this.col = col;
        this.row = row;
        this.states = states;
        this.tooltips = tooltips;
        this.colors = colors;
        this.clickConsumer = clickConsumer != null ? clickConsumer : (a,b,c) -> {};
    }

    public TableCell(COL colEntity, ROW rowEntity, int col, int row, int states, String[] tooltips, Color... colors) {
        this(colEntity, rowEntity, col, row, states, tooltips, null, colors);
    }

    public TableCell(int col, int row, int states) {
        this(null, null, col, row, states, null);
    }

    public TableCell(int col, int row) {
        this(col, row, 1);
    }

    public void click() {
        setState(state + 1);
        clickConsumer.accept(colEntity, rowEntity, state);
        //API.setRelation(colEntity, rowEntity, state);//TODO Mit Lambda lösen??
    }

    public Color getColor() {
        if (colors == null) {
            return null;
        }
        return colors[state];
    }

    public int getState() {
        return state;
    }

    public void setState(int target) {
        state = target % states;
    }

    public String getTooltip() {
        if (tooltips == null) {
            return null;
        }
        return tooltips[state];
    }


}
