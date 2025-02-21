package org.aarquelle.probenplan_pa.dto;

public class RoleDTO {
    private int id;
    private String name;
    private ActorDTO actor;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ActorDTO getActor() {
        return actor;
    }

    public void setActor(ActorDTO actor) {
        this.actor = actor;
    }

    @Override
    public boolean equals(Object o) {
        if (o.getClass() != this.getClass()) {
            return false;
        } else return getName().equals(((RoleDTO) o).getName());
    }
}
