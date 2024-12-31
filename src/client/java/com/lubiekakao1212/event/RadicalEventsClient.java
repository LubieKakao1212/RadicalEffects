package com.lubiekakao1212.event;

import com.lubiekakao1212.apilookup.IEmpLevel;
import com.lubiekakao1212.coating.container.ItemCoatingContainer;
import com.lubiekakao1212.util.ReadOnly;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

import java.util.List;

public class RadicalEventsClient {

    public static void init() {
        ItemTooltipCallback.EVENT.register(RadicalEventsClient::itemTooltip);
    }

    public static void itemTooltip(ItemStack stack, TooltipContext context, List<Text> lines) {
        var level = IEmpLevel.ITEM.find(stack, null);

        if(level != null) {
            level.addTooltip(lines);
        }

        var coatings = new ItemCoatingContainer(stack);

        for(var coating : coatings.getCoatings()) {
            coating.getCoating().addItemTooltip(new ReadOnly<>(coating), coatings, context, lines);
        }
    }
}
