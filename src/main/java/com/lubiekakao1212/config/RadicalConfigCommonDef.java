package com.lubiekakao1212.config;

import com.lubiekakao1212.RadicalEffects;
import io.wispforest.owo.config.annotation.*;

@Modmenu(modId = RadicalEffects.MODID)
@Config(wrapperName = "RadicalConfigCommon", name = "radical-effects")
public class RadicalConfigCommonDef {

    @Nest
    public EmpConfig empConfig = new EmpConfig();

    public static class EmpConfig {
        @SectionHeader(value = "empCost")
        public long levelCostBase = 8192;
        public double levelCostMultiplier = 2;

        @SectionHeader(value = "empDrain")
        @RangeConstraint(min = 0, max = 1, decimalPlaces = 3)
        public double drainRatioPerLevel = 0.025;
        public double drainLinearPerLevel = 20000;
        @RangeConstraint(min = 0, max = 1, decimalPlaces = 2)
        public double drainDistributionFactor = 0.25;
    }
}
