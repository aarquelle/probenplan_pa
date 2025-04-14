package org.aarquelle.probenplan_pa;

import org.aarquelle.probenplan_pa.data.dao.Transaction;
import org.aarquelle.probenplan_pa.dto.ParamsDTO;
import org.aarquelle.probenplan_pa.dto.PlanDTO;
import org.aarquelle.probenplan_pa.ui.cli.CommandLineUI;

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
        return Path.of(System.getProperty("user.home"), "Documents", "Probenplan_PA", "probenplan.db");
    }
}