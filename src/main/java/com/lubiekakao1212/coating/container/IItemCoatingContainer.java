package com.lubiekakao1212.coating.container;

import com.lubiekakao1212.coating.CoatingInstance;
import com.lubiekakao1212.coating.Coatings;
import com.lubiekakao1212.coating.ICoating;
import com.lubiekakao1212.util.ReadOnly;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface IItemCoatingContainer {

    ReadOnly<ItemStack> get();

    Collection<CoatingInstance> getCoatings();

    void addCoating(@NotNull CoatingInstance instance);

    /**
     * Updates the underlying thing to reflect the state of this container
     */
    void applyChanges();


}
