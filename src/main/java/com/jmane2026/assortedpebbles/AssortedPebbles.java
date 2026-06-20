package com.jmane2026.assortedpebbles;

import com.jmane2026.assortedpebbles.config.AssortedPebblesConfig;
import com.jmane2026.assortedpebbles.item.ModItems;
import com.jmane2026.assortedpebbles.network.PebbleGatherPacket;
import com.jmane2026.assortedpebbles.util.ConfigFormatter;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(AssortedPebbles.MODID)
public class AssortedPebbles {
    public static final String MODID = "assortedpebbles";
    public static final Logger LOGGER = LoggerFactory.getLogger(AssortedPebbles.class);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, "assortedpebbles");

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> ASSORTED_PEBBLES_TAB =
            CREATIVE_MODE_TABS.register("assorted_pebbles_tab", () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.assortedpebbles.tab"))
                    .icon(() -> new ItemStack(ModItems.DEEPSLATE_PEBBLE.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.STONE_PEBBLE.get());
                        output.accept(ModItems.ANDESITE_PEBBLE.get());
                        output.accept(ModItems.DIORITE_PEBBLE.get());
                        output.accept(ModItems.GRANITE_PEBBLE.get());
                        output.accept(ModItems.DEEPSLATE_PEBBLE.get());
                        output.accept(ModItems.FLINT_SHARD.get());
                    })
                    .build()
            );

    public AssortedPebbles(IEventBus modEventBus, ModContainer modContainer) {
        ModItems.register(modEventBus);
        modContainer.registerConfig(net.neoforged.fml.config.ModConfig.Type.COMMON, AssortedPebblesConfig.SPEC);
        CREATIVE_MODE_TABS.register(modEventBus);
        modEventBus.addListener(this::registerPackets);
        modEventBus.addListener(this::onConfigLoad);
        modEventBus.addListener(this::onConfigReload);
    }

    private void registerPackets(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1.0.1");

        registrar.playToServer(
                PebbleGatherPacket.TYPE,
                PebbleGatherPacket.STREAM_CODEC,
                PebbleGatherPacket::handle
        );

        System.out.println("[Pebble Collector] Network packet pipelines registered successfully");
    }

    private void onConfigLoad(ModConfigEvent.Loading event) {
        if(event.getConfig().getSpec() == AssortedPebblesConfig.SPEC) {
            ConfigFormatter.formatConfigLines();
        }
    }

    private void onConfigReload(ModConfigEvent.Reloading event) {
        if(event.getConfig().getSpec() == AssortedPebblesConfig.SPEC) {
            ConfigFormatter.formatConfigLines();
        }
    }
}
