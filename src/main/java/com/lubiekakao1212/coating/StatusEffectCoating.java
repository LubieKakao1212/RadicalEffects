package com.lubiekakao1212.coating;

import com.lubiekakao1212.coating.container.IItemCoatingContainer;
import com.lubiekakao1212.coating.container.ItemCoatingContainerWithEntity;
import com.lubiekakao1212.util.NbtKeys;
import com.lubiekakao1212.util.ReadOnly;
import com.lubiekakao1212.util.TextUtil;
import com.lubiekakao1212.util.TranslationUtil;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.PotionUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class StatusEffectCoating implements ICoating {

    private static final NbtKey<Integer> AMPLIFIER_KEY = new NbtKey<>("amplifier", NbtKey.Type.INT);
    private static final NbtKey<Integer> DURATION_KEY = new NbtKey<>("duration", NbtKey.Type.INT);

    private final StatusEffect effect;

    public StatusEffectCoating(StatusEffect effect) {
        this.effect = effect;
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
            var coating = instance.value();

            LivingEntity sourceEntity = null;
            if(container instanceof ItemCoatingContainerWithEntity withEntity) {
                sourceEntity = withEntity.getOwningEntity();
            }

            entityUsage.target.addStatusEffect(getEffectInstance(coating));
        });
    }

    @Override
    public void addItemTooltip(ReadOnly<CoatingInstance> instance, IItemCoatingContainer container, TooltipContext context, List<Text> lines) {
        var coating = instance.value();
        var effectInstance = getEffectInstance(coating);

        var effectText = TranslationUtil.effectNameAndTimeTooltip(effectInstance, 1f);

        lines.add(Text.translatable("coatings.header",
                effectText,
                Text.of(TextUtil.amountBar(coating.getUsesLeft()))
                        .getWithStyle(Style.EMPTY.withFormatting(Formatting.GRAY))
                        .get(0)
        ));

        if(context.isAdvanced()) {
            var coatingNbt = coating.getNbt();
            var nbtCount = 0;
            if(coatingNbt != null) {
                nbtCount = coatingNbt.getSize();
            }

            lines.add(Text.translatable("coatings.nbt", nbtCount));
        }
    }

    private StatusEffectInstance getEffectInstance(CoatingInstance coating) {
        return new StatusEffectInstance(
                effect,
                coating.getOr(DURATION_KEY, 20),
                coating.getOr(AMPLIFIER_KEY, 0)
        );
    }

    /**
     * {@link ICoating#instanceTypesEqual(CoatingInstance, CoatingInstance)} must be checked before this method is called <br/>
     * Second parameter is merged into first <br/>
     * First parameter is to be modified
     *
     * @param one
     * @param two
     */
    @Override
    public void merge(CoatingInstance one, ReadOnly<CoatingInstance> two) {
        var second = two.value();

        var effect1 = getEffectInstance(one);
        var effect2 = getEffectInstance(second);

        var amp1 = effect1.getAmplifier();
        var amp2 = effect2.getAmplifier();

        //Higher amplifier overwrites lower one
        if(amp1 > amp2) {
            return;
        }
        if(amp2 > amp1) {
            one.addUses(-one.getUsesLeft());
            one.addUses(second.getUsesLeft());
            return;
        }

        //We are guaranteed that amp1 == amp2

        var uses1 = one.getUsesLeft();
        var uses2 = second.getUsesLeft();

        //Duration dilution
        var duration = effect1.getDuration() * uses1 + effect2.getDuration() * uses2;
        duration /= uses1 + uses2;

        //Amplifier stays the same
        one.put(DURATION_KEY, duration);

        ICoating.super.merge(one, two);
    }


    /**
     * Checks if two {@link CoatingInstance}s of this coating are considered equal
     *
     * @param a
     * @param b
     * @return
     */
    @Override
    public boolean instanceTypesEqual(CoatingInstance a, CoatingInstance b) {
        return ICoating.super.instanceTypesEqual(a, b);
    }
}
