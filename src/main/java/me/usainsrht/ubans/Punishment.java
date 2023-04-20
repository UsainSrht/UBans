package me.usainsrht.ubans;

import java.util.UUID;

public class Punishment {

    private UUID uuid;
    private PunishmentType type;
    private UUID staff;
    private String reason;
    private long start;

    public Punishment(UUID uuid, PunishmentType type, String reason, UUID staff, long start) {
        this.uuid = uuid;
        this.type = type;
        this.staff = staff;
        this.reason = reason;
        this.start = start;
    }

    public UUID getUuid() {
        return uuid;
    }

    public PunishmentType getType() {
        return type;
    }

    public UUID getStaff() {
        return staff;
    }

    public String getReason() {
        return reason;
    }

    public long getStart() {
        return start;
    }

    public boolean isTemp() {
        return false;
    }
}
