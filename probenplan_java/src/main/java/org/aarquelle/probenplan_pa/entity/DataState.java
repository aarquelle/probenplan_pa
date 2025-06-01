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

package org.aarquelle.probenplan_pa.entity;

import org.aarquelle.probenplan_pa.business.Params;

import java.util.HashSet;
import java.util.Set;

public class DataState {
    private static final DataState INSTANCE = new DataState();

    private final Set<Actor> actors = new HashSet<>();
    private final Set<Rehearsal> rehearsals = new HashSet<>();
    private final Set<Role> roles = new HashSet<>();
    private final Set<Scene> scenes = new HashSet<>();
    private Plan plan;

    private DataState() {
        // Private constructor to prevent instantiation
    }

    public static DataState getInstance() {
        return INSTANCE;
    }

    public void clear() {
        actors.clear();
        rehearsals.clear();
        roles.clear();
        scenes.clear();
        plan = null;
    }

    public Actor createActor() {
        Actor actor = new Actor();
        actors.add(actor);
        return actor;
    }

    public Rehearsal createRehearsal() {
        Rehearsal rehearsal = new Rehearsal();
        rehearsals.add(rehearsal);
        return rehearsal;
    }

    public Role createRole() {
        Role role = new Role();
        roles.add(role);
        return role;
    }

    public Scene createScene() {
        Scene scene = new Scene();
        scenes.add(scene);
        return scene;
    }

    public void setPlan(Plan plan) {
        this.plan = plan;
    }

    public Plan getPlan() {
        return plan;
    }

    public Set<Actor> getActors() {
        return actors;
    }

    public Set<Rehearsal> getRehearsals() {
        return rehearsals;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Set<Scene> getScenes() {
        return scenes;
    }
}
