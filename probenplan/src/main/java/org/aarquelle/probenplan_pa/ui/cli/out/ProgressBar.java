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

package org.aarquelle.probenplan_pa.ui.cli.out;


public class ProgressBar extends Out {
    int width = getTerminalWidth() - 10;
    int max;
    int currentProgress = 0;
    public ProgressBar(int max) {
        this.max = max;
        line("");
        pr("["+" ".repeat(Math.max(0, width)) + "]\u001b[2G");
    }

    public void update(int value) {
        int progress = (int)((double)value / max * width);
        if (progress > currentProgress) {
            currentProgress = progress;
            pr("=");
        }
    }

    public void finish() {
        pr("=]\n");
        try {
            System.out.flush();
        } catch (Exception e) {
            // Handle exception if needed
        }
    }
}
