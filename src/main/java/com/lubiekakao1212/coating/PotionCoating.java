package com.lubiekakao1212.coating;

import com.google.common.collect.Lists;
import com.lubiekakao1212.coating.container.IItemCoatingContainer;
import com.lubiekakao1212.coating.container.ItemCoatingContainerWithEntity;
import com.lubiekakao1212.util.NbtKeys;
import com.lubiekakao1212.util.ReadOnly;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PotionCoating implements ICoating {

    private static final NbtKey<StatusEffectInstance> EFFECT_KEY = new NbtKey<>("effect", NbtKeys.EFFECT_INSTANCE);
    private static final NbtKey<NbtCompound> RAW_EFFECT_KEY = new NbtKey<>("effect", NbtKey.Type.COMPOUND);
    private static final NbtKey<Integer> ID_KEY = new NbtKey<>("Id", NbtKey.Type.INT);

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
            var effect = coating.get(EFFECT_KEY);

            LivingEntity sourceEntity = null;
            if(container instanceof ItemCoatingContainerWithEntity withEntity) {
                sourceEntity = withEntity.getOwningEntity();
            }

            entityUsage.target.addStatusEffect(new StatusEffectInstance(
                        effect.getEffectType(),
                        effect.mapDuration(i -> i),
                        effect.getAmplifier(),
                        effect.isAmbient(),
                        effect.shouldShowParticles()
                ));
        });
    }

    @Override
    public void addItemTooltip(ReadOnly<CoatingInstance> instance, IItemCoatingContainer container, TooltipContext context, List<Text> lines) {
        //ICoating.super.addItemTooltip(instance, container, context, lines);

        var coating = instance.value();
        var effect = coating.get(EFFECT_KEY);

        if(effect == null) {
            lines.add(Text.translatable("coating.radical-effects.potion.malformed"));
            return;
        }

        var pTooltip = new ArrayList<Text>();
        PotionUtil.buildTooltip(List.of(effect), pTooltip, 1f);

        pTooltip.forEach(text -> {
            lines.add(((MutableText)text).append(" "+coating.getUsesLeft()));
        });
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

        var effect1 = one.get(EFFECT_KEY);
        var effect2 = second.get(EFFECT_KEY);

        var amp1 = effect1.getAmplifier();
        var amp2 = effect2.getAmplifier();

        //Higher amplifier overwrites lower one
        if(amp1 > amp2) {
            return;
        }
        if(amp2 > amp1) {
            one.addUses(-one.getUsesLeft());
            one.addUses(second.getUsesLeft());
            one.put(EFFECT_KEY, effect2);
            return;
        }

        //We are guaranteed that amp1 == amp2

        var uses1 = one.getUsesLeft();
        var uses2 = second.getUsesLeft();

        //Duration dilution
        var duration = effect1.getDuration() * uses1 + effect2.getDuration() * uses2;
        duration /= uses1 + uses2;

        one.put(EFFECT_KEY, new StatusEffectInstance(
                effect1.getEffectType(),
                duration,
                amp1,
                effect1.isAmbient(),
                effect1.shouldShowParticles()
        ));

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
        return ICoating.super.instanceTypesEqual(a, b) &&
                a.get(RAW_EFFECT_KEY).get(ID_KEY).equals(b.get(RAW_EFFECT_KEY).get(ID_KEY));
    }
}
