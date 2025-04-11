package org.aarquelle.probenplan_pa.util;

import org.aarquelle.probenplan_pa.business.BusinessException;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateUtils {
    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");


    public static Date getDate(String s) throws BusinessException {
        if (s == null || s.isEmpty()) {
            return null;
        }
        try {
            return new Date(format.parse(s).getTime());
        } catch (ParseException e) {
            throw new BusinessException("Invalid date format: " + s + ". Expected format: dd.MM.yyyy."
            + System.lineSeparator() + e.getMessage());
        }
    }

    public static String getString(Date date) {
        if (date == null) {
            return "";
        }
        return format.format(date);
    }
}
