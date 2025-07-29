package com.apocalypse.caerulaarbor.event;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getInstance();
        var player = mc.player;
        if (player == null) return;

        handleRejection(player);
    }

    /**
     * 触发排异反应“专注失调”时，有1%的概率随机触发一次左键或右键
     *
     * @param player 客户端玩家
     */
    private static void handleRejection(LocalPlayer player) {
        var variable = ModCapabilities.getPlayerVariables(player);
        if (variable.isRejectionInvoked(PlayerVariable.Rejection.CONCENTRATION_DISORDER)) {
            if (player.getRandom().nextDouble() <= 0.01) {
                if (player.getRandom().nextDouble() > 0.5) {
                    KeyMapping.click(Minecraft.getInstance().options.keyUse.getKey());
                } else {
                    KeyMapping.click(Minecraft.getInstance().options.keyAttack.getKey());
                }
            }
        }
    }
}
