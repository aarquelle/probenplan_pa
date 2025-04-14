package org.aarquelle.probenplan_pa;

import org.aarquelle.probenplan_pa.data.dao.Transaction;
import org.aarquelle.probenplan_pa.dto.PlanDTO;
import org.aarquelle.probenplan_pa.ui.cli.CommandLineUI;

import java.nio.file.Path;

public class Main {
    public static boolean TEST_MODE = false;
    public static String URL = "/home/aaron/probenplan_pa/test.db";
    public static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();
    public static final Os OS = getOs();
    public static PlanDTO plan = null;

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

    private static Os getOs() {
        String name = System.getProperty("os.name").toLowerCase();
        if (name.contains("win")) {
            return Os.WINDOWS;
        } else if (name.contains("mac")) {
            return Os.MAC;
        } else if (name.contains("nix") || name.contains("nux")) {
            return Os.LINUX;
        } else {
            throw new UnsupportedOperationException("Unsupported operating system: " + name);
        }
    }
}