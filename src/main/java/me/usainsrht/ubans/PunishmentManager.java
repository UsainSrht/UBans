package me.usainsrht.ubans;

import me.usainsrht.ubans.util.MessageUtil;
import me.usainsrht.ubans.util.UUIDUtil;
import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PunishmentManager {

    public boolean isMuted(UUID uuid) {
        return false;
    }

    public boolean isBanned(UUID uuid) {
        return getBan(uuid) != null;
    }

    public Punishment getBan(UUID uuid) {
        List<Punishment> punishments = new ArrayList<>();
        punishments.addAll(getPunishments(SQLCommands.SELECT_PUNISHMENT, PunishmentType.BAN));
        punishments.addAll(getPunishments(SQLCommands.SELECT_PUNISHMENT, PunishmentType.TEMP_BAN));
        return punishments.isEmpty() ? null : punishments.get(0);
    }

    public Punishment getPunishmentWithSQLResult(ResultSet resultSet) throws SQLException {
        if (resultSet.getLong("end") == 0) {
            return new Punishment(
                    UUID.fromString(resultSet.getString("uuid")),
                    PunishmentType.valueOf(resultSet.getString("type")),
                    UUID.fromString(resultSet.getString("staff")),
                    resultSet.getString("reason"),
                    resultSet.getLong("start")
            );
        }
        return new TemporaryPunishment(
                UUID.fromString(resultSet.getString("uuid")),
                PunishmentType.valueOf(resultSet.getString("type")),
                UUID.fromString(resultSet.getString("staff")),
                resultSet.getString("reason"),
                resultSet.getLong("start"),
                resultSet.getLong("duration"),
                resultSet.getLong("end")
                );
    }

    public List<Punishment> getPunishments(SQLCommands commands, Object... parameters) {
        List<Punishment> ptList = new ArrayList<>();

        ResultSet resultSet = UBans.getInstance().getBansDatabase().runSQLAndGet(commands, parameters);
        try {
            while (resultSet.next()) {
                Punishment punishment = getPunishmentWithSQLResult(resultSet);
                ptList.add(punishment);
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return ptList;
    }
    public void ban(UUID uuid, UUID staff, String reason) {
        Punishment punishment = new Punishment(
                uuid,
                PunishmentType.TEMP_BAN,
                staff,
                reason,
                System.currentTimeMillis()
        );
        List<String> chatMessage = UBans.getInstance().getConfig().getStringList("messages.tempban.chat");
        chatMessage.replaceAll(line -> {
            line = MessageUtil.parseColor(line);
            return MessageUtil.parsePunishment(line, punishment);
        });
        String finalChatMessage = String.join("\n", chatMessage);
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(finalChatMessage));
        UBans.getInstance().getBansDatabase().runSQL(SQLCommands.CREATE_PUNISHMENT,
                uuid.toString(),
                PunishmentType.BAN.toString(),
                staff.toString(),
                reason,
                System.currentTimeMillis(),
                0,
                0
                );
    }

    public void tempBan(UUID uuid, UUID staff, String reason, long duration) {
        TemporaryPunishment temporaryPunishment = new TemporaryPunishment(
                uuid,
                PunishmentType.TEMP_BAN,
                staff,
                reason,
                System.currentTimeMillis(),
                duration,
                System.currentTimeMillis() + duration
        );
        List<String> chatMessage = UBans.getInstance().getConfig().getStringList("messages.tempban.chat");
        chatMessage.replaceAll(line -> {
            line = MessageUtil.parseColor(line);
            return MessageUtil.parsePunishment(line, temporaryPunishment);
        });
        String finalChatMessage = String.join("\n", chatMessage);
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(finalChatMessage));
        UBans.getInstance().getBansDatabase().runSQL(SQLCommands.CREATE_PUNISHMENT,
                uuid.toString(),
                PunishmentType.BAN.toString(),
                staff.toString(),
                reason,
                System.currentTimeMillis(),
                duration,
                System.currentTimeMillis() + duration
        );
    }

    public void mute(UUID uuid, UUID staff, String reason) {
        UBans.getInstance().getLogger().info(UUIDUtil.getName(staff) + " muted " + UUIDUtil.getName(uuid));
    }

    public void tempMute(UUID uuid, UUID staff, String reason, long duration) {
        UBans.getInstance().getLogger().info(UUIDUtil.getName(staff) + " muted " + UUIDUtil.getName(uuid)
                + " for " + duration + "ms");
    }
}
