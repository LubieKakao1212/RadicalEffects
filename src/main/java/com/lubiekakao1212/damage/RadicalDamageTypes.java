package com.lubiekakao1212.damage;

import com.lubiekakao1212.RadicalEffects;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class RadicalDamageTypes {

    public static final RegistryKey<DamageType> DAMAGE_VOLATILE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(RadicalEffects.MODID, "volatile_explosion"));
    public static final RegistryKey<DamageType> DAMAGE_EMP = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(RadicalEffects.MODID, "emp"));
    public static final RegistryKey<DamageType> DAMAGE_ZAP = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier(RadicalEffects.MODID, "zap"));

}
