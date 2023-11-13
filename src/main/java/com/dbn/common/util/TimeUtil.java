package com.dbn.common.util;

import lombok.experimental.UtilityClass;

import java.util.concurrent.TimeUnit;

import static com.dbn.common.util.TimeUtil.Millis.*;

@UtilityClass
public class TimeUtil {
    public interface Millis{
        long ONE_SECOND = 1000;
        long TWO_SECONDS = 2 * ONE_SECOND;
        long FIVE_SECONDS = 5 * ONE_SECOND;
        long TEN_SECONDS = 10 * ONE_SECOND;
        long THIRTY_SECONDS = 30 * ONE_SECOND;
        long ONE_MINUTE = 60 * ONE_SECOND;
        long ONE_HOUR = 60 * ONE_MINUTE;
        long THREE_MINUTES = 3 * ONE_MINUTE;
        long FIVE_MINUTES = 5 * ONE_MINUTE;
        long TEN_MINUTES = 10 * ONE_MINUTE;
    }

    public static int getMinutes(int seconds) {
        return seconds / 60;
    }

    public static int getSeconds(int minutes) {
        return minutes * 60;
    }

    public static boolean isOlderThan(long timestamp, long millis) {
        return System.currentTimeMillis() - millis > timestamp;
    }

    public static boolean isOlderThan(long timestamp, long duration, TimeUnit timeUnit) {
        return System.currentTimeMillis() - timeUnit.toMillis(duration) > timestamp;
    }

    public static long millisSince(long start) {
        return System.currentTimeMillis() - start;
    }

    public static long secondsSince(long start) {
        return TimeUnit.MILLISECONDS.toSeconds(millisSince(start));
    }

    public static String presentableDuration(long millis, boolean compact) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        String separator = compact ? " " : " and ";
        if (hours > 0) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(millis - (hours * ONE_HOUR));
            return presentableDuration(hours, "hour", compact) + (minutes > 0 ? separator + presentableDuration(minutes, "minute", compact) : "");
        }

        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        if (minutes > 0) {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(millis - (minutes * ONE_MINUTE));
            return presentableDuration(minutes, "minute", compact) + (seconds > 0 ? separator + presentableDuration(seconds, "second", compact) : "");
        }

        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis);
        if (seconds > 5) {
            long milliseconds = millis - (seconds * ONE_SECOND);
            return presentableDuration(seconds, "second", compact) + (milliseconds > 0 ? separator + presentableDuration(milliseconds, "millisecond", compact) : "");
        }


        return millis + " ms";
    }

    private static String presentableDuration(long value, String unit, boolean compact) {
        String unitToken = "";
        switch (unit) {
            case "hour": unitToken = compact ? "h" : (value > 1 ? unit + "s" : unit); break;
            case "minute": unitToken = compact ? "m" : (value > 1 ? unit + "s" : unit); break;
            case "second": unitToken = compact ? "s" : (value > 1 ? unit + "s" : unit); break;
            case "millisecond": unitToken = "ms"; break;
        }
        String valueToken = value > 1 ? Long.toString(value) : (compact ? "1" : "one");
        String separatorToken = compact ? "" : " ";
        return valueToken + separatorToken + unitToken;
    }
}
