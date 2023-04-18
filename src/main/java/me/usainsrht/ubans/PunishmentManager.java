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
        UBans.getInstance().getLogger().info(UUIDUtil.getName(staff) + " banned " + UUIDUtil.getName(uuid));
    }

    public void tempBan(UUID uuid, UUID staff, String reason, long duration) {
        UBans.getInstance().getLogger().info(UUIDUtil.getName(staff) + " banned " + UUIDUtil.getName(uuid)
        + " for " + duration + "ms");
    }

    public void mute(UUID uuid, UUID staff, String reason) {
        UBans.getInstance().getLogger().info(UUIDUtil.getName(staff) + " muted " + UUIDUtil.getName(uuid));
    }

    public void tempMute(UUID uuid, UUID staff, String reason, long duration) {
        UBans.getInstance().getLogger().info(UUIDUtil.getName(staff) + " muted " + UUIDUtil.getName(uuid)
                + " for " + duration + "ms");
    }
}
