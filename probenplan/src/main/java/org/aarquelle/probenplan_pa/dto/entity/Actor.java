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

package org.aarquelle.probenplan_pa.dto.entity;

import java.util.HashSet;
import java.util.Set;

public class Actor implements Comparable<Actor> {
    String name;
    final Set<Role> roles = new HashSet<>();
    final Set<Rehearsal> missingRehearsals = new HashSet<>();
    final Set<Rehearsal> maybeRehearsals = new HashSet<>();

    Actor(){}

    public void addRole(Role role) {
        roles.add(role);
        role.actor = this;
    }

    public void addMissingRehearsal(Rehearsal rehearsal) {
        missingRehearsals.add(rehearsal);
        rehearsal.missingActors.add(this);
    }

    public void removeMissingRehearsal(Rehearsal rehearsal) {
        missingRehearsals.remove(rehearsal);
        rehearsal.missingActors.remove(this);
    }

    public void addMaybeRehearsal(Rehearsal rehearsal) {
        maybeRehearsals.add(rehearsal);
        rehearsal.maybeActors.add(this);
    }

    public void removeMaybeRehearsal(Rehearsal rehearsal) {
        maybeRehearsals.remove(rehearsal);
        rehearsal.maybeActors.remove(this);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Set<Rehearsal> getMissingRehearsals() {
        return missingRehearsals;
    }

    public Set<Rehearsal> getMaybeRehearsals() {
        return maybeRehearsals;
    }

    @Override
    public int compareTo(Actor other) {
        return this.name.compareTo(other.name);
    }
}
