package com.apocalypse.caerulaarbor.client.overlay;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class SanityOverlay implements IGuiOverlay {

    public static final String ID = CaerulaArborMod.MODID + "_sanity_overlay";
    public static final ResourceLocation SANITY = CaerulaArborMod.loc("textures/screens/sanity.png");

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Player player = gui.getMinecraft().player;
        if (player == null) return;

        var cap = ModCapabilities.getSanityInjury(player);
        // 与 CaerulaRecordGUIScreen 统一：按当前能力值计算分段，向下取整到 0-19 段
        int index = Mth.clamp((int) Mth.floor((float) (cap.getValue() / 50.0f)), 0, 19);
        guiGraphics.blit(
                SANITY,
                screenWidth / 2 + 92, screenHeight - 19,
                index * 16,
                0, 16, 16, 320, 16
        );
    }
}
