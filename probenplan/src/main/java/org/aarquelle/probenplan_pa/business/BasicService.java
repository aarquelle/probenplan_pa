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

package org.aarquelle.probenplan_pa.business;

import org.aarquelle.probenplan_pa.data.dao.Transaction;
import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.RoleDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;
import org.aarquelle.probenplan_pa.util.Pair;

import java.util.List;
import java.util.Map;

public class BasicService {
    public static List<RehearsalDTO> getRehearsals() {
        try (Transaction t = new Transaction()) {
            return t.getReadDAO().getRehearsals();
        }
    }

    public static List<SceneDTO> getScenes() {
        try (Transaction t = new Transaction()) {
            return t.getReadDAO().getScenes();
        }
    }

    public static List<ActorDTO> getActors() {
        try (Transaction t = new Transaction()) {
            return t.getReadDAO().getActors();
        }
    }

    public static List<RoleDTO> getRoles() {
        try (Transaction t = new Transaction()) {
            return t.getReadDAO().getRoles();
        }
    }

    public static List<Pair<RehearsalDTO, SceneDTO>> getLockedScenes() {
        try (Transaction t = new Transaction()) {
            return t.getReadDAO().getLockedScenes();
        }
    }
}
