package org.aarquelle.probenplan_pa.data.dao;

import org.aarquelle.probenplan_pa.dto.ActorDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DAO extends AbstractDAO{
    public DAO(Connection conn) {
        super(conn);
    }

    public void createActor(String name) {
        String sql = "insert into actors (actor_name) values (?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (Exception e) {
            throw new RuntimeException(e);
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
}
