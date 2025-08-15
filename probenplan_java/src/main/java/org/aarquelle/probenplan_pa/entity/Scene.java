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

public class Scene implements Comparable<Scene>, Entity {
    String name;
    double length;
    double position;

    final Set<Role> bigRoles = new HashSet<>();
    final Set<Role> smallRoles = new HashSet<>();
    final Set<Rehearsal> lockedRehearsals = new HashSet<>();

    Scene(){}

    public RoleSize sizeOfRole(Role role) {
        if (bigRoles.contains(role)) {
            return RoleSize.BIG;
        } else if (smallRoles.contains(role)) {
            return RoleSize.SMALL;
        } else {
            return RoleSize.NONE;
        }
    }

    public void addBigRole(Role role) {
        bigRoles.add(role);
        role.bigScenes.add(this);
    }

    public void removeBigRole(Role role) {
        bigRoles.remove(role);
        role.bigScenes.remove(this);
    }

    public void addSmallRole(Role role) {
        smallRoles.add(role);
        role.smallScenes.add(this);
    }

    public void removeSmallRole(Role role) {
        smallRoles.remove(role);
        role.smallScenes.remove(this);
    }

    public void addLockedRehearsal(Rehearsal rehearsal) {
        lockedRehearsals.add(rehearsal);
        rehearsal.lockedScenes.add(this);
    }

    public void removeLockedRehearsal(Rehearsal rehearsal) {
        lockedRehearsals.remove(rehearsal);
        rehearsal.lockedScenes.remove(this);
    }

    public Set<Role> getBigRoles() {
        return bigRoles;
    }

    public Set<Role> getSmallRoles() {
        return smallRoles;
    }

    public Set<Rehearsal> getLockedRehearsals() {
        return lockedRehearsals;
    }

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

    @Override
    public int compareTo(@NotNull Scene o) {
        return Double.compare(position, o.position);
    }

    @Override
    public String displayName() {
        return name;
    }
}
