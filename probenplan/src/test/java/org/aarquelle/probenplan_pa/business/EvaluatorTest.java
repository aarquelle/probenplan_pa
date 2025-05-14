/*
 * Copyright (c) 2025, Aaron Prott
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package org.aarquelle.probenplan_pa.business;

import org.aarquelle.probenplan_pa.entity.Plan;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.aarquelle.probenplan_pa.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class EvaluatorTest {

    static Plan plan1;
    static Plan plan2;

    @BeforeAll
    static void setUp() throws BusinessException {
        createTestData();
        Analyzer.runAnalysis();

        plan1 = new Plan();
        plan1.put(rehearsal1, scene1);
        plan1.put(rehearsal1, scene2);
        plan1.put(rehearsal1, scene3);
        plan1.put(rehearsal2, scene4);
        plan1.put(rehearsal2, scene5);
        plan1.put(rehearsal3, scene1);
        plan1.put(rehearsal3, scene2);
        plan1.put(rehearsal3, scene3);
        plan1.put(rehearsal3, scene4);
        plan1.put(rehearsal3, scene5);
        plan1.put(rehearsal4, scene1);
        plan1.put(rehearsal5, scene5);

        plan2 = new Plan();
        plan2.put(rehearsal1, scene1);
        plan2.put(rehearsal1, scene3);
        plan2.put(rehearsal2, scene4);
        plan2.put(rehearsal2, scene5);
        plan2.put(rehearsal3, scene1);
        plan2.put(rehearsal3, scene2);
        plan2.put(rehearsal3, scene3);
        plan2.put(rehearsal3, scene4);
        plan2.put(rehearsal3, scene5);
        plan2.put(rehearsal4, scene1);
        plan2.put(rehearsal4, scene2);
        plan2.put(rehearsal5, scene5);
    }

    @Test
    void testEvaluateAllScenesBeforeDLP() {
        Evaluator evaluator = new Evaluator(plan1, new Params());
        assertTrue(evaluator.allScenesBeforeDurchlaufprobe());

        assertFalse(new Evaluator(plan2, new Params()).allScenesBeforeDurchlaufprobe());
    }

    @Test
    void testTotalLength() {
        assertEquals(12, plan1.totalLength());
    }

    @Test
    void testDlpCompleteness() {
        Evaluator evaluator = new Evaluator(plan1, new Params());
        assertDoubleEquals(0.793333, evaluator.dlpCompleteness());
    }

    @Test
    void testBeforeDlpCompleteness() {
        Evaluator evaluator = new Evaluator(plan1, new Params());
        assertDoubleEquals(0.6466666666667, evaluator.completenessBeforeDLP());
    }

    @Test
    void testLumpiness() {
        Evaluator evaluator = new Evaluator(plan1, new Params());
        assertDoubleEquals(1.0, evaluator.lumpiness());
        evaluator = new Evaluator(plan2, new Params());
        assertDoubleEquals(0.9, evaluator.lumpiness());
    }

    @Test
    void testNumberOfRepeats() {
        Evaluator evaluator = new Evaluator(plan1, new Params());
        assertDoubleEquals(2.0, evaluator.numberOfRepeats.get(scene1));
        assertDoubleEquals(1.5, evaluator.numberOfRepeats.get(scene2));
        assertDoubleEquals(1.5, evaluator.numberOfRepeats.get(scene3));
        assertDoubleEquals(2, evaluator.numberOfRepeats.get(scene4));
        assertDoubleEquals(1.2+1-1.0/6*4.0/5, evaluator.numberOfRepeats.get(scene5));
        assertDoubleEquals(1.5/2, evaluator.getMinimumRepeats());
        assertDoubleEquals(2.0/2, evaluator.getMedianRepeats());
    }

    @Test
    void testNumberOfRoles() {
        Evaluator evaluator = new Evaluator(plan1, new Params());
        assertEquals(4, evaluator.getNumberOfRolesInRehearsal(rehearsal1));
        assertEquals(3, evaluator.getNumberOfRolesInRehearsal(rehearsal2));
        assertEquals(4, evaluator.getNumberOfRolesInRehearsal(rehearsal3));
        assertEquals(2, evaluator.getNumberOfRolesInRehearsal(rehearsal4));
        assertEquals(3, evaluator.getNumberOfRolesInRehearsal(rehearsal5));
    }
}