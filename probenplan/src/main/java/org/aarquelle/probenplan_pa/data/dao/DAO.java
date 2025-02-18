package org.aarquelle.probenplan_pa.data.dao;

import org.aarquelle.probenplan_pa.data.exception.DuplicateException;
import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.aarquelle.probenplan_pa.dto.RoleDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DAO extends AbstractDAO{
    public DAO(Connection conn) {
        super(conn);
    }

    /**
     * Create a new actor in the database.
     * @param actor Contains the name. Will contain the id after the method returns.
     * @throws DuplicateException Thrown if the actor name is not unique.
     */
    public void createActor(ActorDTO actor) throws DuplicateException{
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
            while(rs.next()) {
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

    public void createRole(RoleDTO role) {
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
            while(rs.next()) {
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
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
