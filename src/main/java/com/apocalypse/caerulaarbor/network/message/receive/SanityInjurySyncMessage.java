package com.apocalypse.caerulaarbor.network.message.receive;

import com.apocalypse.caerulaarbor.capability.sanity.SanityInjuryCapability;
import com.apocalypse.caerulaarbor.network.ClientPacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SanityInjurySyncMessage(CompoundTag data) {

    public static SanityInjurySyncMessage decode(FriendlyByteBuf buffer) {
        CompoundTag tag = buffer.readNbt();
        return new SanityInjurySyncMessage(tag == null ? new CompoundTag() : tag);
        
    }

    public static void encode(SanityInjurySyncMessage message, FriendlyByteBuf buffer) {
        buffer.writeNbt(message.data);
    }

    public static void handler(SanityInjurySyncMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ClientPacketHandler.handleSanityInjurySync(message, ctx)));
        ctx.get().setPacketHandled(true);
    }
}