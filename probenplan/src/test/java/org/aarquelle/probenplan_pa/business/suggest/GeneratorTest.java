package org.aarquelle.probenplan_pa.business.suggest;

import org.aarquelle.probenplan_pa.business.BusinessException;
import org.aarquelle.probenplan_pa.dto.PlanDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.aarquelle.probenplan_pa.dto.ParamsDTO;

import static org.aarquelle.probenplan_pa.TestUtils.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
}
