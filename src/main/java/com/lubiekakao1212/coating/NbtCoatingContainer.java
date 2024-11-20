package com.lubiekakao1212.coating;

import com.lubiekakao1212.util.ReadOnly;
import io.wispforest.owo.nbt.NbtCarrier;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class NbtCoatingContainer implements ICoatingContainer<Void> {

    public static final NbtKey<NbtList> COATINGS_KEY = new NbtKey.ListKey<>("coatings", NbtKey.Type.COMPOUND);

    private final Map<ICoating, CoatingInstance> coatingCache;

    private NbtCoatingContainer() {
        this.coatingCache = new HashMap<>();
    }

    @Override
    public ReadOnly<Void> get() {
        return new ReadOnly<>(null);
    }

    @Override
    public @NotNull Optional<CoatingInstance> getCoatingByType(@NotNull ICoating coatingType) {
        return Optional.ofNullable(coatingCache.get(coatingType));
    }

    @Override
    public Collection<CoatingInstance> getCoatings() {
        return coatingCache.values();
    }

    @Override
    public void setCoating(@NotNull CoatingInstance instance) {
        coatingCache.put(instance.getCoating(), instance);
    }

    /**
     * Updates the underlying thing to reflect the state of this container
     */
    @Override
    public void applyChanges() { }

    @NotNull
    public NbtList toNbt() {
        var coatingsNbt = new NbtList();

        for(var coating : coatingCache.values()) {
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
    public static NbtCoatingContainer empty() {
        return new NbtCoatingContainer();
    }

    @NotNull
    public static NbtCoatingContainer fromList(@NotNull NbtList list) {
        var container = empty();
        for (var nbt : list) {
            var instance = CoatingInstance.fromNbt((NbtCompound) nbt);
            if(!instance.isEmpty()) {
                container.setCoating(instance);
            }
        }

        return container;
    }

    @NotNull
    public static NbtCoatingContainer fromCarrier(@NotNull NbtCarrier carrier) {
        return fromList(carrier.get(COATINGS_KEY));
    }

}
