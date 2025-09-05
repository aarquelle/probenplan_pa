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

import org.aarquelle.probenplan_pa.business.BasicService;
import org.aarquelle.probenplan_pa.util.DateUtils;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class Rehearsal implements Comparable<Rehearsal>, Entity {
    LocalDate date;
    boolean fullLocked = false;
    final Set<Scene> lockedScenes = new HashSet<>();
    final Set<Actor> missingActors = new HashSet<>();

    final Set<Actor> maybeActors = new HashSet<>();

    Rehearsal(){}

    public void addLockedScene(Scene scene) {
        BasicService.stale();
        lockedScenes.add(scene);
        scene.lockedRehearsals.add(this);
    }

    public void removeLockedScene(Scene scene) {
        BasicService.stale();
        lockedScenes.remove(scene);
        scene.lockedRehearsals.remove(this);
    }

    public void addMissingActor(Actor actor) {
        BasicService.stale();
        missingActors.add(actor);
        actor.missingRehearsals.add(this);
    }

    public void removeMissingActor(Actor actor) {
        BasicService.stale();
        missingActors.remove(actor);
        actor.missingRehearsals.remove(this);
    }

    public void addMaybeActor(Actor actor) {
        BasicService.stale();
        maybeActors.add(actor);
        actor.maybeRehearsals.add(this);
    }

    public void removeMaybeActor(Actor actor) {
        BasicService.stale();
        maybeActors.remove(actor);
        actor.maybeRehearsals.remove(this);
    }

    public boolean isFullLocked() {
        return fullLocked;
    }
    public void setFullLocked(boolean fullLocked) {
        BasicService.stale();
        this.fullLocked = fullLocked;
    }

    public void setDate(LocalDate date) {
        BasicService.stale();
        this.date = date;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public int compareTo(@NotNull Rehearsal o) {
        if (date != null) {
            return date.compareTo(o.date);
        } else {
            if (o.date == null) {
                return 0;
            } else return -1;
        }
    }

    public Set<Scene> getLockedScenes() {
        return lockedScenes;
    }

    public Set<Actor> getMissingActors() {
        return missingActors;
    }

    public Set<Actor> getMaybeActors() {
        return maybeActors;
    }

    @Override
    public String displayName() {
        return DateUtils.getString(date);
    }
}
