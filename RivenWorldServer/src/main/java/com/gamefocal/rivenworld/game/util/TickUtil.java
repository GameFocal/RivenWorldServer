package com.gamefocal.rivenworld.game.util;

public class TickUtil {

    public static Long MILLISECONDS(long milliseconds) {
        return milliseconds / 50;
    }

    public static Long SECONDS(long seconds) {
        return MILLISECONDS(seconds * 1000);
    }

    public static Long MINUTES(long mins) {
        return SECONDS(mins * 60);
    }

    public static Long HOURS(long hours) {
        return MINUTES(hours * 60);
    }

}
