package org.aarquelle.probenplan_pa;

import org.aarquelle.probenplan_pa.data.dao.Transaction;
import org.aarquelle.probenplan_pa.ui.cli.CommandLineUI;

public class Main {
    public static boolean TEST_MODE = true;
    public static String URL = "test.db";

    public static void main(String[] args) {
        Transaction.onStartup();

        if (TEST_MODE) {
            try (Transaction t = new Transaction()) {
                t.getDBManager().clearDB();
                t.getDBManager().initDB();
                t.commit();
            } catch (Exception e) {
                System.err.println("Error creating database: " + e.getMessage());
            }
        }

        new CommandLineUI().start();
    }
}