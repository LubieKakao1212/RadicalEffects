package com.lubiekakao1212.coating;

import com.lubiekakao1212.coating.container.IItemCoatingContainer;
import com.lubiekakao1212.util.ReadOnly;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.world.World;

import java.util.EnumSet;

public class FireCoating implements ICoating {

//    private static final NbtKey<Integer> TIME_KEY = new NbtKey<>("time", NbtKey.Type.INT);

    public int time;

    public FireCoating(int time) {
        this.time = time;
    }

    /**
     * Used to apply effects
     *
     * @param usage
     * @param instance  can be modified
     * @param container
     * @param world
     */
    @Override
    public <T> void affectTarget(CoatingUsage<T> usage, ReadOnly<CoatingInstance> instance, IItemCoatingContainer container, World world) {
        usage.asEntity().ifPresent(entityUsage -> {
//            var coating = instance.value();
//            var time = coating.get(TIME_KEY);
            entityUsage.target.setOnFireFor(time);
        });
    }

}
