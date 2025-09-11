package com.apocalypse.caerulaarbor.network;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.capability.sanity.ISanityInjuryCapability;
import com.apocalypse.caerulaarbor.network.message.receive.PlayerVariablesSyncMessage;
import com.apocalypse.caerulaarbor.network.message.receive.SanityInjurySyncMessage;
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
            variables.seabornization = message.data().seabornization;

            for (var relic : Relic.values()) {
                relic.set(variables, message.data().relics.getOrDefault(relic, relic.defaultLevel));
            }
        }
    }

    public static void handleSanityInjurySync(SanityInjurySyncMessage message, Supplier<NetworkEvent.Context> ctx) {
        if (ctx.get().getDirection().getReceptionSide() != LogicalSide.CLIENT) return;
        var mc = Minecraft.getInstance();
        var player = mc.player;
        if (player == null) return;
        ISanityInjuryCapability cap = ModCapabilities.getSanityInjury(player);
        if (cap == null) return;
        cap.deserializeNBT(message.data());
        // 触发 HUD 刷新（如果 HUD 监听 RenderLevelStageEvent/RenderGuiOverlayEvent 读取能力值，则自然刷新；否则可以考虑发布一个自定义事件）
    }
}
