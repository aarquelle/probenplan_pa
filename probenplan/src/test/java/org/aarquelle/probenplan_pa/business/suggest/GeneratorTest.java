package org.aarquelle.probenplan_pa.business.suggest;

import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.business.create.Creator;
import org.aarquelle.probenplan_pa.dto.PlanDTO;
import org.aarquelle.probenplan_pa.util.Pair;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.aarquelle.probenplan_pa.dto.ParamsDTO;

import static org.aarquelle.probenplan_pa.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeneratorTest {

    @BeforeAll
    static void setUp() throws BusinessException {
        createTestData();
    }

    @Test
    public void testSingleGenerator() {
        ParamsDTO params = new ParamsDTO();
        Generator generator = new Generator(1, params);
        PlanDTO plan = generator.generatePlan();
        System.out.println(plan.verboseToString());
        Analyzer.runAnalysis();
        System.out.println(new Evaluator(plan, params).evaluate());
    }

    @Disabled
    @Test
    public void testGenerator() {
        ParamsDTO params = new ParamsDTO();
        Analyzer.runAnalysis();
        double maximum = 0;
        PlanDTO maxPlan = null;
        int basisSeed = 0;
        for (int i = basisSeed; i < basisSeed + 10000; i++) {
            Generator generator = new Generator(i, params);
            PlanDTO plan = generator.generatePlan();
            double result = new Evaluator(plan, params).evaluate();
            if (result > maximum) {
                maximum = result;
                maxPlan = plan;
                System.out.println("New maximum: " + maximum + " reached at step " + i);
            }
        }
        assertNotNull(maxPlan);
        System.out.println(maxPlan.verboseToString());
    }

    @Test
    public void testLocks() throws BusinessException {
        ParamsDTO params = new ParamsDTO();
        Generator generator = new Generator(1, params);

        Creator.lockScene(scene1, rehearsal1);

        PlanDTO plan = generator.generatePlan();
        System.out.println(plan.verboseToString());
        assertTrue(plan.get(rehearsal1).contains(scene1));

        Creator.clearLocks();
    }
}
