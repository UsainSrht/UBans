package me.usainsrht.ubans;

import java.util.UUID;

public class TemporaryPunishment extends Punishment {
    private String duration;
    private long end;

    public TemporaryPunishment(UUID uuid, PunishmentType type, UUID staff, String reason, long start, String duration, long end) {
        super(uuid, type, staff, reason, start);
        this.duration = duration;
        this.end = end;
    }

    public String getDuration() {
        return duration;
    }

    public long getEnd() {
        return end;
    }
}
