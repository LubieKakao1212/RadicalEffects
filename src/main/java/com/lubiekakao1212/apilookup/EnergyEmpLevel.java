package com.lubiekakao1212.apilookup;

import com.lubiekakao1212.util.EmpLevelStats;
import com.lubiekakao1212.util.EmpLevelUtil;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyItem;

import java.util.List;

import static com.lubiekakao1212.util.RadicalUtil.toShortString;

public class EnergyEmpLevel implements IEmpLevel {

    public static final NbtKey<Long> ENERGY_KEY = new NbtKey<>(SimpleEnergyItem.ENERGY_KEY, NbtKey.Type.LONG);

    private final ItemStack stack;

    public EnergyEmpLevel(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public void addTooltip(List<Text> lines) {
        IEmpLevel.super.addTooltip(lines);

        var energy = stack.get(ENERGY_KEY);
        var levelStats = new EmpLevelStats(energy);
        lines.add(Text.translatable("item.radical-effects.emp.charge",
                toShortString(energy),
                toShortString(levelStats.nextLevelEnergy)
        ).formatted(Formatting.GOLD));
    }

    @Override
    public long getLevel() {
        return EmpLevelUtil.levelFromEnergy(stack.get(ENERGY_KEY));
    }
}
