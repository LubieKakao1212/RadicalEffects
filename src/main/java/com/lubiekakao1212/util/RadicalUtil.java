package com.lubiekakao1212.util;

public class RadicalUtil {

    private static final String[] UNITS = new String[]{
            "",
            "K",
            "M",
            "B",
            "T",
            "Qu",
            "Qt",
            "Sx",
            "Sp",
            "Oc",
            "No",
            "Error",
    };

    private static final String[] SUFFIXES = new String[]{
            "",
            "k",
            "M",
            "G",
            "T",
            "P",
            "E",
            "Z",
            "Error",
    };

    public static String toShortString(long value) {
        int i = 0;
        int j = 1;
        var v = value;
        while(v > 1000) {
            v /= 1000;
            i++;
            j *= 1000;
        }

        var truncated = value / j;
        var str = Long.toString(truncated);
        if(j > 1) {
            var decimal = (value / (j / 10)) % 10;
            return str + "." + decimal + UNITS[i];
        }

        return str;
    }

    public static double log(double base, double arg) {
        return Math.log(arg) / Math.log(base);
    }
}