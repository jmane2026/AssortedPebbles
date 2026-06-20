package com.jmane2026.assortedpebbles.util;

import com.jmane2026.assortedpebbles.config.AssortedPebblesConfig;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ConfigTagResolver {
    private static final Random RANDOM = new Random();

    public static boolean isBlockValidTarget(BlockState state) {
        List<? extends List<String>> pools = AssortedPebblesConfig.LOOT_POOLS.get();
        for (List<String> entry : pools) {
            if (entry == null || entry.isEmpty()) continue;

            String target = entry.get(0).trim();
            if (doesBlockMatchTarget(target, state)) {
                return true;
            }
        }
        return false;
    }

    public static List<ItemStack> calculateDrops(BlockState state) {
        List<ItemStack> successfulDrops = new ArrayList<>();
        List<? extends List<String>> pools = AssortedPebblesConfig.LOOT_POOLS.get();

        for (List<String> entry : pools) {
            if (entry == null || entry.size() < 2) continue;

            String target = entry.get(0).trim();
            String dropString = entry.get(1).trim();

            if (doesBlockMatchTarget(target, state)) {
                String[] dropSegments = dropString.split(",");

                for (String segment : dropSegments) {
                    String[] itemChance = segment.split(":");
                    if (itemChance.length < 3) continue;

                    String itemIdentifier = itemChance[0].trim() + ":" + itemChance[1].trim();

                    try {
                        double chanceNeeded = Double.parseDouble(itemChance[2].trim());

                        if (RANDOM.nextDouble() <= chanceNeeded) {
                            Identifier itemKey = Identifier.parse(itemIdentifier);
                            Item droppedItem = BuiltInRegistries.ITEM.getOptional(itemKey).orElse(Items.AIR);
                            if (droppedItem != Items.AIR) {
                                successfulDrops.add(new ItemStack(droppedItem, 1));
                            }
                        }
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        }
        return successfulDrops;
    }

    private static boolean doesBlockMatchTarget(String target, BlockState state) {
        if (target.startsWith("#")) {
            TagKey<Block> tagKey = TagKey.create(Registries.BLOCK, Identifier.parse(target.substring(1)));
            return state.is(tagKey);
        } else {
            Block targetBlock = BuiltInRegistries.BLOCK.getOptional(Identifier.parse(target)).orElse(Blocks.AIR);
            return state.is(targetBlock);
        }
    }
}