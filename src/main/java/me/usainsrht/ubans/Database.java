package me.usainsrht.ubans;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;

public class Database {
    private HikariConfig config = new HikariConfig();
    private boolean remote;

    public Database(boolean remote) throws ClassNotFoundException {
        this.remote = remote;
        UBans ubans = UBans.getInstance();
        if (remote) {
            FileConfiguration cf = ubans.getConfig();
            String host = cf.getString("storage.connection.host");
            String port = cf.getString("storage.connection.host");
            String name = cf.getString("storage.connection.database");
            String user = cf.getString("storage.connection.username");
            String password = cf.getString("storage.connection.password");
            String properties = cf.getString("storage.connection.properties");
            Class.forName("com.mysql.jdbc.Driver");
            config.setJdbcUrl("jdbc:mysql://" + host + ":" + port + "/" + name + "?"+ properties);
            config.setUsername(user);
            config.setPassword(password);
        } else {
            String driverClassName = "org.hsqldb.jdbc.JDBCDriver";
            Class.forName(driverClassName);
            config.setDriverClassName(driverClassName);
            config.setJdbcUrl("jdbc:hsqldb:file:" + ubans.getDataFolder().getPath() + "/storage;hsqldb.lock_file=false");
            config.setUsername("UB");
            config.setPassword("");
        }
    }

    public HikariDataSource getDataSource(){
        return new HikariDataSource(config);
    }

    public boolean isRemote() {
        return remote;
    }
}
