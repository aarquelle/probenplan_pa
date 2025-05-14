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

import org.aarquelle.probenplan_pa.dto.entity.*;
import org.aarquelle.probenplan_pa.util.Pair;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

    public static List<Pair<Rehearsal, Scene>> getLockedScenes() {
        List<Pair<Rehearsal, Scene>> lockedScenes = new ArrayList<>();
        for (Rehearsal r : ds.getRehearsals()) {
            for (Scene s : r.getLockedScenes()) {
                lockedScenes.add(new Pair<>(r, s));
            }
        }
        return lockedScenes;
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

    public static boolean roleNameExists(String name) {
        for (Role role : ds.getRoles()) {
            if (role.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean actorNameExists(String name) {
        for (Actor actor : ds.getActors()) {
            if (actor.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public static boolean rehearsalDateExists(LocalDate date) {
        for (Rehearsal rehearsal : ds.getRehearsals()) {
            if (rehearsal.getDate().equals(date)) {
                return true;
            }
        }
        return false;
    }

    public static boolean sceneNameExists(String name) {
        for (Scene scene : ds.getScenes()) {
            if (scene.getName().equals(name)) {
                return true;
            }
        }
        return false;
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
}
