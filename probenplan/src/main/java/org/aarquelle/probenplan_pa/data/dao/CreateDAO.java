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

package org.aarquelle.probenplan_pa.data.dao;

import org.aarquelle.probenplan_pa.data.exception.DuplicateException;
import org.aarquelle.probenplan_pa.data.exception.RequiredValueMissingException;
import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.RoleDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CreateDAO extends AbstractDAO {
    public CreateDAO(Connection conn) {
        super(conn);
    }

    public void createActor(ActorDTO actor) throws DuplicateException {
        String sql = "insert into actors (actor_name) values (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, actor.getName());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                actor.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateException("Actor names have to be unique!", e);
            }
        }
    }

    public void createRole(RoleDTO role) throws DuplicateException {
        String sql = "insert into roles (role_name, actor_id) values (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, role.getName());
            if (role.getActor() != null) {
                stmt.setInt(2, role.getActor().getId());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                role.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateException("Role names have to be unique!", e);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void updateRole(@NotNull RoleDTO role) throws RequiredValueMissingException {
        String sql = "update roles set actor_id = ? where role_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            if (role.getId() == 0 || role.getActor() == null || role.getActor().getId() == 0) {
                throw new RequiredValueMissingException("ID, Actor and Actor.ID must be set!");
            }
            stmt.setInt(1, role.getActor().getId());
            stmt.setInt(2, role.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createScene(SceneDTO scene) throws DuplicateException, RequiredValueMissingException {
        String sql = "insert into scenes (scene_name, length, position) values (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, scene.getName());
            stmt.setDouble(2, scene.getLength());
            stmt.setDouble(3, scene.getPosition());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                scene.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateException("Cannot create scene " + scene.getName()
                        + ". Scene names have to be unique!", e);
            } else if (e.getMessage().contains("NOT NULL constraint failed")) {
                throw new RequiredValueMissingException("Scene name, length and position are required!", e);
            }
        }
    }

    public void createRehearsal(RehearsalDTO rehearsal) throws DuplicateException, RequiredValueMissingException {
        String sql = "insert into rehearsals (day, locked_rehearsal) values (?, false)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, rehearsal.getDate());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                rehearsal.setId(rs.getInt(1));
            }
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateException("Rehearsal days have to be unique!", e);
            } else if (e.getMessage().contains("NOT NULL constraint failed")) {
                throw new RequiredValueMissingException("A date must be given!", e);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void createPlaysIn(SceneDTO scene, RoleDTO role, boolean minor) throws DuplicateException {
        String sql = "insert into plays_in (scene_id, role_id, minor) values (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scene.getId());
            stmt.setInt(2, role.getId());
            stmt.setBoolean(3, minor);
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (!e.getMessage().contains("UNIQUE constraint failed")) {
                throw new RuntimeException(e);
            }
        }
    }

    public void createHasNoTime(ActorDTO actor, RehearsalDTO rehearsal, boolean maybe) throws DuplicateException {
        String sql = "insert into has_no_time (actor_id, rehearsal_id, maybe) values (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, actor.getId());
            stmt.setInt(2, rehearsal.getId());
            stmt.setBoolean(3, maybe);
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateException("An actor can only have one entry per rehearsal!", e);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public void lockScene(SceneDTO scene, RehearsalDTO rehearsal) throws DuplicateException {
        String sql = "insert into locked_scenes (scene_id, rehearsal_id) values (?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scene.getId());
            stmt.setInt(2, rehearsal.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            if (!e.getMessage().contains("UNIQUE constraint failed")) {
                throw new RuntimeException(e);
            }
        }
    }

    public void removeLock(SceneDTO scene, RehearsalDTO rehearsal) {
        String sql = "delete from locked_scenes where scene_id = ? and rehearsal_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, scene.getId());
            stmt.setInt(2, rehearsal.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void lockRehearsal(RehearsalDTO rehearsal, boolean locked) {
        String sql = "update rehearsals set locked_rehearsal = ? where rehearsal_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setBoolean(1, locked);
            stmt.setInt(2, rehearsal.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SqlWithoutWhere")
    public void clearLocks() {
        String sql = "delete from locked_scenes";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        sql = "update rehearsals set locked_rehearsal = false";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
