package org.aarquelle.probenplan_pa.business.suggest;

import org.aarquelle.probenplan_pa.TestUtils;
import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.business.create.Creator;
import org.aarquelle.probenplan_pa.data.dao.Transaction;
import org.aarquelle.probenplan_pa.dto.*;
import org.aarquelle.probenplan_pa.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.aarquelle.probenplan_pa.TestUtils.*;

class PossibilitiesTest {



    @BeforeAll
    static void setUp() throws BusinessException {
        createTestData();
    }

    @Test
    public void testNumberOfActors() {
        Map<SceneDTO, Integer> allRoles = Possibilities.tableNumberOfRoles(false);
        assertEquals(2, allRoles.get(scene1));
        assertEquals(2, allRoles.get(scene2));
        assertEquals(2, allRoles.get(scene3));
        assertEquals(1, allRoles.get(scene4));
        assertEquals(3, allRoles.get(scene5));

        Map<SceneDTO, Integer> majorRoles = Possibilities.tableNumberOfRoles(true);
        assertEquals(1, majorRoles.get(scene1));
        assertEquals(2, majorRoles.get(scene2));
        assertEquals(2, majorRoles.get(scene3));
        assertEquals(1, majorRoles.get(scene4));
        assertEquals(2, majorRoles.get(scene5));

    }

    @Test
    public void testMissingActors() {
        RehearsalSceneTable results = Possibilities.missingActors();
        assertEquals(1, results.get(rehearsal1, scene1));
        assertEquals(0, results.get(rehearsal1, scene2));
        assertEquals(1, results.get(rehearsal1, scene3));
        assertEquals(1, results.get(rehearsal1, scene4));
        assertEquals(1, results.get(rehearsal1, scene5));

        assertEquals(1, results.get(rehearsal2, scene1));
        assertEquals(2, results.get(rehearsal2, scene2));
        assertEquals(1, results.get(rehearsal2, scene3));
        assertEquals(0, results.get(rehearsal2, scene4));
        assertEquals(2, results.get(rehearsal2, scene5));

        assertEquals(1, results.get(rehearsal3, scene1));
        assertEquals(1, results.get(rehearsal3, scene2));
        assertEquals(0, results.get(rehearsal3, scene3));
        assertEquals(0, results.get(rehearsal3, scene4));
        assertEquals(1, results.get(rehearsal3, scene5));

        assertEquals(0, results.get(rehearsal4, scene1));
        assertEquals(1, results.get(rehearsal4, scene2));
        assertEquals(1, results.get(rehearsal4, scene3));
        assertEquals(0, results.get(rehearsal4, scene4));
        assertEquals(1, results.get(rehearsal4, scene5));

        assertEquals(0, results.get(rehearsal5, scene1));
        assertEquals(0, results.get(rehearsal5, scene2));
        assertEquals(0, results.get(rehearsal5, scene3));
        assertEquals(0, results.get(rehearsal5, scene4));
        assertEquals(0, results.get(rehearsal5, scene5));
    }

    @Test
    public void testUncertainActors() {
        RehearsalSceneTable results = Possibilities.uncertainActors();
        assertEquals(0, results.get(rehearsal1, scene1));
        assertEquals(0, results.get(rehearsal1, scene2));
        assertEquals(0, results.get(rehearsal1, scene3));
        assertEquals(0, results.get(rehearsal1, scene4));
        assertEquals(0, results.get(rehearsal1, scene5));

        assertEquals(0, results.get(rehearsal2, scene1));
        assertEquals(0, results.get(rehearsal2, scene2));
        assertEquals(0, results.get(rehearsal2, scene3));
        assertEquals(0, results.get(rehearsal2, scene4));
        assertEquals(0, results.get(rehearsal2, scene5));

        assertEquals(0, results.get(rehearsal3, scene1));
        assertEquals(0, results.get(rehearsal3, scene2));
        assertEquals(0, results.get(rehearsal3, scene3));
        assertEquals(0, results.get(rehearsal3, scene4));
        assertEquals(0, results.get(rehearsal3, scene5));

        assertEquals(0, results.get(rehearsal4, scene1));
        assertEquals(0, results.get(rehearsal4, scene2));
        assertEquals(0, results.get(rehearsal4, scene3));
        assertEquals(0, results.get(rehearsal4, scene4));
        assertEquals(0, results.get(rehearsal4, scene5));

        assertEquals(1, results.get(rehearsal5, scene1));
        assertEquals(0, results.get(rehearsal5, scene2));
        assertEquals(1, results.get(rehearsal5, scene3));
        assertEquals(1, results.get(rehearsal5, scene4));
        assertEquals(1, results.get(rehearsal5, scene5));
    }

    @Test
    public void testMissingMajorActors() {
        RehearsalSceneTable results = Possibilities.majorMissingActors();
        assertEquals(1, results.get(rehearsal1, scene1));
        assertEquals(0, results.get(rehearsal1, scene2));
        assertEquals(1, results.get(rehearsal1, scene3));
        assertEquals(1, results.get(rehearsal1, scene4));
        assertEquals(1, results.get(rehearsal1, scene5));

        assertEquals(0, results.get(rehearsal2, scene1));
        assertEquals(2, results.get(rehearsal2, scene2));
        assertEquals(1, results.get(rehearsal2, scene3));
        assertEquals(0, results.get(rehearsal2, scene4));
        assertEquals(1, results.get(rehearsal2, scene5));

        assertEquals(0, results.get(rehearsal3, scene1));
        assertEquals(1, results.get(rehearsal3, scene2));
        assertEquals(0, results.get(rehearsal3, scene3));
        assertEquals(0, results.get(rehearsal3, scene4));
        assertEquals(0, results.get(rehearsal3, scene5));

        assertEquals(0, results.get(rehearsal4, scene1));
        assertEquals(1, results.get(rehearsal4, scene2));
        assertEquals(1, results.get(rehearsal4, scene3));
        assertEquals(0, results.get(rehearsal4, scene4));
        assertEquals(1, results.get(rehearsal4, scene5));

        assertEquals(0, results.get(rehearsal5, scene1));
        assertEquals(0, results.get(rehearsal5, scene2));
        assertEquals(0, results.get(rehearsal5, scene3));
        assertEquals(0, results.get(rehearsal5, scene4));
        assertEquals(0, results.get(rehearsal5, scene5));
    }

    @Test
    public void testUncertainMajorActors() {
        RehearsalSceneTable results = Possibilities.majorUncertainActors();
        assertEquals(0, results.get(rehearsal1, scene1));
        assertEquals(0, results.get(rehearsal1, scene2));
        assertEquals(0, results.get(rehearsal1, scene3));
        assertEquals(0, results.get(rehearsal1, scene4));
        assertEquals(0, results.get(rehearsal1, scene5));

        assertEquals(0, results.get(rehearsal2, scene1));
        assertEquals(0, results.get(rehearsal2, scene2));
        assertEquals(0, results.get(rehearsal2, scene3));
        assertEquals(0, results.get(rehearsal2, scene4));
        assertEquals(0, results.get(rehearsal2, scene5));

        assertEquals(0, results.get(rehearsal3, scene1));
        assertEquals(0, results.get(rehearsal3, scene2));
        assertEquals(0, results.get(rehearsal3, scene3));
        assertEquals(0, results.get(rehearsal3, scene4));
        assertEquals(0, results.get(rehearsal3, scene5));

        assertEquals(0, results.get(rehearsal4, scene1));
        assertEquals(0, results.get(rehearsal4, scene2));
        assertEquals(0, results.get(rehearsal4, scene3));
        assertEquals(0, results.get(rehearsal4, scene4));
        assertEquals(0, results.get(rehearsal4, scene5));

        assertEquals(1, results.get(rehearsal5, scene1));
        assertEquals(0, results.get(rehearsal5, scene2));
        assertEquals(1, results.get(rehearsal5, scene3));
        assertEquals(1, results.get(rehearsal5, scene4));
        assertEquals(1, results.get(rehearsal5, scene5));
    }
}