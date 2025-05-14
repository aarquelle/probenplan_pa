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
import org.aarquelle.probenplan_pa.entity.Rehearsal;
import org.aarquelle.probenplan_pa.entity.Role;
import org.aarquelle.probenplan_pa.entity.Scene;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class FileIO {

    private final Map<Actor, Byte> actorIds = new HashMap<>();
    private final Map<Rehearsal, Byte> rehearsalIds = new HashMap<>();
    private final Map<Role, Byte> roleIds = new HashMap<>();
    private final Map<Scene, Byte> sceneIds = new HashMap<>();

    public <T> byte getIdForEntity(T entity) {
        return switch (entity) {
            case Actor ignored -> actorIds.get(entity);
            case Rehearsal ignored -> rehearsalIds.get(entity);
            case Role ignored -> roleIds.get(entity);
            case Scene ignored -> sceneIds.get(entity);
            default -> throw new IllegalArgumentException("Unknown entity type: " + entity.getClass());
        };

    }

    private void assignIds(DataState state) {
        actorIds.clear();
        rehearsalIds.clear();
        roleIds.clear();
        sceneIds.clear();
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
    }

    public void writeToFile(String fileName, byte[] data) throws IOException {
        try(FileOutputStream fos = new FileOutputStream(fileName)) {
            fos.write(data);
        }
    }

    public byte[] readFromFile(String fileName) throws IOException {
        try (FileInputStream fis = new FileInputStream(fileName)) {
            byte[] data = new byte[fis.available()];
            if (fis.read(data) == -1) {;
                throw new IOException("Unexpectedly reached end of file: " + fileName);
            }
            return data;
        }
    }
}
