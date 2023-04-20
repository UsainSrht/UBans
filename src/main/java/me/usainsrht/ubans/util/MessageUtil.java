package me.usainsrht.ubans.util;

import me.usainsrht.ubans.Punishment;
import me.usainsrht.ubans.TemporaryPunishment;
import me.usainsrht.ubans.UBans;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

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

    public static String parsePunishment(String message, Punishment punishment) {
        message = message.replace("<player>", UUIDUtil.getName(punishment.getUuid()));
        message = message.replace("<staff>", UUIDUtil.getName(punishment.getStaff()));
        message = message.replace("<reason>", punishment.getReason());
        if (punishment.isTemp()) {
            TemporaryPunishment temporaryPunishment = (TemporaryPunishment)punishment;
            message = message.replace("<duration>", DurationUtil.getDurationAsString(temporaryPunishment.getDuration()));
            String durationLeft = DurationUtil.getDurationAsString(temporaryPunishment.getEnd() - System.currentTimeMillis());
            message = message.replace("<duration_left>", durationLeft);
        }
        return message;
    }

    public static String getBannedScreen(Punishment punishment) {
        List<String> screen;
        if (punishment.isTemp()) {
            screen = UBans.getInstance().getConfig().getStringList("messages.tempban.screen");
        }
        else {
            screen = UBans.getInstance().getConfig().getStringList("messages.ban.screen");
        }
        screen.replaceAll(line -> {
            line = MessageUtil.parseColor(line);
            return MessageUtil.parsePunishment(line, punishment);
        });
        return String.join("\n", screen);
    }

    public static String getBannedChatMessage(Punishment punishment) {
        List<String> message;
        if (punishment.isTemp()) {
            message = UBans.getInstance().getConfig().getStringList("messages.tempban.chat");
        }
        else {
            message = UBans.getInstance().getConfig().getStringList("messages.ban.chat");
        }
        message.replaceAll(line -> {
            line = MessageUtil.parseColor(line);
            return MessageUtil.parsePunishment(line, punishment);
        });
        return String.join("\n", message);
    }
}
