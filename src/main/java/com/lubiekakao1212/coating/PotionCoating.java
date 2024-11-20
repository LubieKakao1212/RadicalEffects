package com.lubiekakao1212.coating;

import com.lubiekakao1212.util.ReadOnly;
import io.wispforest.owo.nbt.NbtKey;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtString;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class PotionCoating implements ICoating {

    private static final NbtKey.ListKey<String> POTIONS_KEY = new NbtKey.ListKey<>("potions", NbtKey.Type.STRING);

    /**
     * Used to apply effects
     *
     * @param usage
     * @param instance  can be modified
     * @param container
     * @param world
     */
    @Override
    public <T, C> void affectTarget(CoatingUsage<T> usage, ReadOnly<CoatingInstance> instance, ICoatingContainer<C> container, World world) {
        usage.asEntity().ifPresent(entityUsage -> {
            var coating = instance.value();
            var potionsNbt = coating.get(POTIONS_KEY);

            LivingEntity sourceEntity = null;
            if(container.get().value() instanceof LivingEntity entity) {
                sourceEntity = entity;
            }
            else if(container instanceof ItemCoatingContainerWithEntity withEntity) {
                sourceEntity = withEntity.getOwningEntity();
            }

            for (var potionNbt : potionsNbt) {
                var potion = Potion.byId(((NbtString)potionNbt).asString());
                for (var effect : potion.getEffects()) {
                    //This line does funny
                    //entityUsage.target.addStatusEffect(effect, sourceEntity);

                    //This one does not
                    entityUsage.target.addStatusEffect(
                            new StatusEffectInstance(
                            effect.getEffectType(),
                            effect.mapDuration(i -> i),
                            effect.getAmplifier(),
                            effect.isAmbient(),
                            effect.shouldShowParticles()
                    ));
                }
            }
        });
    }

    @Override
    public void addItemTooltip(ReadOnly<CoatingInstance> instance, ICoatingContainer<ItemStack> container, TooltipContext context, List<Text> lines) {
        ICoating.super.addItemTooltip(instance, container, context, lines);

        var coating = instance.value();
        var potionsNbt = coating.get(POTIONS_KEY);

        for (var potionNbt : potionsNbt) {
            var potion = Potion.byId(((NbtString)potionNbt).asString());
            var pTooltip = new ArrayList<Text>();
            PotionUtil.buildTooltip(potion.getEffects(), pTooltip, 1f);

            pTooltip.forEach(text -> {
                lines.add(Text.literal("  ").append(text));
            });
        }
    }

    @Override
    public EnumSet<Designation> getDesignation() {
        return EnumSet.of(Designation.ITEM, Designation.ENTITY, Designation.ITEM_FOOD);
    }

}
