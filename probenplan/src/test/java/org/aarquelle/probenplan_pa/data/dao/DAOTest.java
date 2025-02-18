package org.aarquelle.probenplan_pa.data.dao;

import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DAOTest {

    @BeforeEach
    void setUp() {
        try (Transaction t = new Transaction()) {
            DBManager dbManager = t.getDBManager();
            dbManager.clearDB();
            dbManager.initDB();
            t.commit();
        }
    }

    @Test
    void createActor() {
        try (Transaction t = new Transaction()) {
            DAO dao = t.getDAO();
            dao.createActor("actor1");
            t.commit();
            List<ActorDTO> results = dao.getActors();
            assertEquals(1, results.size());
            assertEquals("actor1", results.getFirst().getName());
            assertEquals(1, results.getFirst().getId());
        }
    }
}