package com.lubiekakao1212.coating;

import io.wispforest.owo.nbt.NbtCarrier;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.nbt.NbtCompound;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CoatingInstance implements NbtCarrier {

    public static final NbtKey<Integer> USES_LEFT_KEY = new NbtKey<>("usesLeft", NbtKey.Type.INT);
    public static final NbtKey<NbtCompound> NBT_KEY = new NbtKey<>("nbt", NbtKey.Type.COMPOUND);
    public static final NbtKey<ICoating> COATING_KEY = new NbtKey<>("id", ICoating.KEY_TYPE);

    private ICoating coating;
    private int usesLeft;
    private NbtCompound nbt;
    //TODO potency?

    public CoatingInstance(ICoating coating) {
        this(coating, 1);
    }

    public CoatingInstance(ICoating coating, int usesLeft) {
        this(coating, usesLeft, null);
    }

    public CoatingInstance(@NotNull ICoating coating, int usesLeft, @Nullable NbtCompound nbt) {
        this.coating = coating;
        this.usesLeft = usesLeft;
        this.nbt = nbt;
    }

    @NotNull
    public ICoating getCoating() {
        return coating;
    }

    public int getUsesLeft() {
        return usesLeft;
    }

    public void addUses(int uses) {
        this.usesLeft += uses;
    }

    public boolean isEmpty() {
        return usesLeft <= 0 || coating == Coatings.NONE;
    }

    @Nullable
    public NbtCompound getNbt() {
        return nbt;
    }

    @NotNull
    public NbtCompound getOrCreateNbt() {
        if(nbt == null) {
            nbt = new NbtCompound();
        }
        return nbt;
    }

    public static CoatingInstance fromNbt(NbtCompound nbt) {
        var coating = nbt.get(COATING_KEY);
        var usesLeft = nbt.getOr(USES_LEFT_KEY, 1);
        var instanceNbt = nbt.getOr(NBT_KEY, null);

        return new CoatingInstance(coating, usesLeft, instanceNbt);
    }

    public static NbtCompound toNbt(CoatingInstance coatingInstance) {
        var nbt = new NbtCompound();

        nbt.put(COATING_KEY, coatingInstance.coating);
        nbt.put(USES_LEFT_KEY, coatingInstance.usesLeft);
        nbt.put(NBT_KEY, coatingInstance.nbt.copy());

        return nbt;
    }

    @Override
    public <T> T get(@NotNull NbtKey<T> key) {
        return nbt.get(key);
    }

    @Override
    public <T> void put(@NotNull NbtKey<T> key, @NotNull T value) {
        nbt.put(key, value);
    }

    @Override
    public <T> void delete(@NotNull NbtKey<T> key) {
        nbt.delete(key);
    }

    @Override
    public <T> boolean has(@NotNull NbtKey<T> key) {
        return nbt.has(key);
    }
}
