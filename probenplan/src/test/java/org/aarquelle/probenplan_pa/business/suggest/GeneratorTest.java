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
    public void testGenerator() {
        long seed = 2;
        ParamsDTO params = new ParamsDTO();

        for (int i = 0; i < 10000; i++) {
            Generator generator = new Generator(seed, params);
            generator.generatePlan();
        }
    }
}
