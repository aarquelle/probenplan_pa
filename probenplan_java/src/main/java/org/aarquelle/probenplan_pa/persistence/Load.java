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

import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.business.Params;
import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.DataState;
import org.aarquelle.probenplan_pa.entity.Plan;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class Load {
    private final String filename;

    private final Map<Integer, Scene> scenes = new HashMap<>();
    private final Map<Integer, Rehearsal> rehearsals = new HashMap<>();
    private final Map<Integer, Actor> actors = new HashMap<>();
    private final Map<Integer, Role> roles = new HashMap<>();

    private DataState ds;
    int fileVersionNumber;

    byte[] input;
    int pointer;

    public Load(String filename) {
        this.filename = filename;
    }

    public void load(DataState ds) {
        this.ds = ds;
        ds.clear();

        Path path = Paths.get(filename);
        long size;
        try {
            size = Files.size(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (size > Integer.MAX_VALUE) {
            throw new IllegalStateException("File is too large!");
        }
        input = new byte[(int) size];
        pointer = 0;

        try (FileInputStream x = new FileInputStream(filename)) {
            if (x.read(input) == -1) {
                throw new RuntimeException("Unexpected end of file.");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fileVersionNumber = b();
        scenes();
        rehearsals();
        actors();
        roles();
        plan();
        params();

        ds.sort();
    }

    private void scenes() {
        while (true) {
            int id = uByte();
            if (id == 0) {
                break;
            }
            Scene scene = ds.createScene();
            scenes.put(id, scene);
            scene.setName(str());
            scene.setLength(f());
            scene.setPosition(f());
        }
    }

    private void rehearsals() {
        while (true) {
            int id = uByte();
            if (id == 0) {
                break;
            }
            Rehearsal rehearsal = ds.createRehearsal();
            rehearsals.put(id, rehearsal);
            int year = i();
            byte month = b();
            byte day = b();
            rehearsal.setDate(LocalDate.of(year, month, day));
            rehearsal.setFullLocked(bool());
            while (true) {
                int lockId = uByte();
                if (lockId == 0) {
                    break;
                } else {
                    Scene scene = scenes.get(lockId);
                    if (scene != null) {
                        rehearsal.addLockedScene(scene);
                    } else {
                        throw new RuntimeException("Scene with id " + lockId + " not found as lock" +
                                " for rehearsal " + rehearsal.getDate());
                    }
                }
            }
        }
    }

    private void actors() {
        while (true) {
            int id = uByte();
            if (id == 0) {
                break;
            }
            Actor actor = ds.createActor();
            actors.put(id, actor);
            actor.setName(str());
            while (true) {
                int missingId = uByte();
                if (missingId == 0) {
                    break;
                }
                Rehearsal miss = rehearsals.get(missingId);
                if (miss != null) {
                    actor.addMissingRehearsal(miss);
                } else {
                    throw new RuntimeException("No missing rehearsal with id " + missingId
                            + " for actor " + actor.getName());
                }
            }
            while (true) {
                int maybeId = uByte();
                if (maybeId == 0) {
                    break;
                }
                Rehearsal miss = rehearsals.get(maybeId);
                if (miss != null) {
                    actor.addMaybeRehearsal(miss);
                } else {
                    throw new RuntimeException("No maybe rehearsal with id " + maybeId
                            + " for actor " + actor.getName());
                }
            }
        }
    }

    public void roles() {
        while (true) {
            int id = uByte();
            if (id == 0) {
                break;
            }
            Role role = ds.createRole();
            roles.put(id, role);
            role.setName(str());
            int actorId = uByte();
            Actor actor = actors.get(actorId);
            if (actor != null) {
                role.setActor(actor);
            } else {
                throw new RuntimeException("No actor with id " + actorId + " for role " + role.getName());
            }

            while (true) {
                int bigId = uByte();
                if (bigId == 0) {
                    break;
                }
                Scene big = scenes.get(bigId);
                if (big != null) {
                    role.addBigScene(big);
                } else {
                    throw new RuntimeException("No big scene with id " + bigId
                            + " for role " + role.getName());
                }
            }
            while (true) {
                int smallId = uByte();
                if (smallId == 0) {
                    break;
                }
                Scene small = scenes.get(smallId);
                if (small != null) {
                    role.addSmallScene(small);
                } else {
                    throw new RuntimeException("No small scene with id " + smallId
                            + " for role " + role.getName());
                }
            }
        }
    }

    private void plan() {
        if (bool()) {
            Plan plan = new Plan();
            ds.setPlan(plan);
            while (true) {
                int rehearsalId = uByte();
                if (rehearsalId == 0) {
                    break;
                }
                Rehearsal rehearsal = rehearsals.get(rehearsalId);
                if (rehearsal == null) {
                    throw new RuntimeException("No rehearsal " + rehearsalId + " for plan.");
                }
                while (true) {
                    int sceneId = uByte();
                    if (sceneId == 0) {
                        break;
                    }
                    Scene scene = scenes.get(sceneId);
                    if (scene == null) {
                        throw new RuntimeException("No scene " + sceneId + " for rehearsal "
                                + rehearsal + " in plan.");
                    }
                    plan.put(rehearsal, scene);
                }
            }
            pointer++; //Ignoring the trailing 0
        }
    }

    public void params() {
        while(bool()) {
            String name = str();
            Number v = Params.getValue(name);
            try {
                if (v instanceof Float || v instanceof Double) {
                    Params.setPara(name, String.valueOf(f()));
                } else if (v instanceof Integer) {
                    Params.setPara(name, String.valueOf(i()));
                } else if (v instanceof Long l) {
                    Params.setPara(name, String.valueOf(l()));
                } else {
                    throw new RuntimeException("Unknown parameter type: " + v.getClass());
                }
            } catch (BusinessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private int uByte() {
        return input[pointer++] & 0xFF;
    }

    private byte b() {
        return input[pointer++];
    }

    private short s() {
        int a = uByte() << 8;
        int b = uByte();
        return (short) (a | b);
    }

    private int i() {
        int a = uByte() << 24;
        int b = uByte() << 16;
        int c = uByte() << 8;
        int d = uByte();
        return a | b | c | d;
    }

    private long l() {
        long a = (long) uByte() << 56;
        long b = (long) uByte() << 48;
        long c = (long) uByte() << 40;
        long d = (long) uByte() << 32;
        int e = uByte() << 24;
        int f = uByte() << 16;
        int g = uByte() << 8;
        int h = uByte();
        return a | b | c | d | e | f | g | h;
    }

    private float f() {
        return Float.intBitsToFloat(i());
    }

    private String str() {
        short length = s();
        byte[] bytes = new byte[length];
        for (int i = 0; i < length; i++) {
            bytes[i] = b();
        }
        return new String(bytes);
    }

    private boolean bool() {
        return b() != 0;
    }
}
