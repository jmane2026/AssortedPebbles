package com.jmane2026.assortedpebbles.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems("assortedpebbles");

    public static final DeferredItem<Item> STONE_PEBBLE = ITEMS.registerItem(
            "stone_pebble",
            Item::new
    );

    public static final DeferredItem<Item> ANDESITE_PEBBLE = ITEMS.registerItem(
            "andesite_pebble",
            Item::new
    );

    public static final DeferredItem<Item> DIORITE_PEBBLE = ITEMS.registerItem(
            "diorite_pebble",
            Item::new
    );

    public static final DeferredItem<Item> GRANITE_PEBBLE = ITEMS.registerItem(
            "granite_pebble",
            Item::new
    );

    public static final DeferredItem<Item> DEEPSLATE_PEBBLE = ITEMS.registerItem(
            "deepslate_pebble",
            Item::new
    );

    public static final DeferredItem<Item> FLINT_SHARD = ITEMS.registerItem(
            "flint_shard",
            Item::new
    );

    public static void register(IEventBus modEventBus) {
        ITEMS.register(modEventBus);
    }
}
