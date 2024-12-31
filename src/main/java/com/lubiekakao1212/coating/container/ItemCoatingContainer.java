package com.lubiekakao1212.coating.container;

import com.lubiekakao1212.coating.CoatingInstance;
import com.lubiekakao1212.coating.NbtCoatingContainerHelper;
import com.lubiekakao1212.util.ReadOnly;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public class ItemCoatingContainer implements IItemCoatingContainer {

    private final ItemStack stack;
    private final NbtCoatingContainerHelper container;

    public ItemCoatingContainer(ItemStack stack) {
        this.stack = stack;
        this.container = NbtCoatingContainerHelper.fromCarrier(stack);
    }

    @Override
    public ReadOnly<ItemStack> get() {
        return new ReadOnly<>(stack);
    }

    @Override
    public Collection<CoatingInstance> getCoatings() {
        return container.getCoatings();
    }

    @Override
    public void addCoating(@NotNull CoatingInstance instance) {
        container.addCoating(instance);
    }

    @Override
    public void applyChanges() {
        stack.put(NbtCoatingContainerHelper.COATINGS_KEY, container.toNbt());
    }
}
