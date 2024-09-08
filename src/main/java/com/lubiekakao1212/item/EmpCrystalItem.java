package com.lubiekakao1212.item;

import com.lubiekakao1212.util.EmpLevelUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import team.reborn.energy.api.base.SimpleEnergyItem;

public class EmpCrystalItem extends Item implements SimpleEnergyItem {

    public EmpCrystalItem(Settings settings) {
        super(settings);
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