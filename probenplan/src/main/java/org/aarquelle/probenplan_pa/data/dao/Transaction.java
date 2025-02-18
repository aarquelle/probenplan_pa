/*
 * Copyright (c) 2024, Aaron Prott
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.aarquelle.probenplan_pa.data.dao;


import org.aarquelle.probenplan_pa.Main;
import org.aarquelle.probenplan_pa.data.exception.DBNotAvailableException;
import org.sqlite.SQLiteConfig;

import java.sql.Connection;
import java.sql.SQLException;

public class Transaction implements AutoCloseable {
    private final Connection connection;
    private static final SQLiteConfig config = new SQLiteConfig();

    public static void onStartup() {
        if (Main.TEST_MODE) {
            Main.URL = "test.db";
        }
        config.enforceForeignKeys(true);
    }

    public Transaction() {
        connection = getConnection();
    }

    private Connection getConnection()  {
            try {
                Connection conn = config.createConnection("jdbc:sqlite:" + Main.URL);
                conn.setAutoCommit(false);
                return conn;
            } catch (SQLException e) {
                System.err.println(e.getMessage());
                throw new DBNotAvailableException(e);
            }
    }

    public DBManager getDBManager() {
        return new DBManager(connection);
    }

    public ReadDAO getReadDAO() {
        return new ReadDAO(connection);
    }

    public CreateDAO getCreateDAO() {
        return new CreateDAO(connection);
    }

    public void commit() {
        try {
            connection.commit();
        } catch (SQLException e) {
            throw new DBNotAvailableException(e);
        }
    }

    public void close() {
        try {
            connection.rollback();
            connection.close();
        } catch (SQLException e) {
            throw new DBNotAvailableException(e);
        }
    }
}
