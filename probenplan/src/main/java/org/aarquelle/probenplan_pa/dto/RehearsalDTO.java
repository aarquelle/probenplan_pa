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

package org.aarquelle.probenplan_pa.dto;

import java.sql.Date;
import java.time.LocalDate;

public class RehearsalDTO {
    private int id;
    private LocalDate date;
    private boolean locked = false;

    public Date getDate() {
        return date != null ? Date.valueOf(date) : null;
    }

    public void setDate(Date date) {
        if (date != null) {
            this.date = date.toLocalDate();
        } else {
            throw new RuntimeException("Date cannot be null");
        }
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        } else {
            return getDate().equals(((RehearsalDTO) o).getDate());
        }
    }

    @Override
    public int hashCode() {
        return getDate().hashCode();
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
