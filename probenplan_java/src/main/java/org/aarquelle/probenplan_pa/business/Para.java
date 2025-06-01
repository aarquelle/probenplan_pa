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

    void reset() {
        this.value = defaultValue;
    }

    @Override
    public int compareTo(@NotNull Para<T> o) {
        return name.compareTo(o.name);
    }
}
