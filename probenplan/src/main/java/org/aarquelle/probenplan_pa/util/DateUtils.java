package org.aarquelle.probenplan_pa.util;

import java.sql.Date;
import java.text.SimpleDateFormat;

public class DateUtils {
    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");


    public static Date getDate(String s) {
        if (s == null || s.isEmpty()) {
            return null;
        }
        try {
            return new Date(format.parse(s).getTime());
        } catch (Exception e) {
            throw new RuntimeException("Invalid date format: " + s, e);
        }
    }
}
