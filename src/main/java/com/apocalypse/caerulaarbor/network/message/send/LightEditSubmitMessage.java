package com.apocalypse.caerulaarbor.network.message.send;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.network.ModNetwork;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class LightEditSubmitMessage {
    private final double value;

    public LightEditSubmitMessage(double value) {
        this.value = value;
    }

    public static void encode(LightEditSubmitMessage msg, FriendlyByteBuf buf) {
        buf.writeDouble(msg.value);
    }

    public static LightEditSubmitMessage decode(FriendlyByteBuf buf) {
        return new LightEditSubmitMessage(buf.readDouble());
    }

    public static void handler(LightEditSubmitMessage msg, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ServerPlayer sender = ctx.get().getSender();
            if (sender == null) return;
            // only allow ops to use it in multiplayer
            if (sender.getServer() != null && sender.getServer().isDedicatedServer()) {
                if (!sender.hasPermissions(2)) {
                    return;
                }
            }
            var cap = ModCapabilities.getPlayerVariables(sender);
            cap.light = msg.value;
            cap.syncPlayerVariables(sender);
        });
        ctx.get().setPacketHandled(true);
    }
}