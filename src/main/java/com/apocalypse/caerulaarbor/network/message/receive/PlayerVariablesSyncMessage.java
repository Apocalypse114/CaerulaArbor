package com.apocalypse.caerulaarbor.network.message.receive;

import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.network.ClientPacketHandler;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record PlayerVariablesSyncMessage(PlayerVariable data) {

    public static PlayerVariablesSyncMessage decode(FriendlyByteBuf buffer) {
        var data = new PlayerVariable();
        data.readNBT(buffer.readNbt());
        return new PlayerVariablesSyncMessage(data);
    }

    public static void encode(PlayerVariablesSyncMessage message, FriendlyByteBuf buffer) {
        buffer.writeNbt(message.data.writeNBT());
    }

    public static void handler(PlayerVariablesSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT,
                () -> () -> ClientPacketHandler.handlePlayerVariablesSync(message, ctx)));
        ctx.get().setPacketHandled(true);
    }
}
