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

import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

import java.util.HashMap;
import java.util.Map;

public class ResourceHandler {
    private static ResourceHandler INSTANCE;
    Display display;
    Map<Double, Color> colors = new HashMap<>();

    ResourceHandler(Display display) {
        this.display = display;
        INSTANCE = this;
    }

    public static Color getColor(Color lo, Color mi, Color hi, double percentage) {
        return INSTANCE.getColorPrivate(lo, mi, hi, percentage);
    }

    private Color getColorPrivate(Color lo, Color mi, Color hi, double percentage) {
        if (percentage <= 0) {
            return lo;
        } else if (percentage >= 1) {
            return hi;
        } else {
            if (percentage < 0.5) {
                hi = mi;
                percentage += 0.5;
            } else {
                lo = mi;
                percentage -= 0.5;
            }
            Color result = colors.get(percentage);
            if (result == null) {
                int r = lo.getRed() + (int) ((hi.getRed() - lo.getRed()) * percentage);
                int g = lo.getGreen() + (int) ((hi.getGreen() - lo.getGreen()) * percentage);
                int b = lo.getBlue() + (int) ((hi.getBlue() - lo.getBlue()) * percentage);
                result = new Color(display, r, g, b);
                colors.put(percentage, result);
            }
            return result;
        }
    }

    public void dispose() {
        for (Color c : colors.values()) {
            c.dispose();
        }
    }
}
