package com.lubiekakao1212.coating;

import com.lubiekakao1212.util.ReadOnly;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Optional;

public interface ICoatingContainer<T> {

    ReadOnly<T> get();

    @NotNull
    Optional<CoatingInstance> getCoatingByType(@NotNull ICoating coatingType);

    @NotNull
    default Optional<CoatingInstance> getCoatingById(@NotNull Identifier coatingIdentifier) {
        return getCoatingByType(Coatings.REGISTRY.get(coatingIdentifier));
    }

    Collection<CoatingInstance> getCoatings();

    void setCoating(@NotNull CoatingInstance instance);

    /**
     * Updates the underlying thing to reflect the state of this container
     */
    void applyChanges();


}
