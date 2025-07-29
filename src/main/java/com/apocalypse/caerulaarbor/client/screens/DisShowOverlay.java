
package com.apocalypse.caerulaarbor.client.screens;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber({Dist.CLIENT})
public class DisShowOverlay {
    @SubscribeEvent(priority = EventPriority.NORMAL)
    public static void eventHandler(RenderGuiEvent.Pre event) {
        int w = event.getWindow().getGuiScaledWidth();
        int h = event.getWindow().getGuiScaledHeight();

        var player = Minecraft.getInstance().player;
        if (player == null) return;

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        RenderSystem.setShaderColor(1, 1, 1, 1);

        var cap = player.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());

//        if (cap.show_stats && cap.disoclusion > 0) {
//            event.getGuiGraphics().blit(new ResourceLocation("caerula_arbor:textures/screens/disoclution_bg.png"), w - 64, h - 128, 0, 0, 64, 128, 64, 128);
//
//            var img = switch (cap.disoclusion) {
//                case 1 -> "disoclution_attention.png";
//                case 2 -> "disoclution_blood.png";
//                case 3 -> "disoclution_neuro.png";
//                case 4 -> "disoclution_flesh.png";
//
//                default -> throw new IllegalStateException("Unexpected value: " + cap.disoclusion);
//            };
//
//            event.getGuiGraphics().blit(new ResourceLocation("caerula_arbor:textures/screens/" + img), w - 69, h - 72, 0, 0, 64, 64, 64, 64);
//        }
        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor(1, 1, 1, 1);
    }
}
