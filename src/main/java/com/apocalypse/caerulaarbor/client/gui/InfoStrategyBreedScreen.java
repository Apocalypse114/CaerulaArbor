package com.apocalypse.caerulaarbor.client.gui;

import com.apocalypse.caerulaarbor.capability.map.MapVariables;
import com.apocalypse.caerulaarbor.entity.BasinSeaReaperEntity;
import com.apocalypse.caerulaarbor.init.ModEntities;
import com.apocalypse.caerulaarbor.menu.InfoStrategyBreedMenu;
import com.apocalypse.caerulaarbor.network.ModNetwork;
import com.apocalypse.caerulaarbor.network.message.send.InfoStrategyBreedButtonMessage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public class InfoStrategyBreedScreen extends AbstractContainerScreen<InfoStrategyBreedMenu> {
    private final static HashMap<String, Object> guistate = InfoStrategyBreedMenu.guistate;
    private final Level world;
    private final int x, y, z;
    private final Player entity;
    Button button_return;

    public InfoStrategyBreedScreen(InfoStrategyBreedMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.world = container.world;
        this.x = container.x;
        this.y = container.y;
        this.z = container.z;
        this.entity = container.entity;
        this.imageWidth = 256;
        this.imageHeight = 168;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        if ((Entity) new BasinSeaReaperEntity(ModEntities.BASIN_SEA_REAPER.get(), world) instanceof LivingEntity livingEntity) {
            InventoryScreen.renderEntityInInventoryFollowsAngle(guiGraphics, this.leftPos + 29, this.topPos + 96, 20, 0f + (float) Math.atan((this.leftPos + 29 - mouseX) / 40.0), (float) Math.atan((this.topPos + 47 - mouseY) / 40.0), livingEntity);
        }
        this.renderTooltip(guiGraphics, mouseX, mouseY);
//        if (mouseX > leftPos + 244 && mouseX < leftPos + 253 && mouseY > topPos + 20 && mouseY < topPos + 92)
//            guiGraphics.renderTooltip(font, Component.literal(GetPointBreedProcedure.execute(world)), mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/sidebar.png"), this.leftPos - 3, this.topPos - 3, 0, 0, 262, 174, 262, 174);
        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/bg_breed.png"), this.leftPos, this.topPos, Mth.clamp((int) MapVariables.get(world).strategyBreed * 256, 0, 1024), 0, 256, 168, 1280, 168);
//        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/barevo.png"), this.leftPos + 244, this.topPos + 20, Mth.clamp((int) GetBarBreedProcedure.execute(world) * 8, 0, 144), 0, 8, 72, 152, 72);

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
        guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.info_strategy_breed.label_strategy_breed"), 1, -12, -16717080, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.info_strategy_breed.label_to_breed"), 1, 4, -1, false);
        guiGraphics.drawString(this.font, Component.translatable(("item.caerula_arbor.sample_breed.description_" + Math.round(MapVariables.get(world).strategyBreed))).getString(), 1, 100, -1, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.info_strategy_breed.label_proceed"), 1, 172, -1, false);
        if (MapVariables.get(world).strategySilence > 0)
            guiGraphics.drawString(this.font, Component.translatable(("item.caerula_arbor.sample_breed.description_" + Math.round(MapVariables.get(world).strategySilence + 5))).getString(), 1, 116, -3407872, false);
    }

    @Override
    public void init() {
        super.init();
        button_return = new PlainTextButton(this.leftPos + 226, this.topPos + 156, 36, 20, Component.translatable("gui.caerula_arbor.info_strategy_breed.button_return"), e -> {
            ModNetwork.PACKET_HANDLER.sendToServer(new InfoStrategyBreedButtonMessage(0, x, y, z));
            InfoStrategyBreedButtonMessage.handleButtonAction(entity, 0, x, y, z);
        }, this.font);
        guistate.put("button:button_return", button_return);
        this.addRenderableWidget(button_return);
    }
}
