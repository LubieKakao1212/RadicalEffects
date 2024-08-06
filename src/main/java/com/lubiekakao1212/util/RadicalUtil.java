package com.lubiekakao1212.util;

import com.lubiekakao1212.RadicalEffects;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.entity.player.PlayerEntity;
import org.joml.Math;
import team.reborn.energy.api.EnergyStorage;

import java.util.ArrayList;
import java.util.List;

public class RadicalUtil {

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


}
