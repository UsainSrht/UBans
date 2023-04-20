package me.usainsrht.ubans.util;

import java.time.Duration;
import java.time.Instant;
import java.time.Period;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationUtil {
    private static final Pattern periodPattern = Pattern.compile("([0-9]+)(mo|[smhdwy])");
    public static long getDurationAsMilliseconds(String duration) {
        duration = duration.toLowerCase(Locale.ENGLISH);
        Matcher matcher = periodPattern.matcher(duration);
        Instant instant = Instant.EPOCH;
        while(matcher.find()){
            int num = Integer.parseInt(matcher.group(1));
            String typ = matcher.group(2);
            switch (typ) {
                case "s":
                    instant=instant.plus(Duration.ofSeconds(num));
                    break;
                case "m":
                    instant=instant.plus(Duration.ofMinutes(num));
                    break;
                case "h":
                    instant=instant.plus(Duration.ofHours(num));
                    break;
                case "d":
                    instant=instant.plus(Duration.ofDays(num));
                    break;
                case "w":
                    instant=instant.plus(Period.ofWeeks(num));
                    break;
                case "mo":
                    instant=instant.plus(Period.ofMonths(num));
                    break;
                case "y":
                    instant=instant.plus(Period.ofYears(num));
                    break;
            }
        }
        return instant.toEpochMilli();

    }
    public static int getDurationAsTicks(String duration) {
        return (int)(getDurationAsMilliseconds(duration) / 1000 * 20);
    }

    public static String getDurationAsString(long milliseconds) {
        String timeString = "";
        int day = 0;
        int hour = 0;
        int minute = 0;
        int seconds = (int)(milliseconds / 1000);
        if (seconds > 86400 -1) {
            day = Math.floorDiv(seconds, 86400);
            seconds -= day * 86400;
            timeString = timeString + hour + " day";
            if (day > 1) timeString = timeString + "s";
        }
        if (seconds > 3600 - 1) {
            hour = Math.floorDiv(seconds, 3600);
            seconds -= hour * 3600;
            if (day > 0) timeString = timeString + " ";
            timeString = timeString + hour + " hour";
            if (hour > 1) timeString = timeString + "s";
        }
        if (seconds > 60 - 1) {
            minute = Math.floorDiv(seconds, 60);
            seconds -= minute * 60;
            if (hour > 0) timeString = timeString + " ";
            timeString = timeString + minute + " minute";
            if (minute > 1) timeString = timeString + "s";
        }
        if (seconds > 0) {
            if (minute > 0) timeString = timeString + " ";
            timeString = timeString + seconds + " second";
            if (seconds > 1) timeString = timeString + "s";
        }
        return timeString;
    }
}
