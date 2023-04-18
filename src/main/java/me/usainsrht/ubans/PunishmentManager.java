package me.usainsrht.ubans;

import java.util.UUID;

public class PunishmentManager {

    public boolean isMuted(UUID uuid) {
        return false;
    }

    public boolean isBanned(UUID uuid) {
        return false;
    }

    public void ban(UUID uuid, UUID staff, String reason) {

    }

    public void tempBan(UUID uuid, UUID staff, String reason, String duration) {

    }

    public void mute(UUID uuid, UUID staff, String reason) {

    }

    public void tempMute(UUID uuid, UUID staff, String reason, String duration) {

    }
}
