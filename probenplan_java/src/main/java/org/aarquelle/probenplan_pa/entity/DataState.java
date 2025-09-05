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
import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.util.SortedUniqueList;

import java.util.List;

public class DataState {
    private static final DataState INSTANCE = new DataState();

    private long freshness = Long.MIN_VALUE;

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
        BasicService.stale();
        return actor;
    }

    /**
     * Removes an actor from the DataState. This can only be successful if the actor has no attached roles,
     * assign different actors to all roles beforehand.
     * @throws BusinessException Thrown if the actor still has a role.
     */
    public void removeActor(Actor a) throws BusinessException {
        if (a.getRoles().isEmpty()) {
            for (Rehearsal r : List.copyOf(a.maybeRehearsals)) {
                r.missingActors.remove(a);
                r.maybeActors.remove(a);
            }
            actors.remove(a);
            BasicService.stale();
        } else throw new BusinessException("Actor " + a.displayName() + " still has role "
                + a.getRoles().stream().toList().getFirst().displayName() + " attached."); //TODO JFC
    }

    public Rehearsal createRehearsal() {
        Rehearsal rehearsal = new Rehearsal();
        rehearsals.add(rehearsal);
        BasicService.stale();
        return rehearsal;
    }

    public void removeRehearsal(Rehearsal r) {
        for (Actor a : List.copyOf(r.maybeActors)) {
            a.removeMaybeRehearsal(r);
        }
        for (Actor a : List.copyOf(r.missingActors)) {
            a.removeMissingRehearsal(r);
        }
        for (Scene s : List.copyOf(r.lockedScenes)) {
            s.removeLockedRehearsal(r);
        }
        rehearsals.remove(r);
        BasicService.stale();
    }

    public Role createRole() {
        Role role = new Role();
        roles.add(role);
        BasicService.stale();
        return role;
    }

    public void removeRole(Role r) {
        if (r.actor != null) {
            r.actor.roles.remove(r);
        }
        for (Scene s : List.copyOf(r.getBigScenes())) {
            s.removeBigRole(r);
        }
        for (Scene s : List.copyOf(r.getSmallScenes())) {
            s.removeSmallRole(r);
        }

        roles.remove(r);
        BasicService.stale();
    }

    public Scene createScene() {
        Scene scene = new Scene();
        scenes.add(scene);
        BasicService.stale();
        return scene;
    }

    public void removeScene(Scene s) {
        for (Role r : List.copyOf(s.getBigRoles())) {
            r.removeBigScene(s);
        }
        for (Role r : List.copyOf(s.getSmallRoles())) {
            r.removeSmallScene(s);
        }
        for (Rehearsal r : List.copyOf(s.getLockedRehearsals())) {
            r.removeLockedScene(s);
        }
        scenes.remove(s);
        BasicService.stale();
    }

    public void setPlan(Plan plan) {
        BasicService.stale();
        this.plan = plan;
    }

    public Plan getPlan() {
        if (plan == null) {
            plan = new Plan();
        }
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
        BasicService.stale();
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
        BasicService.stale();
    }

    public void stale() {
        freshness++;
    }

    public long getFreshness() {
        return freshness;
    }

}
