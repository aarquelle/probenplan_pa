package org.aarquelle.probenplan_pa.dto;

import org.jetbrains.annotations.NotNull;

public class ActorDTO implements Comparable<ActorDTO> {

    private int id;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        } else return getName().equals(((ActorDTO) o).getName());
    }

    @Override
    public int compareTo(@NotNull ActorDTO o) {
        return getName().compareTo(o.getName());
    }
}
