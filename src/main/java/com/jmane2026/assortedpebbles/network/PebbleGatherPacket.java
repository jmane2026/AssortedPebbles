package com.jmane2026.assortedpebbles.network;

import com.jmane2026.assortedpebbles.util.ConfigTagResolver;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;

public record PebbleGatherPacket(BlockPos pos) implements CustomPacketPayload {

    public static final Type<PebbleGatherPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath("assortedpebbles", "gather_pebble"));

    public static final StreamCodec<FriendlyByteBuf, PebbleGatherPacket> STREAM_CODEC = StreamCodec.of(
            (buf, value) -> buf.writeBlockPos(value.pos),
            buf -> new PebbleGatherPacket(buf.readBlockPos())
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(final PebbleGatherPacket payload, final IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.flow().isServerbound()) {
                ServerPlayer player = (ServerPlayer) context.player();
                ServerLevel level = player.level();
                BlockPos pos = payload.pos();

                if (player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5) < 36.0) {
                    BlockState state = level.getBlockState(pos);

                    if (ConfigTagResolver.isBlockValidTarget(state) && player.getMainHandItem().isEmpty()) {

                        List<ItemStack> successfulDrops = ConfigTagResolver.calculateDrops(state);

                        double spawnX = pos.getX() + 0.5;
                        double spawnY = pos.getY() + 1.05;
                        double spawnZ = pos.getZ() + 0.5;

                        for (ItemStack dropStack : successfulDrops) {
                            if (!dropStack.isEmpty()) {
                                ItemEntity itemEntity = new ItemEntity(level, spawnX, spawnY, spawnZ, dropStack);
                                itemEntity.setDeltaMovement(
                                        level.getRandom().nextGaussian() * 0.02,
                                        0.15,
                                        level.getRandom().nextGaussian() * 0.02
                                );
                                level.addFreshEntity(itemEntity);
                            }
                        }
                    }
                }
            }
        });
    }
}