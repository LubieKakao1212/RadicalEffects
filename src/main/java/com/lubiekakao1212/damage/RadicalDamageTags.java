package com.lubiekakao1212.damage;

import com.lubiekakao1212.RadicalEffects;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class RadicalDamageTags {

    public static final TagKey<DamageType> NO_DAMAGE = TagKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(RadicalEffects.MODID, "no_damage"));
    public static final TagKey<DamageType> CONVERT_ENERGY = TagKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(RadicalEffects.MODID, "convert/energy"));
    public static final TagKey<DamageType> IGNORE_MARK = TagKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(RadicalEffects.MODID, "ignore_mark"));

}
