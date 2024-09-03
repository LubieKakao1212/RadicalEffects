package com.lubiekakao1212.item;

import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import team.reborn.energy.api.base.SimpleEnergyItem;

import java.util.List;

public abstract class EmpChargeItem extends Item {

    public EmpChargeItem(Settings settings) {
        super(settings);
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        var level = getLevel(stack);

        tooltip.add(Text.translatable("item.radical-effects.emp.level", level).formatted(Formatting.AQUA));

        super.appendTooltip(stack, world, tooltip, context);
    }

    public abstract long getLevel(ItemStack stack);
}
