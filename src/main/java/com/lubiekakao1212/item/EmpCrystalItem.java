package com.lubiekakao1212.item;

import com.lubiekakao1212.util.EmpLevelStats;
import com.lubiekakao1212.util.EmpLevelUtil;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyItem;

import java.util.List;

import static com.lubiekakao1212.util.RadicalUtil.*;

public class EmpCrystalItem extends EmpChargeItem implements SimpleEnergyItem {

    public static final NbtKey<Long> ENERGY_KEY = new NbtKey<>(SimpleEnergyItem.ENERGY_KEY, NbtKey.Type.LONG);

    public EmpCrystalItem(Settings settings) {
        super(settings);
    }

    @Override
    public long getLevel(ItemStack stack)  {
        var energy = stack.get(ENERGY_KEY);
        return EmpLevelUtil.levelFromEnergy(energy);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        super.appendTooltip(stack, world, tooltip, context);

        var energy = stack.get(ENERGY_KEY);
        var levelStats = new EmpLevelStats(energy);
        tooltip.add(Text.translatable("item.radical-effects.emp.charge",
                toShortString(energy),
                toShortString(levelStats.nextLevelEnergy)
        ).formatted(Formatting.GOLD));
    }

    /**
     * @param stack Current stack.
     * @return The max energy that can be stored in this item stack (ignoring current stack size).
     */
    @Override
    public long getEnergyCapacity(ItemStack stack) {
        return Long.MAX_VALUE;
    }

    /**
     * @param stack Current stack.
     * @return The max amount of energy that can be inserted in this item stack (ignoring current stack size) in a single operation.
     */
    @Override
    public long getEnergyMaxInput(ItemStack stack) {
        return Long.MAX_VALUE;
    }

    /**
     * @param stack Current stack.
     * @return The max amount of energy that can be extracted from this item stack (ignoring current stack size) in a single operation.
     */
    @Override
    public long getEnergyMaxOutput(ItemStack stack) {
        return 0;
    }
}