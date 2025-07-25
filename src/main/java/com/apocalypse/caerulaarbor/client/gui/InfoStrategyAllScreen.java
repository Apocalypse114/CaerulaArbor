package com.apocalypse.caerulaarbor.client.gui;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.map.MapVariables;
import com.apocalypse.caerulaarbor.menu.InfoStrategyAllMenu;
import com.apocalypse.caerulaarbor.network.InfoStrategyAllButtonMessage;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
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

public class InfoStrategyAllScreen extends AbstractContainerScreen<InfoStrategyAllMenu> {
    private final static HashMap<String, Object> guistate = InfoStrategyAllMenu.guistate;
    private final Level world;
    private final int x, y, z;
    private final Player entity;
    Button button_evolution_tree;
    ImageButton imagebutton_breed_lit;
    ImageButton imagebutton_grow_lit;
    ImageButton imagebutton_mig_lit;
    ImageButton imagebutton_subs_lit;

    public InfoStrategyAllScreen(InfoStrategyAllMenu container, Inventory inventory, Component text) {
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
        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (mouseX > leftPos + 37 && mouseX < leftPos + 95 && mouseY > topPos && mouseY < topPos + 44)
            guiGraphics.renderTooltip(font, Component.translatable("gui.caerula_arbor.info_strategy_all.tooltip_grow"), mouseX, mouseY);
        if (mouseX > leftPos + 149 && mouseX < leftPos + 199 && mouseY > topPos + 20 && mouseY < topPos + 72)
            guiGraphics.renderTooltip(font, Component.translatable("gui.caerula_arbor.info_strategy_all.tooltip_subs"), mouseX, mouseY);
        if (mouseX > leftPos + 64 && mouseX < leftPos + 114 && mouseY > topPos + 87 && mouseY < topPos + 141)
            guiGraphics.renderTooltip(font, Component.translatable("gui.caerula_arbor.info_strategy_all.tooltip_breed"), mouseX, mouseY);
        if (mouseX > leftPos + 179 && mouseX < leftPos + 234 && mouseY > topPos + 101 && mouseY < topPos + 164)
            guiGraphics.renderTooltip(font, Component.translatable("gui.caerula_arbor.info_strategy_all.tooltip_mig"), mouseX, mouseY);
//        if (IfCanSilenceProcedure.execute(world))
//            if (mouseX > leftPos - 14 && mouseX < leftPos + 10 && mouseY > topPos - 12 && mouseY < topPos + 12)
//                guiGraphics.renderTooltip(font, Component.literal(GetPointSilenceProcedure.execute(world)), mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/wetplayer.png"), this.leftPos, this.topPos, 0, 0, 256, 168, 256, 168);
        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/silence.png"), this.leftPos - 17, this.topPos - 18, Mth.clamp((int) MapVariables.get(world).strategySilence * 29, 0, 116), 0, 29, 33, 145, 33);

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
        guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.info_strategy_all.label_tide_observation"), 12, -10, -11801895, false);
    }

    @Override
    public void init() {
        super.init();
        button_evolution_tree = new PlainTextButton(this.leftPos, this.topPos + 169, 76, 20, Component.translatable("gui.caerula_arbor.info_strategy_all.button_evolution_tree"), e -> {
            CaerulaArborMod.PACKET_HANDLER.sendToServer(new InfoStrategyAllButtonMessage(0, x, y, z));
            InfoStrategyAllButtonMessage.handleButtonAction(entity, 0, x, y, z);
        }, this.font);
        guistate.put("button:button_evolution_tree", button_evolution_tree);
        this.addRenderableWidget(button_evolution_tree);
        imagebutton_breed_lit = new ImageButton(this.leftPos + 53, this.topPos + 80, 76, 76, 0, 0, 76, new ResourceLocation("caerula_arbor:textures/screens/atlas/imagebutton_breed_lit.png"), 76, 152, e -> {
            CaerulaArborMod.PACKET_HANDLER.sendToServer(new InfoStrategyAllButtonMessage(1, x, y, z));
            InfoStrategyAllButtonMessage.handleButtonAction(entity, 1, x, y, z);
        });
        guistate.put("button:imagebutton_breed_lit", imagebutton_breed_lit);
        this.addRenderableWidget(imagebutton_breed_lit);
        imagebutton_grow_lit = new ImageButton(this.leftPos + 32, this.topPos, 65, 47, 0, 0, 47, new ResourceLocation("caerula_arbor:textures/screens/atlas/imagebutton_grow_lit.png"), 65, 94, e -> {
            CaerulaArborMod.PACKET_HANDLER.sendToServer(new InfoStrategyAllButtonMessage(2, x, y, z));
            InfoStrategyAllButtonMessage.handleButtonAction(entity, 2, x, y, z);
        });
        guistate.put("button:imagebutton_grow_lit", imagebutton_grow_lit);
        this.addRenderableWidget(imagebutton_grow_lit);
        imagebutton_mig_lit = new ImageButton(this.leftPos + 164, this.topPos + 86, 78, 82, 0, 0, 82, new ResourceLocation("caerula_arbor:textures/screens/atlas/imagebutton_mig_lit.png"), 78, 164, e -> {
            CaerulaArborMod.PACKET_HANDLER.sendToServer(new InfoStrategyAllButtonMessage(3, x, y, z));
            InfoStrategyAllButtonMessage.handleButtonAction(entity, 3, x, y, z);
        });
        guistate.put("button:imagebutton_mig_lit", imagebutton_mig_lit);
        this.addRenderableWidget(imagebutton_mig_lit);
        imagebutton_subs_lit = new ImageButton(this.leftPos + 143, this.topPos + 12, 62, 62, 0, 0, 62, new ResourceLocation("caerula_arbor:textures/screens/atlas/imagebutton_subs_lit.png"), 62, 124, e -> {
            CaerulaArborMod.PACKET_HANDLER.sendToServer(new InfoStrategyAllButtonMessage(4, x, y, z));
            InfoStrategyAllButtonMessage.handleButtonAction(entity, 4, x, y, z);
        });
        guistate.put("button:imagebutton_subs_lit", imagebutton_subs_lit);
        this.addRenderableWidget(imagebutton_subs_lit);
    }
}
