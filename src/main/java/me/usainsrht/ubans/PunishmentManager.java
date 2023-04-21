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

    public int getPunishmentsCount(UUID uuid) {
        int count = 0;
        ResultSet resultSet = UBans.getInstance().getBansDatabase().runSQLAndGet(SQLCommands.SELECT_UUID_PUNISHMENTS_COUNT, uuid.toString());
        try {
            while (resultSet.next()) {
                count += resultSet.getInt("count");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    public int getHistoryCount(UUID uuid) {
        int count = 0;
        ResultSet resultSet = UBans.getInstance().getBansDatabase().runSQLAndGet(SQLCommands.SELECT_UUID_HISTORY_COUNT, uuid.toString());
        try {
            while (resultSet.next()) {
                count += resultSet.getInt("count");
            }
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
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
                PunishmentType.TEMP_BAN.toString(),
                reason,
                staff.toString(),
                System.currentTimeMillis(),
                duration,
                System.currentTimeMillis() + duration
        );
        queueTempPunishment(temporaryPunishment);
    }

    public void unban(UUID uuid, UUID staff) {
        try {
            int id;
            OfflinePlayer staffPlayer = UUIDUtil.getOfflinePlayer(staff);
            Database db = UBans.getInstance().getBansDatabase();
            ResultSet resultSet;
            resultSet = db.runSQLAndGet(SQLCommands.SELECT_PUNISHMENT, uuid.toString(), PunishmentType.BAN.toString());
            if (!resultSet.isBeforeFirst()) {
                resultSet = db.runSQLAndGet(SQLCommands.SELECT_PUNISHMENT, uuid.toString(), PunishmentType.TEMP_BAN.toString());
            }
            if (!resultSet.isBeforeFirst()) {
                if (staffPlayer.isOnline()) {
                    String unbanned = UBans.getInstance().getConfig().getString("messages.unban.not_banned");
                    unbanned = MessageUtil.parseColor(unbanned);
                    unbanned = unbanned.replace("<player>", UUIDUtil.getName(uuid));
                    MessageUtil.send((Player)staffPlayer, unbanned);
                }
                return;
            }
            resultSet.next();
            id = resultSet.getInt("id");
            db.runSQL(SQLCommands.DELETE_PUNISHMENT, id);
            db.runSQL(SQLCommands.CREATE_HISTORY,
                    resultSet.getString("uuid"),
                    resultSet.getString("type"),
                    resultSet.getString("reason"),
                    resultSet.getString("staff"),
                    resultSet.getString("start"),
                    resultSet.getString("duration"),
                    resultSet.getString("end")
            );
            if (staffPlayer.isOnline()) {
                String unbanned = UBans.getInstance().getConfig().getString("messages.unban.success");
                unbanned = MessageUtil.parseColor(unbanned);
                unbanned = unbanned.replace("<player>", UUIDUtil.getName(uuid));
                MessageUtil.send((Player)staffPlayer, unbanned);
            }
        }
        catch (SQLException | NullPointerException e) {
            UBans.getInstance().getLogger().severe("An error occured while unbanning");
            e.printStackTrace();
        }
    }

    public void unmute(UUID uuid, UUID staff) {

    }

    public void mute(UUID uuid, UUID staff, String reason) {
        UBans.getInstance().getLogger().info(UUIDUtil.getName(staff) + " muted " + UUIDUtil.getName(uuid));
    }

    public void tempMute(UUID uuid, UUID staff, String reason, long duration) {
        UBans.getInstance().getLogger().info(UUIDUtil.getName(staff) + " muted " + UUIDUtil.getName(uuid)
                + " for " + duration + "ms");
    }

    public void queueTempPunishments() {
        List<Punishment> punishments = new ArrayList<>();
        punishments.addAll(getPunishments(SQLCommands.SELECT_EVERY_PUNISHMENT_WITH_TYPE, PunishmentType.TEMP_BAN.toString()));
        punishments.addAll(getPunishments(SQLCommands.SELECT_EVERY_PUNISHMENT_WITH_TYPE, PunishmentType.TEMP_MUTE.toString()));
        punishments.forEach(punishment -> queueTempPunishment((TemporaryPunishment)punishment));
    }

    public void queueTempPunishment(TemporaryPunishment punishment) {
        switch (punishment.getType()) {
            case TEMP_BAN:
                Bukkit.getScheduler().runTaskLater(UBans.getInstance(), () -> {
                    unban(punishment.getUuid(), UUID.fromString("00000000-0000-0000-0000-000000000000"));
                }, punishment.getDuration() / 50);
                break;
            case TEMP_MUTE:
                Bukkit.getScheduler().runTaskLater(UBans.getInstance(), () -> {
                    unmute(punishment.getUuid(), UUID.fromString("00000000-0000-0000-0000-000000000000"));
                }, punishment.getDuration() / 50);
                break;
        }
    }
}
