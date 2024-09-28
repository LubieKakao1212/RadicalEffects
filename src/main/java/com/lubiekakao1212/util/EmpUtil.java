package com.lubiekakao1212.util;

import com.lubiekakao1212.RadicalEffects;
import com.lubiekakao1212.config.RadicalConfigCommon;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.PlayerInventoryStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.entity.player.PlayerEntity;
import team.reborn.energy.api.EnergyStorage;

import java.util.ArrayList;
import java.util.List;
import java.lang.Math;
import java.util.function.Consumer;

import static org.joml.Math.lerp;

public class EmpUtil {

    private static final long energyPerLevel = 8192;

    //Do NOT change
    private static final long base = 2;

    public static long levelFromEnergy(long energy) {
        //return (long)Math.max((Math.log((double)energy / (energyPerLevel) + 1) / Math.log(base)), 0);
        //return (long)Math.max((Math.log((double)energy / (energyPerLevel)) / Math.log(base)) + 1, 0);
        var baseCost = RadicalEffects.CONFIG.empConfig.levelCostBase();
        var costMultiplier = RadicalEffects.CONFIG.empConfig.levelCostMultiplier();
        return (long)Math.max((Math.log((double)energy / (baseCost)) / Math.log(costMultiplier)) + 1, 0);
    }

    //Old to-do?
    //TODO Works only for base 2
    public static long energyForLevel(long level) {
        //return (long)Math.pow(base, level - 1) * energyPerLevel * base;
        var baseCost = RadicalEffects.CONFIG.empConfig.levelCostBase();
        var costMultiplier = RadicalEffects.CONFIG.empConfig.levelCostMultiplier();

        return (long)Math.pow(costMultiplier, level - 1) * baseCost;
    }

    /**
     * Drains a set amount of energy from a given player , randomly split among all energy storing items
     * @param player The player to affect
     * @param energyToDrain Total amount of energy to drain
     * @param transaction Transaction this is part of
     * @return Actual amount of energy drained
     */
    public static long drainPlayerEnergyRandomly(PlayerEntity player, long energyToDrain, Transaction transaction) {
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
            var drainBound = (int)Math.min(energyToDrain - energyDrained, Integer.MAX_VALUE);
            var toDrain = random.nextBetween(minDrain, drainBound);

            var idx = random.nextInt(energyItems.size());
            var energyStorage = energyItems.get(idx);
            //bucket.remove(idx);

            var extracted = drainFromStorage(energyStorage, toDrain, transaction);
            if (extracted < toDrain) {
                energyItems.remove(idx);
            }

            energyDrained += extracted;
        }

        return energyDrained;
    }

    public static int drainPlayerEnergyRandomlyByLevel(PlayerEntity player, int drainLevel, Transaction transaction) {
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

        var storageCount = energyItems.size();

        drainLevel *= 100;
        var levelDrained = 0;

        var bucket = new ArrayList<>(energyItems);

        while (levelDrained < drainLevel && !energyItems.isEmpty()) {
            var drainBound = drainLevel - levelDrained;
            double toDrainLevel;
            if(drainBound > 1) {
                toDrainLevel = random.nextBetween(1, (int)getDrainBound(drainBound, drainLevel, storageCount));
            }else {
                toDrainLevel = 1;
            }
            toDrainLevel /= 100;

            var idx = random.nextInt(bucket.size());
            var energyStorage = bucket.get(idx);
            bucket.remove(idx);
            if(bucket.isEmpty()) {
                bucket.addAll(energyItems);
            }

            var extracted = drainFromStorageByLevel(energyStorage, toDrainLevel, transaction);
            /*if (extracted < toDrain) {
                energyItems.remove(energyStorage);
            }*/
            if(extracted.drained < extracted.toDrain) {
                energyItems.remove(energyStorage);
            }
            if(extracted.drained > 0) {
                var ratio = (double)extracted.drained / energyStorage.getCapacity();

                levelDrained += (int)Math.ceil(drainRatioToLevel(ratio) * 100.0);
            }
        }

        return levelDrained / 100;
    }

    public static double drainLevelToRatio(double drainLevel) {
        return 1 - Math.pow(drainRatioBase(), drainLevel);
    }

    public static double drainRatioToLevel(double drainRatio) {
        //TODO check with desmos
        return RadicalUtil.log(drainRatioBase(), 1 - drainRatio);
    }

    public static double drainRatioBase() {
        return 1 - RadicalEffects.CONFIG.empConfig.drainRatioPerLevel();
    }

    public static long getDrainBound(long bound, long total, int energyStorageCount) {
        return Math.min(bound, (long)lerp(total, (double) total / energyStorageCount, RadicalEffects.CONFIG.empConfig.drainDistributionFactor()));
    }

    /**
     * @see EmpUtil#drainLevelToRatio(double) 
     * @see EmpUtil#drainFromStorageByRatio(EnergyStorage, double, Transaction)
     */
    public static DrainData drainFromStorageByLevel(EnergyStorage storage, double drainLevel, Transaction transaction) {
        var ratio = drainLevelToRatio(drainLevel);

        return drainFromStorageByRatio(storage, ratio, transaction);
    }

    /**
     * Drains an amount of energy from an {@link EnergyStorage} proportional to its capacity, ignoring is transfer limit (unless its 0)
     * @param storage The storage to drain from
     * @param energyRatioToDrain Total amount of energy to drain
     * @param transaction Transaction this is part of* @return amount of energy drained
     */
    public static DrainData drainFromStorageByRatio(EnergyStorage storage, double energyRatioToDrain, Transaction transaction) {
        var capacity = storage.getCapacity();
        var toDrain = (long)(capacity * energyRatioToDrain);

        var result = new DrainData();

        result.toDrain = toDrain;
        result.drained = drainFromStorage(storage, toDrain, transaction);

        return result;
    }

    /**
     * Drains a set amount of energy from an {@link EnergyStorage}, ignoring is transfer limit (unless its 0)
     * @param storage The storage to drain from
     * @param energyToDrain Total amount of energy to drain
     * @param transaction Transaction this is part of* @return amount of energy drained
     * @return Amount of energy drained
     */
    public static long drainFromStorage(EnergyStorage storage, long energyToDrain, Transaction transaction) {
        if(!storage.supportsExtraction()) {
            return 0;
        }

        long drained = 0;
        long leftToDrain = energyToDrain;
        long d;

        while((d = storage.extract(leftToDrain, transaction)) != 0){
            drained += d;
            leftToDrain -= d;
        }

        return drained;
    }

    public static class DrainData {
        public long drained;
        public long toDrain;
    }
}
