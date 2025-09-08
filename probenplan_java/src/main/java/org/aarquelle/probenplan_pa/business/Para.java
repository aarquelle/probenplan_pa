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

package org.aarquelle.probenplan_pa.business;

import org.jetbrains.annotations.NotNull;

public class Para<T extends Number> implements Comparable<Para<T>> {
    String name;
    String description;
    T defaultValue;
    T minValue;
    T maxValue;
    T value;

    Para(String name, T defaultValue) {
        this.name = name;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
        this.minValue = null;
        this.maxValue = null;
        this.description = "";
    }

    static Para<Integer> createBoolPara(String name, boolean defaultValue) {
        Para<Integer> p = new Para<>(name, defaultValue ? 1 : 0);
        p.minValue = 0;
        p.maxValue = 1;
        return p;
    }

    public boolean isBoolean() {
        return minValue != null && minValue.equals(0) && maxValue != null && maxValue.equals(1);
    }

    public boolean getBoolValue() {
        if (isBoolean()) {
            return value.equals(1);
        } else throw new RuntimeException("Param " + name + " is not a boolean!");
    }

    @SuppressWarnings("unchecked")
    public void setBoolean(boolean b) {
        if (defaultValue instanceof Integer) {
            setValue((T) (b ? Integer.valueOf(1) : Integer.valueOf(0)));
        }
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public T getDefaultValue() {
        return defaultValue;
    }

    public T getValue() {
        return value;
    }

    @SuppressWarnings("unchecked")
    void setValue(String s) throws BusinessException {
        if (s == null || s.isEmpty()) {
            throw new BusinessException("Parameter " + name + " must be of type "
                    + defaultValue.getClass().getSimpleName());
        }

        if (defaultValue instanceof Integer) {
            setValue((T) Integer.valueOf(s));
        }
        if (defaultValue instanceof Double) {
            setValue((T) Double.valueOf(s));
        }
        if (defaultValue instanceof Long) {
            setValue((T) Long.valueOf(s));
        }
    }

    void setValue(T t) throws BusinessException {
        if (t == null) {
            throw new BusinessException("Parameter " + name + " must be of type "
                    + defaultValue.getClass().getSimpleName());
        }

        if (minValue != null && t.doubleValue() < minValue.doubleValue()) {
            throw new BusinessException("Parameter " + name + " must be >= " + minValue);
        }

        if (maxValue != null && t.doubleValue() > maxValue.doubleValue()) {
            throw new BusinessException("Parameter " + name + " must be <= " + maxValue);
        }

        value = t;
    }

    /**
     * Compares two values. Returns -1 if x < y, 0 if they are equal, and 1 and x > y.
     * For numbers, it compares the double values. For booleans, it returns 0 as long as both values are booleans.
     * For objects implementing Comparable, it returns that result.
     * @throws IllegalArgumentException Thrown if the values cannot be compared.
     */
    /*public int compareValue(T x, T y) throws IllegalArgumentException {
        if (x instanceof Number a) {
            if (y instanceof Number b) {
                if (a.equals(b)) {
                    return 0;
                } else {
                    return a.doubleValue() > b.doubleValue() ? 1 : -1;
                }
            }
            throw new IllegalArgumentException("x is a number, y isn't");
        } else if (x instanceof Boolean){
            if (y instanceof Boolean) {
                return 0;
            } else {
                throw new IllegalArgumentException("x is a boolean, y isn't");
            }
        } else throw new IllegalArgumentException("Undefined relationship between " + x + " and " + y);
    }*/

    void reset() {
        this.value = defaultValue;
    }

    @Override
    public int compareTo(@NotNull Para<T> o) {
        return name.compareTo(o.name);
    }
}
