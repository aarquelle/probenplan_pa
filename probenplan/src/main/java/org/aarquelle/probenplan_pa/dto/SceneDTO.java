package org.aarquelle.probenplan_pa.dto;

import org.jetbrains.annotations.NotNull;

public class SceneDTO implements Comparable<SceneDTO> {
    private int id;
    private String name;
    private double length;
    private double position;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLength() {
        return length;
    }

    public void setLength(double length) {
        this.length = length;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
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
        } else return getName().equals(((SceneDTO) o).getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    @Override
    public int compareTo(@NotNull SceneDTO o) {
        return Double.compare(this.getPosition(), o.getPosition());
    }
}
