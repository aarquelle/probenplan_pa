package org.aarquelle.probenplan_pa.data.dao;

import org.aarquelle.probenplan_pa.data.exception.DuplicateException;
import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.aarquelle.probenplan_pa.dto.RoleDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DAOTest {

    private ActorDTO a1;

    @BeforeEach
    void setUp() {
        try (Transaction t = new Transaction()) {
            DBManager dbManager = t.getDBManager();
            dbManager.clearDB();
            dbManager.initDB();
            t.commit();
        }
        a1 = new ActorDTO();
        a1.setName("actor1");
    }

    @Test
    void createActor() throws Exception {
        try (Transaction t = new Transaction()) {
            DAO dao = t.getDAO();
            dao.createActor(a1);
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
            dao.createActor(a1);
            t.commit();
            assertThrows(DuplicateException.class, () -> dao.createActor(a1));
        }
    }

    @Test
    void createRole() throws Exception {
        try (Transaction t = new Transaction()) {
            DAO dao = t.getDAO();
            dao.createActor(a1);
            RoleDTO role = new RoleDTO();
            role.setName("role1");
            role.setActor(a1);
            dao.createRole(role);
            t.commit();
            List<RoleDTO> results = dao.getRoles();
            assertEquals(1, results.size());
            assertEquals("role1", results.getFirst().getName());
            assertEquals(1, results.getFirst().getId());
            assertEquals("actor1", results.getFirst().getActor().getName());
            assertEquals(1, results.getFirst().getActor().getId());
        }
    }

    @Test
    void createRoleWithoutActor() {
        try (Transaction t = new Transaction()) {
            DAO dao = t.getDAO();
            RoleDTO role = new RoleDTO();
            role.setName("role1");
            dao.createRole(role);
            t.commit();
            List<RoleDTO> results = dao.getRoles();
            assertEquals(1, results.size());
            assertEquals("role1", results.getFirst().getName());
            assertEquals(1, results.getFirst().getId());
            assertNull(results.getFirst().getActor());
        }
    }


}