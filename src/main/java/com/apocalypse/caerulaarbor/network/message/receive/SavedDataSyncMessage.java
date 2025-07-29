package com.apocalypse.caerulaarbor.network.message.receive;

import com.apocalypse.caerulaarbor.capability.map.MapVariables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record SavedDataSyncMessage(SavedData data) {

    public static SavedDataSyncMessage decode(FriendlyByteBuf buffer) {
        CompoundTag nbt = buffer.readNbt();
        var data = new MapVariables();
        if (nbt != null) {
            data.read(nbt);
        }
        return new SavedDataSyncMessage(data);
    }

    public static void encode(SavedDataSyncMessage message, FriendlyByteBuf buffer) {
        if (message.data != null) {
            buffer.writeNbt(message.data.save(new CompoundTag()));
        }
    }

    public static void handler(SavedDataSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            if (context.getDirection().getReceptionSide().isClient() && message.data != null) {
                MapVariables.clientSide = (MapVariables) message.data;
            }
        });
        context.setPacketHandled(true);
    }
}
