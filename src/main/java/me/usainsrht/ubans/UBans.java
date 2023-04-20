package me.usainsrht.ubans;

import me.usainsrht.ubans.command.BanCommand;
import me.usainsrht.ubans.command.CommandHandler;
import me.usainsrht.ubans.listener.LoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

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
        try {
            this.database = new Database(remote);
            database.runSQL(SQLCommands.CREATE_TABLE_PUNISHMENT);
            database.runSQL(SQLCommands.CREATE_TABLE_HISTORY);
        }
        catch (ClassNotFoundException e) {
            getLogger().severe("Couldn't initialize database!");
            e.printStackTrace();
        }
        this.punishmentManager = new PunishmentManager();
        CommandHandler.register(new BanCommand("ban",
                "ban command",
                "/ban",
                new ArrayList<>()));
        getServer().getPluginManager().registerEvents(new LoginEvent(), this);
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
