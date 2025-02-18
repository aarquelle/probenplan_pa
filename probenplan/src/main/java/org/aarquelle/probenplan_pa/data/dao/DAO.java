package org.aarquelle.probenplan_pa.data.dao;

import org.aarquelle.probenplan_pa.data.exception.DuplicateException;
import org.aarquelle.probenplan_pa.data.exception.RequiredValueMissingException;
import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.RoleDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO extends AbstractDAO {
    public DAO(Connection conn) {
        super(conn);
    }

    /**
     * Create a new actor in the database.
     *
     * @param actor Contains the name. Will contain the id after the method returns.
     * @throws DuplicateException Thrown if the actor name is not unique.
     */
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

    public List<ActorDTO> getActors() {
        String sql = "select * from actors";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            List<ActorDTO> results = new ArrayList<>();
            while (rs.next()) {
                ActorDTO actor = new ActorDTO();
                actor.setName(rs.getString("actor_name"));
                actor.setId(rs.getInt("actor_id"));
                results.add(actor);
            }
            return results;
        } catch (Exception e) {
            throw new RuntimeException(e);
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
            System.out.println(e.getMessage());
            if (e.getMessage().contains("UNIQUE constraint failed")) {
                throw new DuplicateException("Role names have to be unique!", e);
            } else {
                throw new RuntimeException(e);
            }
        }
    }

    public List<RoleDTO> getRoles() {
        String sql = "select role_id, role_name, actor_id, actor_name from roles " +
                "left natural join actors";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            List<RoleDTO> results = new ArrayList<>();
            while (rs.next()) {
                RoleDTO role = new RoleDTO();
                role.setName(rs.getString("role_name"));
                role.setId(rs.getInt("role_id"));
                int actorId = rs.getInt("actor_id");
                if (!rs.wasNull()) {
                    ActorDTO actor = new ActorDTO();
                    actor.setId(actorId);
                    actor.setName(rs.getString("actor_name"));
                    role.setActor(actor);
                }
                results.add(role);
            }
            return results;
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
                throw new DuplicateException("Scene names have to be unique!", e);
            } else if (e.getMessage().contains("NOT NULL constraint failed")) {
                throw new RequiredValueMissingException("Scene name, length and position are required!", e);
            }
        }
    }

    public List<SceneDTO> getScenes() {
        String sql = "select * from scenes";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            List<SceneDTO> results = new ArrayList<>();
            while (rs.next()) {
                SceneDTO scene = new SceneDTO();
                scene.setName(rs.getString("scene_name"));
                scene.setId(rs.getInt("scene_id"));
                scene.setLength(rs.getDouble("length"));
                scene.setPosition(rs.getDouble("position"));
                results.add(scene);
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createRehearsal(RehearsalDTO rehearsal) throws DuplicateException, RequiredValueMissingException {
        String sql = "insert into rehearsals (day) values (?)";
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

    public List<RehearsalDTO> getRehearsals() {
        String sql = "select * from rehearsals order by day";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            List<RehearsalDTO> results = new ArrayList<>();
            while (rs.next()) {
                RehearsalDTO rehearsal = new RehearsalDTO();
                rehearsal.setId(rs.getInt("id"));
                rehearsal.setDate(rs.getDate("day"));
                results.add(rehearsal);
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
