package com.apocalypse.caerulaarbor.client.gui;

import com.apocalypse.caerulaarbor.network.ModNetwork;
import com.apocalypse.caerulaarbor.network.message.send.LightEditSubmitMessage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class LightEditScreen extends Screen {

    private EditBox input;

    public LightEditScreen(Component title) {
        super(title);
    }

    public static void open(Component title) {
        Minecraft.getInstance().setScreen(new LightEditScreen(title));
    }

    @Override
    protected void init() {
        super.init();
        int w = 200;
        int h = 20;
        int cx = this.width / 2;
        int cy = this.height / 2;

        input = new EditBox(this.font, cx - w / 2, cy - h - 10, w, h, Component.translatable("screen.caerula_arbor.light_edit.input"));
        input.setMaxLength(32);
        input.setValue("");
        addRenderableWidget(input);

        addRenderableWidget(Button.builder(Component.translatable("gui.done"), b -> submit())
                .bounds(cx - 100, cy + 10, 90, 20)
                .build());
        addRenderableWidget(Button.builder(Component.translatable("gui.cancel"), b -> onClose())
                .bounds(cx + 10, cy + 10, 90, 20)
                .build());
    }

    private void submit() {
        try {
            double val = Double.parseDouble(input.getValue().trim());
            ModNetwork.PACKET_HANDLER.sendToServer(new LightEditSubmitMessage(val));
            onClose();
        } catch (NumberFormatException ignored) {
            // keep screen, maybe show error later
        }
    }

    @Override
    public void render(GuiGraphics gg, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(gg);
        super.render(gg, mouseX, mouseY, partialTicks);
        gg.drawCenteredString(this.font, this.title, this.width / 2, this.height / 2 - 40, 0xFFFFFF);
        input.render(gg, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}