package com.apocalypse.caerulaarbor.client.gui;

import com.apocalypse.caerulaarbor.capability.map.MapVariables;
import com.apocalypse.caerulaarbor.menu.EvoTreeMenu;
import com.apocalypse.caerulaarbor.network.ModNetwork;
import com.apocalypse.caerulaarbor.network.message.send.EvoTreeButtonMessage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class EvoTreeScreen extends AbstractContainerScreen<EvoTreeMenu> {
    private final static HashMap<String, Object> guistate = EvoTreeMenu.guistate;
    private final Level world;
    private final int x, y, z;
    private final Player entity;
    Button button_return;

    public EvoTreeScreen(EvoTreeMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.world = container.world;
        this.x = container.x;
        this.y = container.y;
        this.z = container.z;
        this.entity = container.entity;
        this.imageWidth = 256;
        this.imageHeight = 216;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        MapVariables variable = MapVariables.get(world);

        if (mouseX > leftPos + 33 && mouseX < leftPos + 158 && mouseY > topPos + 108 && mouseY < topPos + 140)
            guiGraphics.renderTooltip(font, Component.literal(Component.translatable("item.caerula_arbor.sample_grow.description_" + Math.round(variable.strategyGrow)).getString()), mouseX, mouseY);
        if (mouseX > leftPos + 33 && mouseX < leftPos + 158 && mouseY > topPos + 68 && mouseY < topPos + 100)
            guiGraphics.renderTooltip(font, Component.literal(Component.translatable("item.caerula_arbor.sample_breed.description_" + Math.round(variable.strategyBreed)).getString()), mouseX, mouseY);
        if (mouseX > leftPos + 33 && mouseX < leftPos + 158 && mouseY > topPos + 28 && mouseY < topPos + 61)
            guiGraphics.renderTooltip(font, Component.literal(Component.translatable("item.caerula_arbor.sample_subsisting.description_" + Math.round(variable.strategySubsisting)).getString()), mouseX, mouseY);
        if (mouseX > leftPos + 33 && mouseX < leftPos + 158 && mouseY > topPos + 148 && mouseY < topPos + 181)
            guiGraphics.renderTooltip(font, Component.literal(Component.translatable("item.caerula_arbor.sample_migration.description_" + Math.round(variable.strategyMigration)).getString()), mouseX, mouseY);
        if (MapVariables.get(world).strategySilence > 0)
            if (mouseX > leftPos + 176 && mouseX < leftPos + 200 && mouseY > topPos + 33 && mouseY < topPos + 57)
                guiGraphics.renderTooltip(font, Component.literal(Component.translatable("item.caerula_arbor.sample_subsisting.description_" + Math.round(MapVariables.get(world).strategySilence + 5)).getString()), mouseX, mouseY);
        if (MapVariables.get(world).strategySilence > 0)
            if (mouseX > leftPos + 175 && mouseX < leftPos + 196 && mouseY > topPos + 116 && mouseY < topPos + 133)
                guiGraphics.renderTooltip(font, Component.literal(Component.translatable("item.caerula_arbor.sample_grow.description_" + Math.round(MapVariables.get(world).strategySilence + 5)).getString()), mouseX, mouseY);
        if (MapVariables.get(world).strategySilence > 0)
            if (mouseX > leftPos + 175 && mouseX < leftPos + 193 && mouseY > topPos + 78 && mouseY < topPos + 92)
                guiGraphics.renderTooltip(font, Component.literal(Component.translatable("item.caerula_arbor.sample_breed.description_" + Math.round(MapVariables.get(world).strategySilence + 5)).getString()), mouseX, mouseY);
        if (MapVariables.get(world).strategySilence > 0)
            if (mouseX > leftPos + 175 && mouseX < leftPos + 199 && mouseY > topPos + 151 && mouseY < topPos + 175)
                guiGraphics.renderTooltip(font, Component.literal(Component.translatable("item.caerula_arbor.sample_migration.description_" + Math.round(MapVariables.get(world).strategySilence + 5)).getString()), mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/evo_arrow.png"), this.leftPos + 14, this.topPos + 40, Mth.clamp((int) ((double) (MapVariables.get(world).strategySubsisting >= 4 ? 1 : 0)) * 161, 0, 161), 0, 161, 8, 322, 8);
        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/evo_arrow.png"), this.leftPos + 14, this.topPos + 80, Mth.clamp((int) ((double) (MapVariables.get(world).strategyBreed >= 4 ? 1 : 0)) * 161, 0, 161), 0, 161, 8, 322, 8);
        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/evo_arrow.png"), this.leftPos + 14, this.topPos + 120, Mth.clamp((int) ((double) (MapVariables.get(world).strategyGrow >= 4 ? 1 : 0)) * 161, 0, 161), 0, 161, 8, 322, 8);
        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/evo_arrow.png"), this.leftPos + 14, this.topPos + 160, Mth.clamp((int) ((double) (MapVariables.get(world).strategyMigration >= 4 ? 1 : 0)) * 161, 0, 161), 0, 161, 8, 322, 8);
        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/evo_complete.png"), this.leftPos + 32, this.topPos + 28, Mth.clamp((int) MapVariables.get(world).strategySubsisting * 128, 0, 512), 0, 128, 33, 640, 33);
        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/evo_complete.png"), this.leftPos + 32, this.topPos + 68, Mth.clamp((int) MapVariables.get(world).strategyBreed * 128, 0, 512), 0, 128, 33, 640, 33);
        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/evo_complete.png"), this.leftPos + 32, this.topPos + 108, Mth.clamp((int) MapVariables.get(world).strategyGrow * 128, 0, 512), 0, 128, 33, 640, 33);
        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/evo_complete.png"), this.leftPos + 32, this.topPos + 148, Mth.clamp((int) MapVariables.get(world).strategyMigration * 128, 0, 512), 0, 128, 33, 640, 33);
        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/side_arrow.png"), this.leftPos + 176, this.topPos + 42, 0, Mth.clamp((int) ((double) (MapVariables.get(world).strategySilence >= 1 ? 1 : 0)) * 123, 0, 123), 39, 123, 39, 246);
        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/silence.png"), this.leftPos + 208, this.topPos + 86, Mth.clamp((int) MapVariables.get(world).strategySilence * 29, 0, 116), 0, 29, 33, 145, 33);

        RenderSystem.disableBlend();
    }

    @Override
    public boolean keyPressed(int key, int b, int c) {
        if (key == 256) {
            this.minecraft.player.closeContainer();
            return true;
        }
        return super.keyPressed(key, b, c);
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.evo_tree.label_strategy_subsisting"), -24, 31, -16724737, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.evo_tree.label_strategy_breed"), -23, 71, -16724737, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.evo_tree.label_sreategy_grow"), -23, 111, -16724737, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.evo_tree.label_strategy_migration"), -23, 151, -16724737, false);
    }

    @Override
    public void init() {
        super.init();
        button_return = new PlainTextButton(this.leftPos + 218, this.topPos + 203, 38, 20, Component.translatable("gui.caerula_arbor.evo_tree.button_return"), e -> {
            ModNetwork.PACKET_HANDLER.sendToServer(new EvoTreeButtonMessage(0, x, y, z));
            EvoTreeButtonMessage.handleButtonAction(entity, 0, x, y, z);
        }, this.font);
        guistate.put("button:button_return", button_return);
        this.addRenderableWidget(button_return);
    }
}
