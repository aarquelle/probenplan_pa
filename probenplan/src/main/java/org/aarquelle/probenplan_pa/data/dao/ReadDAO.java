package org.aarquelle.probenplan_pa.data.dao;

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

public class ReadDAO extends AbstractDAO {
    public ReadDAO(Connection conn) {
        super(conn);
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

    public List<RoleDTO> getRolesForScene(SceneDTO sceneDTO) {
        return getRolesForScene(sceneDTO, false, false);
    }

    public List<RoleDTO> getRolesForScene(SceneDTO sceneDTO, boolean minor) {
        return getRolesForScene(sceneDTO, true, minor);
    }

    private List<RoleDTO> getRolesForScene(SceneDTO sceneDTO, boolean sizeMatters, boolean minor) {
        String sql = "select roles.role_id, role_name from roles, plays_in where " +
                "scene_id = ? and roles.role_id = plays_in.role_id";
        if (sizeMatters) {
            sql += " and minor = ?";
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, sceneDTO.getId());
            if (sizeMatters) {
                stmt.setBoolean(2, minor);
            }
            ResultSet rs = stmt.executeQuery();
            List<RoleDTO> results = new ArrayList<>();
            while (rs.next()) {
                RoleDTO role = new RoleDTO();
                role.setId(rs.getInt("role_id"));
                role.setName(rs.getString("role_name"));
                results.add(role);
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<ActorDTO> getMissingActorsForRehearsal(RehearsalDTO rehearsalDTO) {
        return getMissingActorsForRehearsal(rehearsalDTO, false, false);
    }

    public List<ActorDTO> getMissingActorsForRehearsal(RehearsalDTO rehearsalDTO, boolean maybe) {
        return getMissingActorsForRehearsal(rehearsalDTO, true, maybe);
    }

    private List<ActorDTO> getMissingActorsForRehearsal(RehearsalDTO rehearsalDTO, boolean maybeMatters, boolean maybe) {
        String sql = "select actors.actor_id, actor_name, maybe from actors, has_no_time where " +
                "day = ? and actors.actor_id = has_no_time.actor_id";
        if (maybeMatters) {
            sql += " and maybe = ?";
        }
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDate(1, rehearsalDTO.getDate());
            if (maybeMatters) {
                stmt.setBoolean(2, maybe);
            }
            ResultSet rs = stmt.executeQuery();
            List<ActorDTO> results = new ArrayList<>();
            while (rs.next()) {
                ActorDTO actor = new ActorDTO();
                actor.setId(rs.getInt("actor_id"));
                actor.setName(rs.getString("actor_name"));
                results.add(actor);
            }
            return results;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
