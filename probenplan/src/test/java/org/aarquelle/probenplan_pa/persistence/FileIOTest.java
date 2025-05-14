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

package org.aarquelle.probenplan_pa.persistence;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.aarquelle.probenplan_pa.persistence.FileUtils.*;
import static org.junit.jupiter.api.Assertions.*;
class FileIOTest {

    @TempDir
    public java.nio.file.Path tempDir;

    @Test
    public void testWrite() throws IOException {
        try(FileOutputStream fos = new FileOutputStream(tempDir.resolve("test.txt").toString())) {
            byte b = 100;
            fos.write(b);
            short s = 500;
            fos.write(shortToTwoByte(s));
            fos.flush();
        }
        try (FileInputStream fis = new FileInputStream(tempDir.resolve("test.txt").toString())) {
            int b = b(fis);
            assertEquals(100, b);
            int s = s(fis);
            assertEquals(500, s);
        }

    }


}