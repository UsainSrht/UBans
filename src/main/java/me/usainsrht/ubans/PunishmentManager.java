package me.usainsrht.ubans;

import me.usainsrht.ubans.util.UUIDUtil;

import java.util.UUID;

public class PunishmentManager {

    public boolean isMuted(UUID uuid) {
        return false;
    }

    public boolean isBanned(UUID uuid) {
        return false;
    }

    public void ban(UUID uuid, UUID staff, String reason) {
        UBans.getInstance().getLogger().info(UUIDUtil.getName(staff) + " banned " + UUIDUtil.getName(uuid)
        + " reason: " + reason);
    }

    public void tempBan(UUID uuid, UUID staff, String reason, long duration) {
        UBans.getInstance().getLogger().info(UUIDUtil.getName(staff) + " banned " + UUIDUtil.getName(uuid)
        + " for " + duration + "ms" + " reason: " + reason);
    }

    public void mute(UUID uuid, UUID staff, String reason) {
        UBans.getInstance().getLogger().info(UUIDUtil.getName(staff) + " muted " + UUIDUtil.getName(uuid));
    }

    public void tempMute(UUID uuid, UUID staff, String reason, long duration) {
        UBans.getInstance().getLogger().info(UUIDUtil.getName(staff) + " muted " + UUIDUtil.getName(uuid)
                + " for " + duration + "ms");
    }
}
