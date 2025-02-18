package org.aarquelle.probenplan_pa;

import org.aarquelle.probenplan_pa.data.dao.DBManager;
import org.aarquelle.probenplan_pa.data.dao.Transaction;

public class Main {
    public static boolean TEST_MODE = true;
    public static String URL = "test.db";

    public static void main(String[] args) {
        Transaction.onStartup();
        try (Transaction t = new Transaction()) {
            DBManager dbManager = t.getDBManager();
            dbManager.initDB();
            t.commit();
        }
    }
}