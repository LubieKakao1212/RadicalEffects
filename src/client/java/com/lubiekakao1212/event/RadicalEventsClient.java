package com.lubiekakao1212.event;

import com.lubiekakao1212.apilookup.IEmpLevel;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

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
    }

}
