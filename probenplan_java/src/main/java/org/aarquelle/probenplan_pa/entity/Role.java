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

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class Role implements Comparable<Role>, Entity {
    String name;
    Actor actor;

    final Set<Scene> bigScenes = new HashSet<>();
    final Set<Scene> smallScenes = new HashSet<>();

    Role() {}

    public RoleSize sizeOfScene(Scene scene) {
        if (bigScenes.contains(scene)) {
            return RoleSize.BIG;
        } else if (smallScenes.contains(scene)) {
            return RoleSize.SMALL;
        } else {
            return RoleSize.NONE;
        }
    }

    public void addBigScene(Scene scene) {
        bigScenes.add(scene);
        scene.addBigRole(this);
    }

    public void removeBigScene(Scene scene) {
        bigScenes.remove(scene);
        scene.removeBigRole(this);
    }

    public void addSmallScene(Scene scene) {
        smallScenes.add(scene);
        scene.addSmallRole(this);
    }

    public void removeSmallScene(Scene scene) {
        smallScenes.remove(scene);
        scene.removeSmallRole(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Actor getActor() {
        return actor;
    }

    public void setActor(Actor actor) {
        actor.roles.add(this);
        this.actor = actor;
    }

    public Set<Scene> getBigScenes() {
        return bigScenes;
    }

    public Set<Scene> getSmallScenes() {
        return smallScenes;
    }

    @Override
    public int compareTo(@NotNull Role other) {
        if (name != null) {
            return this.name.compareTo(other.name);
        } else {
            return other.name == null ? 0 : -1;
        }
    }

    @Override
    public String displayName() {
        return name;
    }
}
