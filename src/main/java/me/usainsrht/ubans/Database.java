package me.usainsrht.ubans;

import org.bukkit.configuration.file.FileConfiguration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection connection;

    public Connection getConnection() throws SQLException {
        if (connection != null) {
            return connection;
        }

        FileConfiguration config = UBans.getInstance().getConfig();
        String host = config.getString("mysql.host");
        String port = config.getString("mysql.port");
        String name = config.getString("mysql.database");
        String url = "jdbc:mysql://" + host + ":" + port + "/" + name;
        String user = config.getString("mysql.username");
        String password = config.getString("mysql.password");

        Connection connection = DriverManager.getConnection(url, user, password);

        this.connection = connection;

        return connection;
    }

    public void createDefault() throws SQLException {

        Statement statement = getConnection().createStatement();

        String createPunishments = "CREATE TABLE IF NOT EXISTS punishments (uuid varchar(36) primary key, type varchar(64), start long, duration varchar(64), end long, staff varchar(36), reason varchar(255))";
        String createHistory = "CREATE TABLE IF NOT EXISTS punishments (uuid varchar(36) primary key, type varchar(64), start long, duration varchar(64), end long, staff varchar(36), reason varchar(255))";

        statement.execute(createPunishments);
        statement.execute(createHistory);

        statement.close();

    }



}
