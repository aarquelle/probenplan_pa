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

package org.aarquelle.probenplan_pa.util;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class CsvUtils {

    public static String[][] parseArgs(String[] args) {
        String csvData = String.join(" ", args);
        if (csvData.isEmpty()) {
            return new String[0][0];
        }
        String[] lines = csvData.split(System.lineSeparator());
        String[][] data = new String[lines.length][];
        for (int i = 0; i < lines.length; i++) {
            data[i] = lines[i].split("\t");
            for (int j = 0; j < data[i].length; j++) {
                data[i][j] = data[i][j].trim();
            }
        }

        return data;
    }

    public static String tableToCsv(String[][] table) {
        StringBuilder csvBuilder = new StringBuilder();
        for (String[] row : table) {
            for (int i = 0; i < row.length; i++) {
                csvBuilder.append(row[i]);
                if (i < row.length - 1) {
                    csvBuilder.append("\t");
                }
            }
            csvBuilder.append(System.lineSeparator());
        }
        return csvBuilder.toString();
    }

    public static void copyTextToClipboard(String s) {
        StringSelection stringSelection = new StringSelection(s);
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(stringSelection, null);
    }
}
