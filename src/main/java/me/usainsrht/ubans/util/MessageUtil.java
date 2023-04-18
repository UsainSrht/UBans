package me.usainsrht.ubans.util;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MessageUtil {

    public static void send(Player p, String message) {
        p.sendMessage(message);
    }

    public static void send(CommandSender sender, String message) {
        sender.sendMessage(message);
    }

    public static String parseColor(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
