package com.lubiekakao1212.coating;

import com.lubiekakao1212.util.ReadOnly;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public class ItemCoatingContainer implements ICoatingContainer<ItemStack> {

    private final ItemStack stack;
    private final NbtCoatingContainer container;

    public ItemCoatingContainer(ItemStack stack) {
        this.stack = stack;
        this.container = NbtCoatingContainer.fromCarrier(stack);
    }

    @Override
    public ReadOnly<ItemStack> get() {
        return new ReadOnly<>(stack);
    }

    @Override
    public @NotNull Optional<CoatingInstance> getCoatingByType(@NotNull ICoating coatingType) {
        return container.getCoatingByType(coatingType);
    }

    @Override
    public Collection<CoatingInstance> getCoatings() {
        return container.getCoatings();
    }

    @Override
    public void setCoating(@NotNull CoatingInstance instance) {
        container.setCoating(instance);
    }

    @Override
    public void applyChanges() {
        stack.put(NbtCoatingContainer.COATINGS_KEY, container.toNbt());
    }
}
