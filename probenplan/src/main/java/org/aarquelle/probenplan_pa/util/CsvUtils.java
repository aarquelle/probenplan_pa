package org.aarquelle.probenplan_pa.util;

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
}
