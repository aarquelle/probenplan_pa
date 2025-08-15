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


import org.aarquelle.probenplan_pa.entity.Entity;
import org.aarquelle.probenplan_pa.ui.API;
import org.eclipse.swt.graphics.Color;

public class TableCell {
    private Color[] colors;
    private int state;
    private String[] tooltips;
    private final Entity a, b;
    public TableCell(Entity a, Entity b, String[] tooltips, Color... colors) {
        this.a = a;
        this.b = b;
        this.tooltips = tooltips;
        this.colors = colors;
    }

    public void click() {
        state = state < colors.length - 1 ? state + 1 : 0;
        API.setRelation(a, b, state);
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
        state = target % colors.length;
    }

    public String getTooltip() {
        if (tooltips == null) {
            return null;
        }
        return tooltips[state];
    }


}
