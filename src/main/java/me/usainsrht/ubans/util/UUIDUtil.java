package me.usainsrht.ubans.util;

import org.bukkit.Bukkit;

import java.util.UUID;

public class UUIDUtil {

    public static UUID getUUID(String name) {
        return Bukkit.getOfflinePlayer(name).getUniqueId();
    }

    public static String getName(UUID uuid) {
        return Bukkit.getOfflinePlayer(uuid).getName();
    }
}
