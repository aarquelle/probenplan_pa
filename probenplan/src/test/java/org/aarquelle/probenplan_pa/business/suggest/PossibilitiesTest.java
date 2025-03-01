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

    /**
     * Testszenario:
     * <p>
     * Alice spielt den Helden, Bob den Bösewicht und Charlie den Boten und den Hirten.
     * <p>
     * Szene 1: Hirtenszene: Held, Hirte (klein)
     * Szene 2: Botenszene: Bösewicht, Bote
     * Szene 3: Herausforderung: Held trifft Bösewicht.
     * Szene 4: Monolog: Held hält Monolog.
     * Szene 5: Kampf: Held trifft Bösewicht und Boten (klein).
     * <p>
     * Proben:
     * 1.1, 1.2, 1.3, 1.4, 1.5 2025
     * <p>
     * Alice kann am 1.1 nicht. Also: Nur Szene 2 am 1.1
     * Nur Alice kann am 1.2. Also: Szene 1 semi, Szene 4 am 1.2
     * Charlie kann am 1.3 nicht. Also: Szenen 3 und 4 ganz, Szenen 1 und 5 semi, Szene 2 gar nicht
     * Bob kann am 1.4 nicht. Also: Szene 1 und 4 ganz, Szenen 2, 3 und 5 gar nicht.
     * Am 1.5 können alle, aber Alice nur vielleicht. Also: Szene 2 ganz, Szenen 1, 3, 4, 5 semi.
     */

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