package com.lubiekakao1212.apilookup;

import com.lubiekakao1212.RadicalEffects;
import com.lubiekakao1212.item.RadicalItems;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public interface IEmpLevel {

    ItemApiLookup<IEmpLevel, Void> ITEM = ItemApiLookup.get(new Identifier(RadicalEffects.MODID, "emp_level"), IEmpLevel.class, Void.class);

    long getLevel();

    default void addTooltip(List<Text> lines) {
        lines.add(Text.translatable("item.radical-effects.emp.level", getLevel()).formatted(Formatting.AQUA));
    }

    static void init() {
        ITEM.registerForItems((itemStack, context) -> new DirectEmpLevel(itemStack), RadicalItems.EMP_SHARD, RadicalItems.EMP_ARROW);
        ITEM.registerForItems((itemStack, context) -> new EnergyEmpLevel(itemStack), RadicalItems.EMP_CRYSTAL);
    }

}
