package com.jmane2026.assortedpebbles.config;

import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class AssortedPebblesConfig {
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.IntValue TICK_COOLDOWN;
    public static final ModConfigSpec.ConfigValue<List<? extends List<String>>> LOOT_POOLS;

    static {
        BUILDER.push("Assorted Pebbles Config");

        TICK_COOLDOWN = BUILDER
                .comment("How many game ticks between each pebble drop while holding Shift + Right-Click.")
                .defineInRange("tickCooldown", 5, 1, 100);

        LOOT_POOLS = BUILDER
                .comment("""
                         Configure your loot pools as individual block entries.
                         You can create as many as you want!
                         Format:
                           [ "target_block_or_tag",
                               \"\"\"\\
                               "item1:chance1,
                               item2:chance2,
                               etc"
                               \"\"\"
                           ],""")
                .defineList("lootPools",
                        List.of(
                                List.of("#minecraft:dirt", "assortedpebbles:stone_pebble:0.45,assortedpebbles:andesite_pebble:0.12,assortedpebbles:diorite_pebble:0.12,assortedpebbles:granite_pebble:0.12,assortedpebbles:deepslate_pebble:0.05"),
                                List.of("minecraft:gravel", "assortedpebbles:flint_shard:0.1,minecraft:flint:0.02")
                        ),
                        element -> element instanceof List
                );

        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}