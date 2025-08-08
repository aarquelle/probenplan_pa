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

import org.aarquelle.probenplan_pa.business.Para;
import org.aarquelle.probenplan_pa.business.Params;
import org.aarquelle.probenplan_pa.entity.Actor;
import org.aarquelle.probenplan_pa.entity.DataState;
import org.aarquelle.probenplan_pa.entity.Plan;
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

public class Save {
    static final byte VERSION = 2;
    private final DataState ds;
    private final String filename;

    private final Map<Actor, Byte> actorIds = new HashMap<>();
    private final Map<Rehearsal, Byte> rehearsalIds = new HashMap<>();
    private final Map<Role, Byte> roleIds = new HashMap<>();
    private final Map<Scene, Byte> sceneIds = new HashMap<>();
    private final Map<Plan, Byte> planIds = new HashMap<>();

    private List<Byte> wl;

    public Save(DataState ds, String filename) {
        this.ds = ds;
        this.filename = filename;
    }

    /**
     * Calculate the size in bytes.
     */
    /*private int getByteLength() {
        int sizeVersion = 1;

        int sceneIds = ds.getScenes().size();
        int sceneNameLengths = ds.getScenes().size() * 2;
        int sceneLengths = ds.getScenes().size() * 4;
        int scenePositions = ds.getScenes().size() * 4;
        int sceneNames = 0;
        for (Scene s : ds.getScenes()) {
            sceneNames += s.getName().getBytes().length;
        }
        int sizeScenes = sceneIds + sceneNameLengths + sceneLengths + scenePositions + sceneNames + 1;

        int rehearsalIds = ds.getRehearsals().size();
        int years = ds.getRehearsals().size() * 4;
        int months = ds.getRehearsals().size();
        int days = ds.getRehearsals().size();
        int fullLocked = ds.getRehearsals().size();
        int lockedIds = 1;
        for (Rehearsal r : ds.getRehearsals()) {
            lockedIds += r.getLockedScenes().size();
        }
        int sizeRehearsals = rehearsalIds + years + months + days + fullLocked + lockedIds + 1;

        int actorIds = ds.getActors().size();
        int actorNameLengths = ds.getActors().size() * 2;
        int actorNames = 0;
        int missingIds = 1;
        int maybeIds = 1;
        for (Actor a : ds.getActors()) {
            actorNames += a.getName().getBytes().length;
            missingIds += a.getMissingRehearsals().size();
            maybeIds += a.getMaybeRehearsals().size();
        }

        int sizeActors = actorIds + actorNameLengths + actorNames + missingIds + maybeIds + 1;

        int roleIds = ds.getRoles().size();
        int roleNameLengths = ds.getRoles().size() * 2;
        int roleActorIds = ds.getRoles().size() * 2;
        int roleNames = 0;
        int bigSceneIds = 1;
        int smallSceneIds = 1;
        for (Role r : ds.getRoles()) {
            roleNames += r.getName().getBytes().length;
            bigSceneIds += r.getBigScenes().size();
            smallSceneIds += r.getSmallScenes().size();
        }
        int sizeRoles = roleIds + roleNameLengths + roleActorIds + roleNames + bigSceneIds + smallSceneIds + 1;
    }*/
    public void saveFile() {
        assignIds(ds);
        wl = new ArrayList<>();
        try (FileOutputStream fos = new FileOutputStream(filename)) {
            wl.add(VERSION);

            scenes();
            rehearsals();
            actors();
            roles();
            plan();
            params();


            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            for (Byte b : wl) {
                byteArrayOutputStream.write(b);
            }
            fos.write(byteArrayOutputStream.toByteArray());
            fos.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void b(byte b) {
        wl.add(b);
    }

    private void b(int i) {
        if (i <= 0xFF) {
            wl.add((byte) i);
        } else {
            throw new IllegalArgumentException(i + "cannot be saved as byte");
        }
    }

    private void b(boolean b) {
        wl.add(b ? (byte) 1 : (byte) 0);
    }

    private void s(short s) {
        wl.add((byte) (s >> 8));
        wl.add((byte) s);
    }

    private void i(int i) {
        wl.add((byte) (i >> 24));
        wl.add((byte) (i >> 16));
        wl.add((byte) (i >> 8));
        wl.add((byte) i);
    }

    private void f(float f) {
        i(Float.floatToIntBits(f));
    }

    private void f(double d) {
        f((float) d);
    }

    private void f(Double d) {
        f(d.floatValue());
    }

    private void str(String s) {
        s((short) s.getBytes().length);
        for (byte b : s.getBytes()) {
            wl.add(b);
        }
    }

    private void l(long l) {
        wl.add((byte) (l >> 56));
        wl.add((byte) (l >> 48));
        wl.add((byte) (l >> 40));
        wl.add((byte) (l >> 32));
        wl.add((byte) (l >> 24));
        wl.add((byte) (l >> 16));
        wl.add((byte) (l >> 8));
        wl.add((byte) l);
    }

    private <T> void coll(Collection<T> coll) {
        for (T e : coll) {
            b(getIdForEntity(e));
        }
        b(0);
    }

    private void scenes() {
        for (Scene s : ds.getScenes()) {
            b(getIdForEntity(s));
            str(s.getName());
            f(s.getLength());
            f(s.getPosition());
        }
        b(0);
    }

    private void rehearsals() {
        for (Rehearsal r : ds.getRehearsals()) {
            b(getIdForEntity(r));
            int year = r.getDate().getYear();
            byte month = (byte) r.getDate().getMonthValue();
            byte day = (byte) r.getDate().getDayOfMonth();
            i(year);
            b(month);
            b(day);
            b(r.isFullLocked());
            coll(r.getLockedScenes());
        }
        b(0);
    }

    private void actors() {
        for (Actor a : ds.getActors()) {
            b(getIdForEntity(a));
            str(a.getName());
            coll(a.getMissingRehearsals());
            coll(a.getMaybeRehearsals());
        }
        b(0);
    }

    private void roles() {
        for (Role r : ds.getRoles()) {
            b(getIdForEntity(r));
            str(r.getName());
            b(getIdForEntity(r.getActor()));
            coll(r.getBigScenes());
            coll(r.getSmallScenes());
        }
        b(0);
    }

    private void plan() {
        Plan p = ds.getPlan();
        if (p != null) {
            b(1); // Plan is present
            for (Rehearsal r : p.getRehearsals()) {
                b(getIdForEntity(r));
                coll(p.get(r));
            }
            b(0);
        }
        b(0); //Terminator in case of future arrays.
    }

    private void params() {
        for (Para<?> p : Params.getAllParams()) {
            if (!p.getValue().equals(p.getDefaultValue())) {
                b(1);
                str(p.getName());
                if (p.getValue() instanceof Float f) {
                    f(f);
                }else if (p.getValue() instanceof Double d) {
                    f(d);
                } else if (p.getValue() instanceof Integer i) {
                    i(i);
                } else if (p.getValue() instanceof Long l) {
                    l(l);
                }
            }
        }
        b(0);
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
        planIds.put(state.getPlan(), (byte) 1);
    }
}
