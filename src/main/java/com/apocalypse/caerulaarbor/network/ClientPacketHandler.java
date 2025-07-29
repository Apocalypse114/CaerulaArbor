package com.apocalypse.caerulaarbor.network;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.network.message.receive.PlayerVariablesSyncMessage;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {

    public static void handlePlayerVariablesSync(PlayerVariablesSyncMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() == LogicalSide.CLIENT) {
            var player = Minecraft.getInstance().player;
            if (player == null) return;

            PlayerVariable variables = ModCapabilities.getPlayerVariables(player);
            variables.light = message.data().light;
            variables.life = message.data().life;
            variables.maxLive = message.data().maxLive;
            variables.shield = message.data().shield;
            variables.rejection = message.data().rejection;

            variables.show_stats = message.data().show_stats;
            variables.kingShowPtc = message.data().kingShowPtc;

            for (var relic : Relic.values()) {
                relic.set(variables, message.data().relics.getOrDefault(relic, relic.defaultLevel));
            }

            variables.chitin_knife_selected = message.data().chitin_knife_selected;
            variables.player_king_suit = message.data().player_king_suit;
            variables.player_demon_suit = message.data().player_demon_suit;
            variables.seabornization = message.data().seabornization;
        }
    }
}
