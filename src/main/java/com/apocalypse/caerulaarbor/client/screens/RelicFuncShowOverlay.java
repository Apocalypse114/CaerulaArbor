
package com.apocalypse.caerulaarbor.client.screens;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class RelicFuncShowOverlay {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        var player = Minecraft.getInstance().player;
        if (player == null) return;

        var cap = ModCapabilities.getPlayerVariables(player);
        if (cap.kingShowPtc) return;

        event.getGuiGraphics().blit(new ResourceLocation("caerula_arbor:textures/screens/icon_king.png"), 6, 8, Mth.clamp(cap.player_king_suit * 16, 0, 32), 0, 16, 16, 48, 16);
        event.getGuiGraphics().blit(new ResourceLocation("caerula_arbor:textures/screens/icon_artifi.png"), 22, 8, Mth.clamp(cap.player_demon_suit * 16, 0, 32), 0, 16, 16, 48, 16);

        if (player.hasEffect(ModMobEffects.TIDE_OF_CHITIN.get())) {
            event.getGuiGraphics().blit(new ResourceLocation("caerula_arbor:textures/screens/icon_chitin.png"), 38, 8, 0, 0, 16, 16, 16, 16);
        }
    }
}
