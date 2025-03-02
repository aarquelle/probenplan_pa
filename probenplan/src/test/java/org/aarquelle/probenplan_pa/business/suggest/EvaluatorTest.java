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

}