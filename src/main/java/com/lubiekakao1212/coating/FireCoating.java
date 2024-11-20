package com.lubiekakao1212.coating;

import com.lubiekakao1212.util.ReadOnly;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtString;
import net.minecraft.potion.Potion;
import net.minecraft.world.World;

import java.util.EnumSet;

public class FireCoating implements ICoating {

    private static final NbtKey<Integer> TIME_KEY = new NbtKey<>("time", NbtKey.Type.INT);

    /**
     * Used to apply effects
     *
     * @param usage
     * @param instance  can be modified
     * @param container
     * @param world
     */
    @Override
    public <T, C> void affectTarget(CoatingUsage<T> usage, ReadOnly<CoatingInstance> instance, ICoatingContainer<C> container, World world) {
        usage.asEntity().ifPresent(entityUsage -> {
            var coating = instance.value();
            var time = coating.get(TIME_KEY);
            entityUsage.target.setOnFireFor(time);
        });
    }

    @Override
    public EnumSet<Designation> getDesignation() {
        return EnumSet.of(Designation.ITEM, Designation.ENTITY, Designation.ITEM_FOOD);
    }

}
