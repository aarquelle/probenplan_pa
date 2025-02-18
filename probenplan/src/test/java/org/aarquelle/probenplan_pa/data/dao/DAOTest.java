package org.aarquelle.probenplan_pa.data.dao;

import org.aarquelle.probenplan_pa.data.exception.DuplicateException;
import org.aarquelle.probenplan_pa.data.exception.RequiredValueMissingException;
import org.aarquelle.probenplan_pa.dto.ActorDTO;
import org.aarquelle.probenplan_pa.dto.RehearsalDTO;
import org.aarquelle.probenplan_pa.dto.RoleDTO;
import org.aarquelle.probenplan_pa.dto.SceneDTO;
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
            ReadDAO readDao = t.getReadDAO();
            CreateDAO createDAO = t.getCreateDAO();
            createDAO.createActor(a1);
            t.commit();
            List<ActorDTO> results = readDao.getActors();
            assertEquals(1, results.size());
            assertEquals("actor1", results.getFirst().getName());
            assertEquals(1, results.getFirst().getId());
            assertEquals(1, results.getFirst().getId());
        }
    }

    @Test
    void createDuplicateActor() throws Exception {
        try (Transaction t = new Transaction()) {
            CreateDAO createDAO = t.getCreateDAO();
            createDAO.createActor(a1);
            t.commit();
            assertThrows(DuplicateException.class, () -> createDAO.createActor(a1));
        }
    }

    @Test
    void createRole() throws Exception {
        try (Transaction t = new Transaction()) {
            ReadDAO readDao = t.getReadDAO();
            CreateDAO createDAO = t.getCreateDAO();
            createDAO.createActor(a1);
            RoleDTO role = new RoleDTO();
            role.setName("role1");
            role.setActor(a1);
            createDAO.createRole(role);
            t.commit();
            List<RoleDTO> results = readDao.getRoles();
            assertEquals(1, results.size());
            assertEquals("role1", results.getFirst().getName());
            assertEquals(1, results.getFirst().getId());
            assertEquals("actor1", results.getFirst().getActor().getName());
            assertEquals(1, results.getFirst().getActor().getId());
        }
    }

    @Test
    void createRoleWithoutActor() throws Exception{
        try (Transaction t = new Transaction()) {
            ReadDAO readDao = t.getReadDAO();
            CreateDAO createDAO = t.getCreateDAO();
            RoleDTO role = new RoleDTO();
            role.setName("role1");
            createDAO.createRole(role);
            t.commit();
            List<RoleDTO> results = readDao.getRoles();
            assertEquals(1, results.size());
            assertEquals("role1", results.getFirst().getName());
            assertEquals(1, results.getFirst().getId());
            assertNull(results.getFirst().getActor());
            assertThrows(DuplicateException.class, () -> createDAO.createRole(role));
        }
    }

    @Test
    void createScene() throws Exception {
        try (Transaction t = new Transaction()) {
            ReadDAO readDao = t.getReadDAO();
            CreateDAO createDAO = t.getCreateDAO();
            SceneDTO scene = new SceneDTO();
            scene.setName("scene1");
            scene.setLength(10.5);
            scene.setPosition(1.0);
            createDAO.createScene(scene);
            t.commit();
            List<SceneDTO> results = readDao.getScenes();
            assertEquals(1, results.size());
            assertEquals("scene1", results.getFirst().getName());
            assertEquals(10.5, results.getFirst().getLength());
            assertEquals(1.0, results.getFirst().getPosition());
            assertEquals(1, results.getFirst().getId());
        }
    }

    @Test
    void createDuplicateScene() throws Exception {
        try (Transaction t = new Transaction()) {
            CreateDAO createDAO = t.getCreateDAO();
            SceneDTO scene = new SceneDTO();
            scene.setName("scene1");
            scene.setLength(10.5);
            scene.setPosition(1.0);
            createDAO.createScene(scene);
            t.commit();
            assertThrows(DuplicateException.class, () -> createDAO.createScene(scene));
        }
    }

    @Test
    void createSceneWithNullValues() {
        try (Transaction t = new Transaction()) {
            CreateDAO createDAO = t.getCreateDAO();
            SceneDTO scene = new SceneDTO();
            scene.setName(null);
            scene.setLength(0);
            scene.setPosition(0);
            assertThrows(RequiredValueMissingException.class, () -> createDAO.createScene(scene));
        }
    }

    @Test
    void createRehearsal() throws Exception {
        try (Transaction t = new Transaction()) {
            ReadDAO readDao = t.getReadDAO();
            CreateDAO createDAO = t.getCreateDAO();
            RehearsalDTO rehearsal = new RehearsalDTO();
            rehearsal.setDate(java.sql.Date.valueOf("2023-12-01"));
            createDAO.createRehearsal(rehearsal);
            t.commit();
            List<RehearsalDTO> results = readDao.getRehearsals();
            assertEquals(1, results.size());
            assertEquals(java.sql.Date.valueOf("2023-12-01"), results.getFirst().getDate());
            assertEquals(1, results.getFirst().getId());
        }
    }

    @Test
    void createDuplicateRehearsal() throws Exception {
        try (Transaction t = new Transaction()) {
            CreateDAO createDAO = t.getCreateDAO();
            RehearsalDTO rehearsal = new RehearsalDTO();
            rehearsal.setDate(java.sql.Date.valueOf("2023-12-01"));
            createDAO.createRehearsal(rehearsal);
            t.commit();
            assertThrows(DuplicateException.class, () -> createDAO.createRehearsal(rehearsal));
        }
    }

    @Test
    void createRehearsalWithNullDate() {
        try (Transaction t = new Transaction()) {
            CreateDAO createDAO = t.getCreateDAO();
            RehearsalDTO rehearsal = new RehearsalDTO();
            assertThrows(RequiredValueMissingException.class, () -> createDAO.createRehearsal(rehearsal));
        }
    }

}