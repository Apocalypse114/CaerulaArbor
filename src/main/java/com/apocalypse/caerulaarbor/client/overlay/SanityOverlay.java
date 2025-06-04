package com.apocalypse.caerulaarbor.client.overlay;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;

@OnlyIn(Dist.CLIENT)
public class SanityOverlay implements IGuiOverlay {

    public static final String ID = CaerulaArborMod.MODID + "_sanity_overlay";

    @Override
    public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
        Player player = gui.getMinecraft().player;
        if (player == null) return;

        player.getCapability(ModCapabilities.SANITY_INJURY).ifPresent(
                cap -> guiGraphics.blit(CaerulaArborMod.loc("textures/screens/sanity.png"),
                        screenWidth / 2 + 92, screenHeight - 19,
                        Mth.clamp((int) Math.ceil(cap.getValue() / 50) * 16, 0, 304),
                        0, 16, 16, 320, 16)
        );
    }
}
