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

import java.io.FileInputStream;
import java.io.IOException;

public class FileUtils {
    static byte[] shortToTwoByte(short s) {
        byte[] bytes = new byte[2];
        bytes[0] = (byte) (s >> 8);
        bytes[1] = (byte) s;
        return bytes;
    }

    static byte[] intToFourBytes(int i) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (i >> 24);
        bytes[1] = (byte) (i >> 16);
        bytes[2] = (byte) (i >> 8);
        bytes[3] = (byte) i;
        return bytes;
    }

    static byte[] floatToFourBytes(float f) {
        int intBits = Float.floatToIntBits(f);
        return intToFourBytes(intBits);
    }

    static byte[] doubleToEightBytes(double d) {
        long longBits = Double.doubleToLongBits(d);
        byte[] bytes = new byte[8];
        bytes[0] = (byte) (longBits >> 56);
        bytes[1] = (byte) (longBits >> 48);
        bytes[2] = (byte) (longBits >> 40);
        bytes[3] = (byte) (longBits >> 32);
        bytes[4] = (byte) (longBits >> 24);
        bytes[5] = (byte) (longBits >> 16);
        bytes[6] = (byte) (longBits >> 8);
        bytes[7] = (byte) longBits;
        return bytes;
    }

    /**
     * Reads a single byte from the file.
     */
    static int b(FileInputStream fis) throws IOException {
        return fis.read();
    }

    static int s(FileInputStream fis) throws IOException {
        return (b(fis) << 8) | b(fis);
    }

    static int i(FileInputStream fis) throws IOException {
        return (b(fis) << 24) | (b(fis) << 16) | (b(fis) << 8) | b(fis);
    }

    static float f(FileInputStream fis) throws IOException {
        int bits = i(fis);
        return Float.intBitsToFloat(bits);
    }

    /**
     * Reads a string from the file. The length of the string is determined by the first two bytes.
     */
    static String str(FileInputStream fis) throws IOException {
        int length = s(fis);
        byte[] bytes = new byte[length];
        if (fis.read(bytes) == -1) {
            throw new IOException("Unexpectedly reached end of file");
        }
        return new String(bytes);
    }
}
