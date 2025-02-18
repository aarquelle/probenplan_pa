package org.aarquelle.probenplan_pa.data.dao;

import java.sql.Connection;

public class AbstractDAO {
    Connection conn;

    public AbstractDAO(Connection conn) {
        this.conn = conn;
    }
}
