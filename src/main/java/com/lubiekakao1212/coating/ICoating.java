package com.lubiekakao1212.coating;

import com.lubiekakao1212.coating.container.IItemCoatingContainer;
import com.lubiekakao1212.coating.container.ItemCoatingContainerWithEntity;
import com.lubiekakao1212.util.ReadOnly;
import com.lubiekakao1212.util.TextUtil;
import com.lubiekakao1212.util.TranslationUtil;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.List;

public interface ICoating {

    NbtKey.Type<ICoating> KEY_TYPE = NbtKey.Type.ofRegistry(Coatings.REGISTRY);

    /**
     * How much additional durability should be used upon hitting an entity using this coating <p>
     * This is called on the LOGICAL SERVER
     * @param instance Do not modify
     * @param container Do not modify
     * @return Additional durability to consume, can be negative
     */
    default <T> int getItemDurabilityLoss(CoatingUsage<T> usage, ReadOnly<CoatingInstance> instance, IItemCoatingContainer container, World world) {
        return 0;
    }

    /**
     * Used to apply effects
     * @param instance can be modified
     */
    default <T> void affectTarget(CoatingUsage<T> usage, ReadOnly<CoatingInstance> instance, IItemCoatingContainer container, World world) { }

    /**
     * Called on every interaction, used to modify the item
     * @param instance Do not modify here
     */
    default <T> ItemStack modifyItemUsed(CoatingUsage<T> usage, ReadOnly<CoatingInstance> instance, IItemCoatingContainer container, ItemStack stack, World world) {
        if(stack.isDamageable()) {
            var damage = getItemDurabilityLoss(usage, instance, container, world);
            if(damage > 0) {
                if(container instanceof ItemCoatingContainerWithEntity entityContainer) {
                    stack.damage(damage, entityContainer.getOwningEntity(), entity -> { });
                }
            }
        }
        return stack;
    }

    /**
     * {@link ICoating#modifyOnUse(CoatingUsage, CoatingInstance, IItemCoatingContainer, net.minecraft.world.World)}
     * @param usage
     * @param instance
     * @param container
     * @param world
     * @return
     * @param <T>
     */
    default <T> int getCoatingUsesUsed(CoatingUsage<T> usage, ReadOnly<CoatingInstance> instance, IItemCoatingContainer container, World world) {
        return 1;
    }

    /**
     * Used to modify the coating instance upon use, should not modify other coatings
     * @param usage
     * @param instance
     * @param container
     * @param world
     */
    default <T, C> void modifyOnUse(CoatingUsage<T> usage, CoatingInstance instance, IItemCoatingContainer container, World world) {
        instance.addUses(-getCoatingUsesUsed(usage, new ReadOnly<>(instance), container, world));
    }

    //TODO getBonusMiningSpeed()

    //TODO getAttributeModifiers()

    //TODO affectFood()

    //TODO tick()

    default void addItemTooltip(ReadOnly<CoatingInstance> instance, IItemCoatingContainer container, TooltipContext context, List<Text> lines) {
        var value = instance.value();
        var translationKeyBase = TranslationUtil.ofCoating(value.getCoating());
        var translationKeyName = translationKeyBase + ".name";
        //lines.add(Text.translatable(translationKeyName, value.getUsesLeft()));
        lines.add(
                Text.translatable("coatings.header",
                        Text.translatable(translationKeyName),
                        Text.of(TextUtil.amountBar(value.getUsesLeft()))
                                .getWithStyle(Style.EMPTY.withFormatting(Formatting.GRAY))
                                .get(0)
                )
        );
                        //value.getUsesLeft()));
        var valueNbt = value.getNbt();
        var nbtCount = 0;
        if(valueNbt != null) {
            nbtCount = valueNbt.getSize();
        }

        if(context.isAdvanced()) {
            lines.add(Text.translatable("coatings.nbt", nbtCount));
        }
    }

    /**
     * {@link ICoating#instanceTypesEqual(CoatingInstance, CoatingInstance)} must be checked before this method is called <br/>
     * Second parameter is merged into first <br/>
     * First parameter is to be modified
     * @param one
     * @param two
     */
    default void merge(CoatingInstance one, ReadOnly<CoatingInstance> two) {
        one.addUses(two.value().getUsesLeft());
    }

    /**
     * Checks if two {@link CoatingInstance}s of this coating are considered equal
     * @return
     */
    default boolean instanceTypesEqual(CoatingInstance a, CoatingInstance b) {
        return a.getCoating() == this && b.getCoating() == this;
    }
}
