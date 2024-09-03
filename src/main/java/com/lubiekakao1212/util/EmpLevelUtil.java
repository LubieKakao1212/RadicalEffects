package com.lubiekakao1212.util;

import com.lubiekakao1212.qulib.math.MathUtilKt;

public class EmpLevelUtil {

    private static final long energyPerLevel = 4096;
    private static final double base = 2;

    public static long levelFromEnergy(long energy) {
        //TODO unhardcode values
        return (long)Math.max((Math.log((double)energy / (energyPerLevel)) / Math.log(base) + 1.0), 0);
    }

    public static long energyForLevel(long level) {
        return (long)Math.pow(base, level - 1) * energyPerLevel;
    }
}
