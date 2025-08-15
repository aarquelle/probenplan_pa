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

import org.aarquelle.probenplan_pa.util.SortedUniqueList;

import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

public class DataState {
    private static final DataState INSTANCE = new DataState();

    private final SortedUniqueList<Actor> actors = new SortedUniqueList<>();
    private final SortedUniqueList<Rehearsal> rehearsals = new SortedUniqueList<>();
    private final SortedUniqueList<Role> roles = new SortedUniqueList<>();
    private final SortedUniqueList<Scene> scenes = new SortedUniqueList<>();
    private Plan plan;

    private final SortedUniqueList<Scene> enforcedScenes = new SortedUniqueList<>();

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

    public SortedUniqueList<Actor> getActors() {
        return actors;
    }

    public SortedUniqueList<Rehearsal> getRehearsals() {
        return rehearsals;
    }

    public SortedUniqueList<Role> getRoles() {
        return roles;
    }

    public SortedUniqueList<Scene> getScenes() {
        return scenes;
    }

    public void enforceScene(Scene s) {
        enforcedScenes.add(s);
    }

    public SortedUniqueList<Scene> getEnforcedScenes() {
        return enforcedScenes;
    }

    public void sort() {
        actors.sort();
        roles.sort();
        rehearsals.sort();
        scenes.sort();
    }

}
