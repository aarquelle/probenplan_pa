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

import org.aarquelle.probenplan_pa.business.BusinessException;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class CsvUtils {

    public static String[][] parseArgs(String[] args) throws BusinessException {
        String csvData;
        if (args.length == 1 && args[0].equals("!")) {
            csvData = getClipBoard();
        }
        else {
            csvData = String.join(" ", args);
        }
        if (csvData.isEmpty()) {
            return new String[0][0];
        } else if (!csvData.contains(System.lineSeparator())) {
            throw new BusinessException("Keine Zeilenumbrüche im Paste. Bist du auf Windows? Dann verwende " +
                    "\"!\" als Argument, um die kopierten Daten zu verwenden.");
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

    public static String getClipBoard(){
        try {
            String javaString = (String)Toolkit.getDefaultToolkit().getSystemClipboard().
                    getData(DataFlavor.stringFlavor);
            return javaString.replace("\n", System.lineSeparator());
        } catch (HeadlessException e) {
            throw new RuntimeException("Headless: ", e);
        } catch (UnsupportedFlavorException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
