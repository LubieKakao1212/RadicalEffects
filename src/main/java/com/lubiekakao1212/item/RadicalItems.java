package com.lubiekakao1212.item;

import io.wispforest.owo.itemgroup.OwoItemSettings;
import io.wispforest.owo.registration.reflect.ItemRegistryContainer;
import net.minecraft.item.Item;

public class RadicalItems implements ItemRegistryContainer {

    public static final Item EMP_ARROW = new EmpArrowItem(new OwoItemSettings());

    public static final Item EMP_CRYSTAL = new EmpCrystalItem(new OwoItemSettings().maxCount(1));
    public static final Item EMP_SHARD = new Item(new OwoItemSettings().maxCount(64));

}
