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
