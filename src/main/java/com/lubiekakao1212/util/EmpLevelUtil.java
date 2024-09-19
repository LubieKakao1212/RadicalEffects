package com.lubiekakao1212.util;

import com.lubiekakao1212.qulib.math.MathUtilKt;

public class EmpLevelUtil {

    private static final long energyPerLevel = 8192;

    //Do NOT change
    private static final long base = 2;

    public static long levelFromEnergy(long energy) {
        //TODO unhardcode values
        //return (long)Math.max((Math.log((double)energy / (energyPerLevel) + 1) / Math.log(base)), 0);
        return (long)Math.max((Math.log((double)energy / (energyPerLevel)) / Math.log(base)) + 1, 0);
    }

    //TODO Works only for base 2
    public static long energyForLevel(long level) {
        //return (long)Math.pow(base, level - 1) * energyPerLevel * base;
        return (long)Math.pow(base, level - 1) * energyPerLevel;
    }
}
