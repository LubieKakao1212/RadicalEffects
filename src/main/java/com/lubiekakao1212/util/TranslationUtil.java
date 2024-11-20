package com.lubiekakao1212.util;

import com.lubiekakao1212.coating.Coatings;
import com.lubiekakao1212.coating.ICoating;
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


}
