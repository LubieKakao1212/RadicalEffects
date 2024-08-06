package com.lubiekakao1212.effects;

import com.lubiekakao1212.RadicalEffects;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class RadicalStatusEffects {

    public static StatusEffect VOLATILE = new GenericStatusEffect(StatusEffectCategory.HARMFUL, 0x777777);
    public static StatusEffect MARK = new GenericStatusEffect(StatusEffectCategory.HARMFUL, 0xf0f011);

    public static void init() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier(RadicalEffects.MODID, "volatile"), VOLATILE);
        Registry.register(Registries.STATUS_EFFECT, new Identifier(RadicalEffects.MODID, "mark"), MARK);
    }

}
