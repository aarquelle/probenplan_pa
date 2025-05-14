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

import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Rehearsal implements Comparable<Rehearsal> {
    LocalDate date;
    boolean fullLocked = false;
    final List<Scene> lockedScenes = new ArrayList<>();
    final List<Actor> missingActors = new ArrayList<>();

    final List<Actor> maybeActors = new ArrayList<>();

    Rehearsal(){}

    public void addLockedScene(Scene scene) {
        lockedScenes.add(scene);
        scene.lockedRehearsals.add(this);
    }

    public void removeLockedScene(Scene scene) {
        lockedScenes.remove(scene);
        scene.lockedRehearsals.remove(this);
    }

    public void addMissingActor(Actor actor) {
        missingActors.add(actor);
        actor.missingRehearsals.add(this);
    }

    public void removeMissingActor(Actor actor) {
        missingActors.remove(actor);
        actor.missingRehearsals.remove(this);
    }

    public void addMaybeActor(Actor actor) {
        maybeActors.add(actor);
        actor.maybeRehearsals.add(this);
    }

    public void removeMaybeActor(Actor actor) {
        maybeActors.remove(actor);
        actor.maybeRehearsals.remove(this);
    }

    public boolean isFullLocked() {
        return fullLocked;
    }
    public void setFullLocked(boolean fullLocked) {
        this.fullLocked = fullLocked;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public int compareTo(@NotNull Rehearsal o) {
        return date.compareTo(o.date);
    }

    public List<Scene> getLockedScenes() {
        return lockedScenes;
    }

    public List<Actor> getMissingActors() {
        return missingActors;
    }

    public List<Actor> getMaybeActors() {
        return maybeActors;
    }
}
