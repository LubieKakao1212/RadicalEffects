package com.lubiekakao1212.util;

import com.lubiekakao1212.RadicalEffects;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.entity.player.PlayerEntity;
import team.reborn.energy.api.EnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class RadicalUtil {

    private static final String[] UNITS = new String[]{
            "",
            "K",
            "M",
            "B",
            "T",
            "Qu",
            "Qt",
            "Sx",
            "Sp",
            "Oc",
            "No",
            "Error",
    };

    public static int drainPlayerEnergyRandomly(PlayerEntity player, int energyToDrain, Transaction transaction) {
        final var minDrain = 10;

        var playerInv = PlayerInventoryStorage.of(player);

        List<EnergyStorage> energyItems = new ArrayList<>();
        for(var slot : playerInv.getSlots()) {
            var ctx = ContainerItemContext.ofPlayerSlot(player, slot);
            var energy = ctx.find(EnergyStorage.ITEM);

            if(energy != null && energy.supportsExtraction()) {
                energyItems.add(energy);
            }
        }
        var random = player.getRandom();


        var t = energyToDrain - minDrain;
        var energyDrained = 0;

        while (energyDrained < t && !energyItems.isEmpty()) {
            var toDrain = random.nextBetween(minDrain, energyToDrain - energyDrained);

            var idx = random.nextInt(energyItems.size());
            var energyStorage = energyItems.get(idx);
            //bucket.remove(idx);

            var extracted = (int) energyStorage.extract(toDrain, transaction);
            if (extracted == 0) {
                energyItems.remove(energyStorage);
            }

            energyDrained += extracted;
        }

        return energyDrained;
    }

    public static String toShortString(long value) {
        int i = 0;
        int j = 1;
        var v = value;
        while(v > 1000) {
            v /= 1000;
            i++;
            j *= 1000;
        }

        var truncated = value / j;
        var str = Long.toString(truncated);
        if(j > 1) {
            var decimal = (value / (j / 10)) % 10;
            return str + "." + decimal + UNITS[i];
        }

        return str;
    }
}