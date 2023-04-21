package me.usainsrht.ubans;

public enum SQLCommands {

    CREATE_TABLE_PUNISHMENT(
            "CREATE TABLE IF NOT EXISTS `punishments` ("+
                    "`id` int NOT NULL AUTO_INCREMENT," +
                    "`uuid` VARCHAR(36) NULL DEFAULT NULL," +
                    "`type` VARCHAR(64) NULL DEFAULT NULL," +
                    "`reason` VARCHAR(255) NULL DEFAULT NULL," +
                    "`staff` VARCHAR(36) NULL DEFAULT NULL," +
                    "`start` LONG DEFAULT NULL," +
                    "`duration` LONG DEFAULT NULL," +
                    "`end` LONG DEFAULT NULL," +
                    "PRIMARY KEY (`id`))",

            "CREATE TABLE IF NOT EXISTS punishments (" +
                    "id INTEGER IDENTITY PRIMARY KEY," +
                    "uuid VARCHAR(36)," +
                    "type VARCHAR(64)," +
                    "reason VARCHAR(255)," +
                    "staff VARCHAR(36)," +
                    "start BIGINT," +
                    "duration BIGINT," +
                    "end BIGINT)"
    ),
    CREATE_TABLE_HISTORY(
            "CREATE TABLE IF NOT EXISTS `history` (" +
                    "`id` int NOT NULL AUTO_INCREMENT," +
                    "`uuid` VARCHAR(36) NULL DEFAULT NULL," +
                    "`type` VARCHAR(64) NULL DEFAULT NULL," +
                    "`reason` VARCHAR(255) NULL DEFAULT NULL," +
                    "`staff` VARCHAR(36) NULL DEFAULT NULL," +
                    "`start` LONG DEFAULT NULL," +
                    "`duration` LONG DEFAULT NULL," +
                    "`end` LONG DEFAULT NULL," +
                    "PRIMARY KEY (`id`))",

            "CREATE TABLE IF NOT EXISTS history (" +
                    "id INTEGER IDENTITY PRIMARY KEY," +
                    "uuid VARCHAR(36)," +
                    "type VARCHAR(64)," +
                    "reason VARCHAR(255)," +
                    "staff VARCHAR(36)," +
                    "start BIGINT," +
                    "duration BIGINT," +
                    "end BIGINT)"
    ),
    CREATE_PUNISHMENT(
            "INSERT INTO `punishments` " +
                    "(`uuid`, `type`, `reason`, `staff`, `start`, `duration`, `end`) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)",

            "INSERT INTO punishments " +
                    "(uuid, type, reason, staff, start, duration, end) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)"
    ),
    CREATE_HISTORY(
            "INSERT INTO `history` " +
                    "(`uuid`, `type`, `reason`, `staff`, `start`, `duration`, `end`) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)",

            "INSERT INTO history " +
                    "(uuid, type, reason, staff, start, duration, end) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)"
    ),
    SELECT_PUNISHMENT(
            "SELECT * FROM `punishments` WHERE `uuid` = ? AND `type` = ?",
            "SELECT * FROM punishments WHERE uuid = ? AND type = ?"
    ),
    DELETE_PUNISHMENT(
            "DELETE FROM `punishments` WHERE `id` = ?",
            "DELETE FROM punishments WHERE id = ?"
    ),
    SELECT_UUID_PUNISHMENTS_WITH_LIMIT_AND_OFFSET(
            "SELECT * FROM `punishments` WHERE `uuid` = ? LIMIT ? OFFSET ?",
            "SELECT * FROM punishments WHERE uuid = ? LIMIT ? OFFSET ?"
    ),
    SELECT_UUID_HISTORY_WITH_LIMIT_AND_OFFSET(
            "SELECT * FROM `history` WHERE `uuid` = ? LIMIT ? OFFSET ?",
            "SELECT * FROM history WHERE uuid = ? LIMIT ? OFFSET ?"
    ),
    SELECT_UUID_PUNISHMENTS_COUNT(
            "SELECT COUNT(id) as count FROM `punishments` WHERE `uuid` = ?",
            "SELECT COUNT(id) as count FROM punishments WHERE uuid = ?"
    ),
    SELECT_UUID_HISTORY_COUNT(
            "SELECT COUNT(id) as count FROM `history` WHERE `uuid` = ?",
            "SELECT COUNT(id) as count FROM history WHERE uuid = ?"
    ),
    SELECT_EVERY_PUNISHMENT_WITH_TYPE(
            "SELECT * FROM `punishments` WHERE `type` = ?",
            "SELECT * FROM punishments WHERE type = ?"
    );
    private String mysql;
    private String hsql;

    SQLCommands(String mysql, String hsql) {
        this.mysql = mysql;
        this.hsql = hsql;
    }

    @Override
    public String toString() {
        return UBans.getInstance().getBansDatabase().isRemote() ? mysql : hsql;
    }
}
