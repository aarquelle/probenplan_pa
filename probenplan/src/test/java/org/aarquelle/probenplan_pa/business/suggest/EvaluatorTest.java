package org.aarquelle.probenplan_pa.business.suggest;

import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.dto.ParamsDTO;
import org.aarquelle.probenplan_pa.dto.PlanDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.aarquelle.probenplan_pa.TestUtils.*;
import static org.junit.jupiter.api.Assertions.*;

class EvaluatorTest {

    static PlanDTO plan1;
    static PlanDTO plan2;

    @BeforeAll
    static void setUp() throws BusinessException {
        createTestData();
        Analyzer.runAnalysis();

        plan1 = new PlanDTO();
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

        plan2 = new PlanDTO();
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
        Evaluator evaluator = new Evaluator(plan1, new ParamsDTO());
        assertTrue(evaluator.allScenesBeforeDurchlaufprobe());

        assertFalse(new Evaluator(plan2, new ParamsDTO()).allScenesBeforeDurchlaufprobe());
    }

    @Test
    void testTotalLength() {
        assertEquals(12, plan1.totalLength());
    }

    @Test
    void testDlpCompleteness() {
        Evaluator evaluator = new Evaluator(plan1, new ParamsDTO());
        assertDoubleEquals(0.793333, evaluator.dlpCompleteness());
    }

    @Test
    void testBeforeDlpCompleteness() {
        Evaluator evaluator = new Evaluator(plan1, new ParamsDTO());
        assertDoubleEquals(0.6466666666667, evaluator.completenessBeforeDLP());
    }

    @Test
    void testLumpiness() {
        Evaluator evaluator = new Evaluator(plan1, new ParamsDTO());
        assertDoubleEquals(1.0, evaluator.lumpiness());
        evaluator = new Evaluator(plan2, new ParamsDTO());
        assertDoubleEquals(0.9, evaluator.lumpiness());
    }

    @Test
    void testNumberOfRepeats() {
        Evaluator evaluator = new Evaluator(plan1, new ParamsDTO());
        assertDoubleEquals(2.0, evaluator.numberOfRepeats.get(scene1));
        assertDoubleEquals(1.5, evaluator.numberOfRepeats.get(scene2));
        assertDoubleEquals(1.5, evaluator.numberOfRepeats.get(scene3));
        assertDoubleEquals(2, evaluator.numberOfRepeats.get(scene4));
        assertDoubleEquals(1.2+1-1.0/6*4.0/5, evaluator.numberOfRepeats.get(scene5));
        assertDoubleEquals(1.5/2, evaluator.getMinimumRepeats());
        assertDoubleEquals(2.0/2, evaluator.getMedianRepeats());
    }
}