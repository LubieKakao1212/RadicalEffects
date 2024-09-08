package com.lubiekakao1212.apilookup;

import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.item.ItemStack;

public class DirectEmpLevel implements IMutableEmpLevel {

    public static final NbtKey<Long> LEVEL_KEY = new NbtKey<>("empLevel", NbtKey.Type.LONG);

    private final ItemStack stack;

    public DirectEmpLevel(ItemStack stack) {
        this.stack = stack;
    }

    @Override
    public long getLevel() {
        return stack.get(LEVEL_KEY);
    }

    @Override
    public void setLevel(long level) {
        stack.put(LEVEL_KEY, level);
    }
}
