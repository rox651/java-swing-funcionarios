package com.uidea.rrhh.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {

    private Database() {
    }

    public static Connection getConnection() throws SQLException {
        String url = Config.getDbUrl();
        String user = Config.getDbUser();
        String pass = Config.getDbPassword();
        return DriverManager.getConnection(url, user, pass);
    }
}
