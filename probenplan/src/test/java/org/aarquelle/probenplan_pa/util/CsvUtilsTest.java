package org.aarquelle.probenplan_pa.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CsvUtilsTest {

    @Test
    void parseArgs() {
        String[] args = """
                	Alfons Müller	Berta	Christian	Denise	Emil
                1.4	x				x
                2.4		?			?
                5.4		?		\t
                6.4				\t
                19.6				\t
               \s
               \s
               \s""".split(" ");

        String[][] result = CsvUtils.parseArgs(args);
        assertEquals("", result[0][0]);
        assertEquals("Alfons Müller", result[0][1]);
        assertEquals("Emil", result[0][5]);
        assertEquals("x", result[1][1]);
        assertEquals("19.6", result[5][0]);
    }
}