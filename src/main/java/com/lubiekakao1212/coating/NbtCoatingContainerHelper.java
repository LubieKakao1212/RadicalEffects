package com.lubiekakao1212.coating;

import com.lubiekakao1212.util.ReadOnly;
import io.wispforest.owo.nbt.NbtCarrier;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class NbtCoatingContainerHelper {

    public static final NbtKey<NbtList> COATINGS_KEY = new NbtKey.ListKey<>("coatings", NbtKey.Type.COMPOUND);

    private final List<CoatingInstance> coatingCache = new ArrayList<>();

    private NbtCoatingContainerHelper() {
    }

    public Collection<CoatingInstance> getCoatings() {
        return coatingCache;
    }

    public void addCoating(@NotNull CoatingInstance coating) {
        var roCoating = new ReadOnly<>(coating);
        boolean merged = false;
        for(var existing : coatingCache) {
            if(existing.isOfSameType(coating)) {
                existing.getCoating().merge(existing, roCoating);
                merged = true;
                break;
            }
        }

        if(!merged) {
            coatingCache.add(coating);
        }
        //coatingCache.put(instance.getCoating(), instance);
    }

    /**
     * Updates the underlying thing to reflect the state of this container
     */
    public void applyChanges() { }

    @NotNull
    public NbtList toNbt() {
        var coatingsNbt = new NbtList();

        for(var coating : coatingCache) {
            if(coating.isEmpty())
                continue;
            coatingsNbt.add(CoatingInstance.toNbt(coating));
        }

        return coatingsNbt;
    }

    @NotNull
    public NbtCompound writeToNbt(@NotNull NbtCompound nbt) {
        nbt.put(COATINGS_KEY, toNbt());
        return nbt;
    }

    @NotNull
    public static NbtCoatingContainerHelper empty() {
        return new NbtCoatingContainerHelper();
    }

    @NotNull
    public static NbtCoatingContainerHelper fromList(@NotNull NbtList list) {
        var container = empty();
        for (var nbt : list) {
            var instance = CoatingInstance.fromNbt((NbtCompound) nbt);
            if(!instance.isEmpty()) {
                container.addCoating(instance);
            }
        }

        return container;
    }

    @NotNull
    public static NbtCoatingContainerHelper fromCarrier(@NotNull NbtCarrier carrier) {
        return fromList(carrier.get(COATINGS_KEY));
    }

}
