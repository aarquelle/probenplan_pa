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

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.aarquelle.probenplan_pa.persistence.FileUtils.*;

public class Save {
    static final byte VERSION = 1;
    private final DataState ds;
    private final String filename;

    private final Map<Actor, Byte> actorIds = new HashMap<>();
    private final Map<Rehearsal, Byte> rehearsalIds = new HashMap<>();
    private final Map<Role, Byte> roleIds = new HashMap<>();
    private final Map<Scene, Byte> sceneIds = new HashMap<>();
    private final Map<Plan, Byte> planIds = new HashMap<>();

    public Save(DataState ds, String filename) {
        this.ds = ds;
        this.filename = filename;
    }

    public void saveFile() {
        assignIds(ds);
        try(FileOutputStream fos = new FileOutputStream(filename)) {
            fos.write(VERSION);
            for (Scene s : ds.getScenes()) {
                fos.write(getIdForEntity(s));
                byte[] name = s.getName().getBytes();
                short nameLength = (short) name.length;
                fos.write(shortToTwoByte(nameLength));
                fos.write(name);
                fos.write(floatToFourBytes((float)s.getLength()));
                fos.write(floatToFourBytes((float) s.getPosition()));
            }
            fos.write(0);

            for (Rehearsal r : ds.getRehearsals()) {
                fos.write(getIdForEntity(r));
                int year = r.getDate().getYear();
                byte month = (byte) r.getDate().getMonthValue();
                byte day = (byte) r.getDate().getDayOfMonth();
                fos.write(intToFourBytes(year));
                fos.write(month);
                fos.write(day);
                fos.write(r.isFullLocked() ? 1 : 0);
                for (Scene s : r.getLockedScenes()) {
                    fos.write(getIdForEntity(s));
                }
                fos.write(0);
            }
            fos.write(0);

            for (Actor a : ds.getActors()) {
                fos.write(getIdForEntity(a));
                byte[] name = a.getName().getBytes();
                short nameLength = (short) name.length;
                fos.write(shortToTwoByte(nameLength));
                fos.write(name);
                for (Rehearsal r : a.getMissingRehearsals()) {
                    fos.write(getIdForEntity(r));
                }
                fos.write(0);
                for (Rehearsal r : a.getMaybeRehearsals()) {
                    fos.write(getIdForEntity(r));
                }
                fos.write(0);
            }
            fos.write(0);

            for (Role r : ds.getRoles()) {
                fos.write(getIdForEntity(r));
                byte[] name = r.getName().getBytes();
                short nameLength = (short) name.length;
                fos.write(shortToTwoByte(nameLength));
                fos.write(name);
                fos.write(getIdForEntity(r.getActor()));
                for (Scene s : r.getBigScenes()) {
                    fos.write(getIdForEntity(s));
                }
                fos.write(0);
                for (Scene s : r.getSmallScenes()) {
                    fos.write(getIdForEntity(s));
                }
                fos.write(0);
            }
            fos.write(0);

            Plan p = ds.getPlan();
            if (p != null) {
                fos.write(1); // Plan is present
                for (Rehearsal r : p.getRehearsals()) {
                    fos.write(getIdForEntity(r));
                    for (Scene s : p.get(r)) {
                        fos.write(getIdForEntity(s));
                    }
                    fos.write(0);
                }
                fos.write(0);
            }
            fos.write(0); //Terminator in case of future arrays.
            fos.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private <T> byte getIdForEntity(T entity) {
        return switch (entity) {
            case Actor ignored -> actorIds.get(entity);
            case Rehearsal ignored -> rehearsalIds.get(entity);
            case Role ignored -> roleIds.get(entity);
            case Scene ignored -> sceneIds.get(entity);
            case Plan ignored -> planIds.get(entity);
            default -> throw new IllegalArgumentException("Unknown entity type: " + entity.getClass());
        };

    }

    private void assignIds(DataState state) {
        actorIds.clear();
        rehearsalIds.clear();
        roleIds.clear();
        sceneIds.clear();
        planIds.clear();
        byte id = 1;
        for (Actor actor : state.getActors()) {
            actorIds.put(actor, id++);
        }
        id = 1;
        for (Rehearsal rehearsal : state.getRehearsals()) {
            rehearsalIds.put(rehearsal, id++);
        }
        id = 1;
        for (Role role : state.getRoles()) {
            roleIds.put(role, id++);
        }
        id = 1;
        for (Scene scene : state.getScenes()) {
            sceneIds.put(scene, id++);
        }
        planIds.put(state.getPlan(), (byte)1);
    }
}
