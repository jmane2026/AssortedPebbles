package com.jmane2026.assortedpebbles.client;

import com.jmane2026.assortedpebbles.config.AssortedPebblesConfig;
import com.jmane2026.assortedpebbles.network.PebbleGatherPacket;
import com.jmane2026.assortedpebbles.util.ConfigTagResolver;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(value = Dist.CLIENT, modid = "assortedpebbles")
public class ClientPebbleTicker {

    private static int tickCooldown = 0;

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        if (mc.level == null || mc.player == null || mc.isPaused()) return;

        if (tickCooldown > 0) {
            tickCooldown--;
        }

        if (mc.options.keyUse.isDown() && mc.player.isCrouching()) {
            Player player = mc.player;

            if (player.getItemInHand(InteractionHand.MAIN_HAND).isEmpty() && tickCooldown == 0) {
                HitResult hit = mc.hitResult;

                if (hit != null && hit.getType() == HitResult.Type.BLOCK) {
                    BlockHitResult blockHit = (BlockHitResult) hit;
                    BlockPos pos = blockHit.getBlockPos();
                    BlockState state = mc.level.getBlockState(pos);

                    if (ConfigTagResolver.isBlockValidTarget(state)) {
                        player.swing(InteractionHand.MAIN_HAND);
                        mc.getConnection().send(new PebbleGatherPacket(pos));
                        tickCooldown = AssortedPebblesConfig.TICK_COOLDOWN.get();
                    }
                }
            }
        }
    }
}