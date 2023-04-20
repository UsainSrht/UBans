package me.usainsrht.ubans;

import java.util.UUID;

public class TemporaryPunishment extends Punishment {
    private long duration;
    private long end;

    public TemporaryPunishment(UUID uuid, PunishmentType type, UUID staff, String reason, long start, long duration, long end) {
        super(uuid, type, staff, reason, start);
        this.duration = duration;
        this.end = end;
    }

    public long getDuration() {
        return duration;
    }

    public long getEnd() {
        return end;
    }

    @Override
    public boolean isTemp() {
        return true;
    }
}
