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
                            actor_name varchar(30) not null unique
                        )
                        """
        );
        executeUpdate(
                """
                        create table if not exists roles (
                            role_id INTEGER primary key,
                            role_name varchar(30) not null unique ,
                            actor_id INTEGER,
                            foreign key (actor_id) references actors(actor_id) on delete set null
                        )
                        """
        );
        executeUpdate(
                """
                        create table if not exists scenes (
                            scene_id INTEGER primary key,
                            scene_name varchar(30) not null unique ,
                            length DOUBLE not null,
                            position DOUBLE not null unique
                        )
                        """
        );
        executeUpdate(
                """
                        create table if not exists rehearsals (
                            rehearsal_id INTEGER primary key,
                            day date unique not null
                        )
                        """
        );
        executeUpdate(
                """
                        create table if not exists has_no_time (
                            rehearsal_id INTEGER references rehearsals not null,
                            actor_id INTEGER references actors not null,
                            maybe BOOLEAN not null,
                            CONSTRAINT unq unique (rehearsal_id, actor_id)
                        )
                        """
        );
        executeUpdate(
                """
                        create table if not exists plays_in (
                            role_id INTEGER references roles not null,
                            scene_id INTEGER references scenes not null,
                            minor BOOLEAN not null,
                            CONSTRAINT unq unique (role_id, scene_id)
                        )
                        """
        );
    }

    public void clearDB() {
        executeUpdate("drop table if exists plays_in;");
        executeUpdate("drop table if exists has_no_time;");
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
