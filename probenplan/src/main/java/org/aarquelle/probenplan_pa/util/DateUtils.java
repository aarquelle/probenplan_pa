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

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

public class DateUtils {
    private static final SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy");


    public static LocalDate getLocalDate(String s) throws BusinessException{
        try {
            return new Date(format.parse(s).getTime()).toLocalDate();
        } catch (ParseException e) {
            throw new BusinessException("Invalid date format: " + s + ". Expected format: dd.MM.yyyy."
            + System.lineSeparator() + e.getMessage());
        }
    }

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

    public static String getString(LocalDate date) {
        if (date == null) {
            return "";
        }
        return format.format(Date.valueOf(date));
    }
}
