package org.aarquelle.probenplan_pa.data.dao;

import org.intellij.lang.annotations.Language;

import java.sql.Connection;
import java.sql.SQLException;

public class DBManager extends AbstractDAO {

    public DBManager(Connection conn) {
        super(conn);
    }

    /**
     * Initialize the database, create tables if they don't exist.
     * This should be safe to call multiple times.
     */
    public void initDB() {
        executeUpdate(
                """
                        create table if not exists actors (
                            actor_id INTEGER primary key,
                            actor_name varchar(30) not null
                        )
                        """
        );
        executeUpdate(
                """
                        create table if not exists roles (
                            role_id INTEGER primary key,
                            role_name varchar(30) not null,
                            actor_id INTEGER references actors(actor_id)
                        )
                        """
        );
        executeUpdate(
                """
                        create table if not exists scenes (
                            scene_id INTEGER primary key,
                            scene_name varchar(30) not null,
                            length INTEGER not null,
                            position INTEGER not null
                        )
                        """
        );
        executeUpdate(
                """
                        create table if not exists rehearsals (
                            day date primary key
                        )
                        """
        );
        executeUpdate(
                """
                        create table if not exists has_time (
                            day date references rehearsals,
                            actor_id INTEGER references actors
                        )
                        """
        );
        executeUpdate(
                """
                        create table if not exists plays_in (
                            actor_id INTEGER references actors,
                            scene_id INTEGER references scenes
                        )
                        """
        );
    }

    public void clearDB() {
        executeUpdate("drop table if exists plays_in;");
        executeUpdate("drop table if exists has_time;");
        executeUpdate("drop table if exists rehearsals;");
        executeUpdate("drop table if exists scenes;");
        executeUpdate("drop table if exists roles;");
        executeUpdate("drop table if exists actors;");
    }

    private void executeUpdate(@Language("SQL") String sql) {
        try {
            conn.prepareStatement(sql).executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
