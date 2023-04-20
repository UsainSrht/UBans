package me.usainsrht.ubans;

import java.util.UUID;

public class TemporaryPunishment extends Punishment {
    private long duration;
    private long end;

    public TemporaryPunishment(UUID uuid, PunishmentType type, String reason, UUID staff, long start, long duration, long end) {
        super(uuid, type, reason, staff, start);
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
