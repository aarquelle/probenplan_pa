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

package org.aarquelle.probenplan_pa.util;

import org.aarquelle.probenplan_pa.business.BusinessException;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

class CsvUtilsTest {

    @Test
    void importFromClipboard() throws BusinessException {
        try (MockedStatic<CsvUtils> mockedClipboard = mockStatic(CsvUtils.class)) {

            String args = """
                     	Alfons Müller	Berta	Christian	Denise	Emil
                     1.4	x				x
                     2.4		?			?
                     5.4		?		\t
                     6.4				\t
                     19.6				\t
                    \s
                    \s
                    \s""".replace("\n", System.lineSeparator());
            mockedClipboard.when(CsvUtils::getClipBoard).thenReturn(args);
            mockedClipboard.when(CsvUtils::importFromClipboard).thenCallRealMethod();
            String[][] result = CsvUtils.importFromClipboard();

            assertEquals("", result[0][0]);
            assertEquals("Alfons Müller", result[0][1]);
            assertEquals("Emil", result[0][5]);
            assertEquals("x", result[1][1]);
            assertEquals("19.6", result[5][0]);
        }
    }
}