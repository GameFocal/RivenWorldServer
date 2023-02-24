package com.gamefocal.rivenworld.game.util;

import java.text.DecimalFormat;

public class MathUtil {

    private static char[] c = new char[]{'k', 'm', 'b', 't'};

    public static float map(float value, float inMin, float inMax, float outMin, float outMax) {
        return Math.round(((value - inMin) * (outMax - outMin)) / ((inMax - inMin) + outMin));
    }

    public static String formatToShort(double n) {

        DecimalFormat format = new DecimalFormat("###.#");

        if (n < 1000) {
            return String.valueOf(n);
        } else if (n < 999999) {
            return format.format(n / 1000) + "k";
        } else if (n < 999999999) {
            return format.format(n / 1000000) + "M";
        }

        return String.valueOf(n);
    }

    public static int modInverse(int a, int m) {

        for (int x = 1; x < m; x++)
            if (((a % m) * (x % m)) % m == 1)
                return x;
        return 1;
    }

}
