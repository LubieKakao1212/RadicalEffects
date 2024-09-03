package com.lubiekakao1212.util;

public class EmpLevelStats {

    public final long level;
    public final long currentLevelEnergy;
    public final long nextLevelEnergy;
    public final long nextLevelEnergyDelta;
    public final long currentLeftoverEnergy;

    public EmpLevelStats(long energy) {
        this.level = EmpLevelUtil.levelFromEnergy(energy);

        this.currentLevelEnergy = EmpLevelUtil.energyForLevel(level);
        this.nextLevelEnergy = EmpLevelUtil.energyForLevel(level + 1);

        this.nextLevelEnergyDelta = nextLevelEnergy - currentLevelEnergy;

        currentLeftoverEnergy = energy - currentLevelEnergy;
    }


}
