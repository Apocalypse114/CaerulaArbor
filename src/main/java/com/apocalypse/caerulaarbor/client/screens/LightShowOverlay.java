
package com.apocalypse.caerulaarbor.client.screens;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class LightShowOverlay {
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();
        Player entity = Minecraft.getInstance().player;
        if (entity == null) {
            return;
        }

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);
        var cap = ModCapabilities.getPlayerVariables(entity);
        if (cap.show_stats) {
            if (cap.light < 1) {
                event.getGuiGraphics().blit(new ResourceLocation("caerula_arbor:textures/screens/light_extinguish.png"), w / 2 - 32, 0, 0, 0, 64, 32, 64, 32);
            } else if (cap.light < 50) {
                event.getGuiGraphics().blit(new ResourceLocation("caerula_arbor:textures/screens/light_dim.png"), w / 2 - 32, 0, 0, 0, 64, 32, 64, 32);
            } else if (cap.light < 85) {
                event.getGuiGraphics().blit(new ResourceLocation("caerula_arbor:textures/screens/light_waving.png"), w / 2 - 32, 0, 0, 0, 64, 32, 64, 32);
            } else {
                event.getGuiGraphics().blit(new ResourceLocation("caerula_arbor:textures/screens/light.png"), w / 2 - 32, 0, 0, 0, 64, 32, 64, 32);
            }

            event.getGuiGraphics().blit(new ResourceLocation("caerula_arbor:textures/screens/target_health.png"), 6, h - 24, 0, 0, 24, 16, 24, 16);

            if (cap.shield > 0) {
                event.getGuiGraphics().blit(new ResourceLocation("caerula_arbor:textures/screens/target_shield.png"), 6, h - 40, 0, 0, 24, 16, 24, 16);
            }
            event.getGuiGraphics().drawString(Minecraft.getInstance().font, "" + cap.light, w / 2 - 5, 26, -13421773, false);
            event.getGuiGraphics().drawString(Minecraft.getInstance().font, "" + cap.light, w / 2 - 6, 26, -1, false);
            event.getGuiGraphics().drawString(Minecraft.getInstance().font, "" + cap.maxLive, 37, h - 21, -16764109, false);
            event.getGuiGraphics().drawString(Minecraft.getInstance().font, "" + cap.maxLive, 36, h - 21, -10040065, false);
            event.getGuiGraphics().drawString(Minecraft.getInstance().font, "" + cap.life, 21, h - 21, -16764109, false);
            event.getGuiGraphics().drawString(Minecraft.getInstance().font, "" + cap.life, 20, h - 21, -1, false);
            if (cap.shield > 0) {

                event.getGuiGraphics().drawString(Minecraft.getInstance().font, "" + cap.shield, 21, h - 36, -16777216, false);
                event.getGuiGraphics().drawString(Minecraft.getInstance().font, "" + cap.shield, 20, h - 36, -1, false);
            }
        }
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
