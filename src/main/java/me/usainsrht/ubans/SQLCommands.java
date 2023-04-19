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
                    "end BIGINT,"
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

            "CREATE TABLE IF NOT EXISTS punishments (" +
                    "id INTEGER IDENTITY PRIMARY KEY," +
                    "uuid VARCHAR(36)," +
                    "type VARCHAR(64)," +
                    "reason VARCHAR(255)," +
                    "staff VARCHAR(36)," +
                    "start BIGINT," +
                    "duration BIGINT," +
                    "end BIGINT,"
    ),
    CREATE_PUNISHMENT(
            "INSERT INTO `punishments` " +
                    "(`uuid`, `reason`, `staff`, `type`, `start`, `duration`, `end`) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)",

            "INSERT INTO punishments " +
                    "(uuid, reason, staff, type, start, duration, end) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)"
    ),
    CREATE_HISTORY(
            "INSERT INTO `history` " +
                    "(`name`, `uuid`, `reason`, `staff`, `type`, `start`, `end`, `calculation`) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)",

            "INSERT INTO history " +
                    "(name, uuid, reason, staff, type, start, end, calculation) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)"
    ),
    SELECT_PUNISHMENT(
            "SELECT * FROM `punishments` WHERE `uuid` = ? AND `start` = ? AND `type` = ?",
            "SELECT * FROM punishments WHERE uuid = ? AND start = ? AND type = ?"
    ),
    DELETE_PUNISHMENT(
            "DELETE FROM `punishments` WHERE `id` = ?",
            "DELETE FROM punishments WHERE id = ?"
    ),
    DELETE_OLD_PUNISHMENTS(
            "DELETE FROM `punishments` WHERE `end` <= ? AND `end` != -1",
            "DELETE FROM punishments WHERE end <= ? AND end != -1"
    ),
    SELECT_UUID_PUNISHMENTS(
            "SELECT * FROM `punishments` WHERE `uuid` = ?",
            "SELECT * FROM punishments WHERE uuid = ?"
    ),
    SELECT_UUID_HISTORY(
            "SELECT * FROM `history` WHERE `uuid` = ?",
            "SELECT * FROM history WHERE uuid = ?"
    ),
    SELECT_PUNISHMENT_WITH_ID(
            "SELECT * FROM `punishments` WHERE `id` = ?",
            "SELECT * FROM punishments WHERE id = ?"
    ),
    SELECT_EVERY_PUNISHMENTS(
            "SELECT * FROM `punishments`",
            "SELECT * FROM punishments"
    ),
    SELECT_EVERY_HISTORY(
            "SELECT * FROM `history`",
            "SELECT * FROM history"
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
