package com.apocalypse.caerulaarbor.client.gui;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.menu.RelicShowcaseMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class RelicShowcaseScreen extends AbstractContainerScreen<RelicShowcaseMenu> {

    private static final ResourceLocation RELIC_SHOWCASE = CaerulaArborMod.loc("textures/screens/relic_showcase.png");

    private final Player player;

    public RelicShowcaseScreen(RelicShowcaseMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.player = container.player;
        this.imageWidth = 328;
        this.imageHeight = 216;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        var cap = ModCapabilities.getPlayerVariables(player);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        var poseStack = guiGraphics.pose();

        poseStack.pushPose();

        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        var cap = ModCapabilities.getPlayerVariables(player);

        guiGraphics.blit(RELIC_SHOWCASE, this.leftPos, this.topPos, 0, 0, 328, 216, 328, 216);

        RenderSystem.disableBlend();

        poseStack.popPose();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256 && this.minecraft != null) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, b, c);
    }

    @Override
    protected void renderLabels(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.relic_showcase.label_relic_showcase"), 4, -12, -1, false);

        var cap = ModCapabilities.getPlayerVariables(player);
    }

    @Override
    public void init() {
        super.init();
        Button button_return = new PlainTextButton(this.leftPos + 292, this.topPos + 204, 24, 20, Component.translatable("gui.caerula_arbor.relic_showcase.button_return"), e -> {
//            CaerulaArborMod.PACKET_HANDLER.sendToServer(new RelicShowcaseButtonMessage(0, x, y, z));
//            RelicShowcaseButtonMessage.handleButtonAction(player, 0, x, y, z);
        }, this.font);
        this.addRenderableWidget(button_return);
    }
}
