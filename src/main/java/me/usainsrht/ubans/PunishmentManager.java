package me.usainsrht.ubans;

import me.usainsrht.ubans.util.MessageUtil;
import me.usainsrht.ubans.util.UUIDUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

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
        punishments.addAll(getPunishments(SQLCommands.SELECT_PUNISHMENT, uuid.toString(), PunishmentType.BAN.toString()));
        punishments.addAll(getPunishments(SQLCommands.SELECT_PUNISHMENT, uuid.toString(), PunishmentType.TEMP_BAN.toString()));
        return punishments.isEmpty() ? null : punishments.get(0);
    }

    public Punishment getPunishmentWithSQLResult(ResultSet resultSet) throws SQLException {
        if (resultSet.getLong("end") == 0) {
            return new Punishment(
                    UUID.fromString(resultSet.getString("uuid")),
                    PunishmentType.valueOf(resultSet.getString("type")),
                    resultSet.getString("reason"),
                    UUID.fromString(resultSet.getString("staff")),
                    resultSet.getLong("start")
            );
        }
        return new TemporaryPunishment(
                UUID.fromString(resultSet.getString("uuid")),
                PunishmentType.valueOf(resultSet.getString("type")),
                resultSet.getString("reason"),
                UUID.fromString(resultSet.getString("staff")),
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
                reason,
                staff,
                System.currentTimeMillis()
        );
        OfflinePlayer offlinePlayer = UUIDUtil.getOfflinePlayer(uuid);
        if (offlinePlayer.isOnline()) {
            ((Player)offlinePlayer).kickPlayer(MessageUtil.getBannedScreen(punishment));
        }
        String chatMessage = MessageUtil.getBannedChatMessage(punishment);
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(chatMessage));
        UBans.getInstance().getBansDatabase().runSQL(SQLCommands.CREATE_PUNISHMENT,
                uuid.toString(),
                PunishmentType.BAN.toString(),
                reason,
                staff.toString(),
                System.currentTimeMillis(),
                0,
                0
                );
    }

    public void tempBan(UUID uuid, UUID staff, String reason, long duration) {
        TemporaryPunishment temporaryPunishment = new TemporaryPunishment(
                uuid,
                PunishmentType.TEMP_BAN,
                reason,
                staff,
                System.currentTimeMillis(),
                duration,
                System.currentTimeMillis() + duration
        );
        OfflinePlayer offlinePlayer = UUIDUtil.getOfflinePlayer(uuid);
        if (offlinePlayer.isOnline()) {
            ((Player)offlinePlayer).kickPlayer(MessageUtil.getBannedScreen(temporaryPunishment));
        }
        String chatMessage = MessageUtil.getBannedChatMessage(temporaryPunishment);
        Bukkit.getOnlinePlayers().forEach(p -> p.sendMessage(chatMessage));
        UBans.getInstance().getBansDatabase().runSQL(SQLCommands.CREATE_PUNISHMENT,
                uuid.toString(),
                PunishmentType.BAN.toString(),
                reason,
                staff.toString(),
                System.currentTimeMillis(),
                duration,
                System.currentTimeMillis() + duration
        );
    }

    public void unban(UUID uuid, UUID staff) {
        Database db = UBans.getInstance().getBansDatabase();
        int id = db.runSQLAndGet(SQLCommands.SELECT_PUNISHMENT, );
                db.runSQL(SQLCommands.DELETE_PUNISHMENT, id);
    }

    public void mute(UUID uuid, UUID staff, String reason) {
        UBans.getInstance().getLogger().info(UUIDUtil.getName(staff) + " muted " + UUIDUtil.getName(uuid));
    }

    public void tempMute(UUID uuid, UUID staff, String reason, long duration) {
        UBans.getInstance().getLogger().info(UUIDUtil.getName(staff) + " muted " + UUIDUtil.getName(uuid)
                + " for " + duration + "ms");
    }
}
