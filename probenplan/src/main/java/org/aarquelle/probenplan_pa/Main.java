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

package org.aarquelle.probenplan_pa;

import org.aarquelle.probenplan_pa.data.dao.Transaction;
import org.aarquelle.probenplan_pa.dto.ParamsDTO;
import org.aarquelle.probenplan_pa.dto.PlanDTO;
import org.aarquelle.probenplan_pa.ui.cli.CommandLineUI;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static boolean TEST_MODE = false;
    public static String URL = getDbFile().toString();
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    public static PlanDTO plan = null;
    public static ParamsDTO params = new ParamsDTO();

    public static void main(String[] args) {
        Transaction.onStartup();
        System.out.println("Opened file: " + Path.of(URL).toAbsolutePath());

        try (Transaction t = new Transaction()) {
            if (TEST_MODE) {
                t.getDBManager().clearDB();
            }
            t.getDBManager().initDB();
            t.commit();
        } catch (Exception e) {
            System.err.println("Error creating database: " + e.getMessage());
        }

        new CommandLineUI().start();
    }

    private static Path getDbFile() {
        Path folder = Path.of(System.getProperty("user.home"), "Documents", "Probenplan_PA");
        try {
            Files.createDirectories(folder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return folder.resolve("probenplan.db");
    }
}