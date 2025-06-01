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

import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.DataState;
import org.aarquelle.probenplan_pa.entity.Plan;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;

import java.time.LocalDate;
import java.util.Set;

public class BasicService {
    static DataState ds = DataState.getInstance();

    public static Set<Rehearsal> getRehearsals() {
        return ds.getRehearsals();
    }

    public static Set<Scene> getScenes() {
        return ds.getScenes();
    }

    public static Set<Actor> getActors() {
        return ds.getActors();
    }

    public static Set<Role> getRoles() {
        return ds.getRoles();
    }

    public static Role createRole() {
        return ds.createRole();
    }

    public static Actor createActor() {
        return ds.createActor();
    }

    public static Rehearsal createRehearsal() {
        return ds.createRehearsal();
    }

    public static Scene createScene() {
        return ds.createScene();
    }

    public static Role getRoleByName(String name) {
        return ds.getRoles().stream()
                .filter(role -> role.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public static Actor getActorByName(String name) {
        return ds.getActors().stream()
                .filter(actor -> actor.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public static Rehearsal getRehearsalByDate(LocalDate date) {
        return ds.getRehearsals().stream()
                .filter(rehearsal -> rehearsal.getDate().equals(date))
                .findFirst()
                .orElse(null);
    }

    public static Scene getSceneByName(String name) {
        return ds.getScenes().stream()
                .filter(scene -> scene.getName().equals(name))
                .findFirst()
                .orElse(null);
    }

    public static void clearData() {
        ds.clear();
    }

    public static void setPlan(Plan plan) {
        ds.setPlan(plan);
    }

    public static Plan getPlan() {
        return ds.getPlan();
    }
}
