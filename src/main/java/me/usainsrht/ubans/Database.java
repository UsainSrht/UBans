package me.usainsrht.ubans;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.configuration.file.FileConfiguration;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Database {
    private HikariConfig config = new HikariConfig();
    private boolean remote;
    private RowSetFactory rowSetFactory;

    public Database(boolean remote) throws ClassNotFoundException {
        this.remote = remote;
        UBans ubans = UBans.getInstance();
        if (remote) {
            FileConfiguration cf = ubans.getConfig();
            String host = cf.getString("storage.connection.host");
            String port = cf.getString("storage.connection.port");
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

    private CachedRowSet getCachedRowSet() throws SQLException {
        if (rowSetFactory == null) {
            rowSetFactory = RowSetProvider.newFactory();
        }
        return rowSetFactory.createCachedRowSet();
    }

    public void runSQL(SQLCommands commands, Object... parameters) {
        runSQL(commands, false, parameters);
    }

    public ResultSet runSQLAndGet(SQLCommands commands, Object... parameters) {
        return runSQL(commands, true, parameters);
    }

    private ResultSet runSQL(SQLCommands commands, boolean result, Object... parameters) {
        return executeStatement(commands.toString(), result, parameters);
    }

    private synchronized ResultSet executeStatement(String command, boolean result, Object... parameters) {
        try (Connection connection = getDataSource().getConnection(); PreparedStatement statement = connection.prepareStatement(command)) {

            for (int i = 0; i < parameters.length; i++) {
                statement.setObject(i + 1, parameters[i]);
            }

            if (result) {
                CachedRowSet results = getCachedRowSet();
                results.populate(statement.executeQuery());
                return results;
            }
            statement.execute();
        } catch (SQLException e) {
            UBans.getInstance().getLogger().severe("an error occured while trying to execute: " + "\n" + command);
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }
}
