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

package org.aarquelle.probenplan_pa.persistence;

import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.DataState;
import org.aarquelle.probenplan_pa.entity.Plan;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.aarquelle.probenplan_pa.persistence.FileUtils.*;

public class Load {
    private final String filename;
    private int fileVersionNumber;

    private final Map<Integer, Scene> scenes = new HashMap<>();
    private final Map<Integer, Rehearsal> rehearsals = new HashMap<>();
    private final Map<Integer, Actor> actors = new HashMap<>();
    private final Map<Integer, Role> roles = new HashMap<>();

    public Load(String filename) {
        this.filename = filename;
    }

    public void load(DataState ds) {
        ds.clear();
        try (FileInputStream x = new FileInputStream(filename)) {
            fileVersionNumber = b(x);
            int l1 = b(x);
            while(l1 > 0) {
                Scene scene = ds.createScene();
                int sceneId = l1;
                scenes.put(sceneId, scene);

                scene.setName(str(x));
                scene.setLength(f(x));
                scene.setPosition(f(x));
                l1 = b(x);
            }
            l1 = b(x);
            while(l1 > 0) {
                Rehearsal rehearsal = ds.createRehearsal();
                int rehearsalId = l1;
                rehearsals.put(rehearsalId, rehearsal);

                int year = i(x);
                int month = b(x);
                int day = b(x);
                rehearsal.setDate(LocalDate.of(year, month, day));

                rehearsal.setFullLocked(b(x) == 1);

                int l2 = b(x);
                while(l2 > 0) {
                    int sceneId = l2;
                    Scene scene = scenes.get(sceneId);
                    if (scene != null) {
                        rehearsal.addLockedScene(scene);
                        l2 = b(x);
                    } else throw new RuntimeException("Scene not found: " + sceneId);
                }
                l1 = b(x);
            }

            l1 = b(x);
            while (l1 > 0) {
                Actor actor = ds.createActor();
                int actorId = l1;
                actors.put(actorId, actor);
                actor.setName(str(x));

                int l2 = b(x);
                while (l2 > 0) {
                    int rehearsalId = l2;
                    Rehearsal rehearsal = rehearsals.get(rehearsalId);
                    if (rehearsal != null) {
                        actor.addMissingRehearsal(rehearsal);
                        l2 = b(x);
                    } else throw new RuntimeException("Rehearsal not found: " + rehearsalId);
                }

                l2 = b(x);
                while (l2 > 0) {
                    int rehearsalId = l2;
                    Rehearsal rehearsal = rehearsals.get(rehearsalId);
                    if (rehearsal != null) {
                        actor.addMaybeRehearsal(rehearsal);
                        l2 = b(x);
                    } else throw new RuntimeException("Rehearsal not found: " + rehearsalId);
                }
                l1 = b(x);
            }

            l1 = b(x);
            while (l1 > 0) {
                Role role = ds.createRole();
                int roleId = l1;
                roles.put(roleId, role);
                role.setName(str(x));
                int actorId = b(x);
                Actor actor = actors.get(actorId);
                if (actor != null) {
                    role.setActor(actor);
                } else throw new RuntimeException("Actor not found: " + actorId);
                int l2 = b(x);
                while (l2 > 0) {
                    int sceneId = l2;
                    Scene scene = scenes.get(sceneId);
                    if (scene != null) {
                        role.addBigScene(scene);
                        l2 = b(x);
                    } else throw new RuntimeException("Scene not found: " + sceneId);
                }
                l2 = b(x);
                while (l2 > 0) {
                    int sceneId = l2;
                    Scene scene = scenes.get(sceneId);
                    if (scene != null) {
                        role.addSmallScene(scene);
                        l2 = b(x);
                    } else throw new RuntimeException("Scene not found: " + sceneId);
                }
                l1 = b(x);
            }

            while (b(x) > 0) {
                b(x);// Skip PlanID, we don't need it now.
                Plan plan = new Plan();
                ds.setPlan(plan);
                int l2 = b(x);
                while(l2 > 0) {
                    int rehearsalId = l2;
                    Rehearsal rehearsal = rehearsals.get(rehearsalId);
                    if (rehearsal != null) {
                        int l3 = b(x);
                        while (l3 > 0) {
                            int sceneId = l3;
                            Scene scene = scenes.get(sceneId);
                            if (scene != null) {
                                plan.put(rehearsal, scene);
                                l3 = b(x);
                            } else throw new RuntimeException("Scene not found: " + sceneId);
                        }
                        l2 = b(x);
                    } else {
                        throw new RuntimeException("Rehearsal not found: " + rehearsalId);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
