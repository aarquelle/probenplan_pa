package org.aarquelle.probenplan_pa.dto;

import java.sql.Date;
import java.time.LocalDate;

public class RehearsalDTO {
    private int id;
    private LocalDate date;

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
}
