package org.aarquelle.probenplan_pa.data.dao;

import org.aarquelle.probenplan_pa.data.exception.DuplicateException;
import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DAOTest {

    private ActorDTO a1() {
        ActorDTO a = new ActorDTO();
        a.setName("actor1");
        return a;
    }

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
    void createActor() throws Exception {
        try (Transaction t = new Transaction()) {
            DAO dao = t.getDAO();
            dao.createActor(a1());
            t.commit();
            List<ActorDTO> results = dao.getActors();
            assertEquals(1, results.size());
            assertEquals("actor1", results.getFirst().getName());
            assertEquals(1, results.getFirst().getId());
            assertEquals(1, results.getFirst().getId());
        }
    }

    @Test
    void createDuplicateActor() throws Exception {
        try (Transaction t = new Transaction()) {
            DAO dao = t.getDAO();
            dao.createActor(a1());
            t.commit();
            assertThrows(DuplicateException.class, () -> dao.createActor(a1()));
        }
    }


}