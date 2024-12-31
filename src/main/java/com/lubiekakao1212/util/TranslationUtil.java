package com.lubiekakao1212.util;

import com.lubiekakao1212.coating.Coatings;
import com.lubiekakao1212.coating.ICoating;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffectUtil;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class TranslationUtil {

    public static String ofIdentifier(Identifier identifier) {
        return identifier.getNamespace()+"."+identifier.getPath();
    }

    public static String ofCoating(Identifier identifier) {
        return "coating."+ofIdentifier(identifier);
    }

    public static String ofCoating(ICoating coating) {
        var id = Coatings.REGISTRY.getId(coating);
        return ofCoating(id);
    }

    public static Text effectNameAndTimeTooltip(StatusEffectInstance effectInstance, float durationMultiplier) {
        MutableText mutableText = Text.translatable(effectInstance.getTranslationKey());

        if (effectInstance.getAmplifier() > 0) {
            mutableText = Text.translatable("potion.withAmplifier", mutableText, Text.translatable("potion.potency." + effectInstance.getAmplifier()));
        }

        if (!effectInstance.isDurationBelow(20)) {
            mutableText = Text.translatable("potion.withDuration", mutableText, StatusEffectUtil.durationToString(effectInstance, durationMultiplier));
        }

        return mutableText.formatted(effectInstance.getEffectType().getCategory().getFormatting());
    }


}
