package org.aarquelle.probenplan_pa.business.suggest;

import org.aarquelle.probenplan_pa.business.BusinessException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.aarquelle.probenplan_pa.dto.ParamsDTO;

import static org.aarquelle.probenplan_pa.TestUtils.*;

public class GeneratorTest {

    @BeforeAll
    static void setUp() throws BusinessException {
        createTestData();
    }

    @Test
    public void testSingleGenerator() {
        ParamsDTO params = new ParamsDTO();
        Generator generator = new Generator(5, params);
        System.out.println(generator.generatePlan().verboseToString());
    }

    @Test
    public void testGenerator() {
        ParamsDTO params = new ParamsDTO();

        for (int i = 0; i < 1000; i++) {
            Generator generator = new Generator(i, params);
            generator.generatePlan();
        }
    }
}
