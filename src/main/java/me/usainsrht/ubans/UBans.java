package me.usainsrht.ubans;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.SQLException;

public final class UBans extends JavaPlugin {
    private static UBans instance;
    private boolean remote;
    private Database database;
    private PunishmentManager punishmentManager;
    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        this.remote = getConfig().getBoolean("storage.remote");
        if (remote) {
            this.database = new Database();
            try {
                database.createDefault();
            }
            catch (SQLException e) {
                getLogger().severe("couldn't create tables");
                e.printStackTrace();
            }
        }
        else {
            //handle the file storage
        }
        this.punishmentManager = new PunishmentManager();
    }

    @Override
    public void onDisable() {

    }

    public static UBans getInstance() {
        return instance;
    }

    public Database getBansDatabase() {
        return database;
    }

    public PunishmentManager getPunishmentManager() {
        return punishmentManager;
    }
}
