package com.lubiekakao1212.item;

import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.item.ItemStack;

public class EmpShardItem extends EmpChargeItem {

    public static final NbtKey<Long> LEVEL_KEY = new NbtKey<>("empLevel", NbtKey.Type.LONG);

    public EmpShardItem(Settings settings) {
        super(settings);
    }

    @Override
    public long getLevel(ItemStack stack) {
        return stack.get(LEVEL_KEY);
    }

}
