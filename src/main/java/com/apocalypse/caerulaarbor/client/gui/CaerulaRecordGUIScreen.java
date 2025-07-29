package com.apocalypse.caerulaarbor.client.gui;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.capability.sanity.SanityInjuryCapability;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import com.apocalypse.caerulaarbor.menu.CaerulaRecorderMenu;
import com.apocalypse.caerulaarbor.network.ModNetwork;
import com.apocalypse.caerulaarbor.network.message.send.CaerulaRecordGUIButtonMessage;
import com.apocalypse.caerulaarbor.procedures.GetOceanizeStateProcedure;
import com.apocalypse.caerulaarbor.procedures.GetSanityProcedure;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Checkbox;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import java.text.DecimalFormat;
import java.util.Optional;

public class CaerulaRecordGUIScreen extends AbstractContainerScreen<CaerulaRecorderMenu> {

    private final Player entity;
    Checkbox show_hud;
    Checkbox show_ptc;
    ImageButton imagebutton_relic_icon;

    public CaerulaRecordGUIScreen(CaerulaRecorderMenu container, Inventory inventory, Component text) {
        super(container, inventory, text);
        this.entity = container.player;
        this.imageWidth = 168;
        this.imageHeight = 166;
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTicks);
        if (entity == null) return;

        InventoryScreen.renderEntityInInventoryFollowsAngle(guiGraphics, this.leftPos + 86, this.topPos + 67, 30, 0f + (float) Math.atan((this.leftPos + 86 - mouseX) / 40.0), (float) Math.atan((this.topPos + 18 - mouseY) / 40.0), CaerulaRecordGUIScreen.this.entity);

        this.renderTooltip(guiGraphics, mouseX, mouseY);
        if (mouseX > leftPos + 2 && mouseX < leftPos + 26 && mouseY > topPos + 118 && mouseY < topPos + 142)
            guiGraphics.renderTooltip(font, Component.translatable("gui.caerula_arbor.caerula_record_gui.tooltip_occupy_warn"), mouseX, mouseY);
//        if (cap.disoclusion == 1)
//            if (mouseX > leftPos + 101 && mouseX < leftPos + 173 && mouseY > topPos + 147 && mouseY < topPos + 163)
//                guiGraphics.renderTooltip(font, Component.translatable("gui.caerula_arbor.caerula_record_gui.tooltip_discon"), mouseX, mouseY);
//        if (cap.disoclusion == 2)
//            if (mouseX > leftPos + 101 && mouseX < leftPos + 173 && mouseY > topPos + 147 && mouseY < topPos + 163)
//                guiGraphics.renderTooltip(font, Component.translatable("gui.caerula_arbor.caerula_record_gui.tooltip_haemp"), mouseX, mouseY);
        if (mouseX > leftPos + 6 && mouseX < leftPos + 22 && mouseY > topPos + 99 && mouseY < topPos + 115)
            guiGraphics.renderTooltip(font, Component.translatable("gui.caerula_arbor.caerula_record_gui.tooltip_show_relics"), mouseX, mouseY);
//        if (cap.disoclusion == 3)
//            if (mouseX > leftPos + 101 && mouseX < leftPos + 173 && mouseY > topPos + 147 && mouseY < topPos + 163)
//                guiGraphics.renderTooltip(font, Component.translatable("gui.caerula_arbor.caerula_record_gui.tooltip_neuro"), mouseX, mouseY);
        if (mouseX > leftPos + 5 && mouseX < leftPos + 16 && mouseY > topPos + 32 && mouseY < topPos + 43)
            guiGraphics.renderTooltip(font, Component.translatable("gui.caerula_arbor.caerula_record_gui.tooltip_playerlife"), mouseX, mouseY);
        if (mouseX > leftPos + 5 && mouseX < leftPos + 16 && mouseY > topPos + 53 && mouseY < topPos + 64)
            guiGraphics.renderTooltip(font, Component.translatable("gui.caerula_arbor.caerula_record_gui.tooltip_playershield"), mouseX, mouseY);
//        if (cap.disoclusion == 4)
//            if (mouseX > leftPos + 101 && mouseX < leftPos + 173 && mouseY > topPos + 147 && mouseY < topPos + 163)
//                guiGraphics.renderTooltip(font, Component.translatable("gui.caerula_arbor.caerula_record_gui.tooltip_flesh"), mouseX, mouseY);
        if (mouseX > leftPos + 136 && mouseX < leftPos + 161 && mouseY > topPos + 7 && mouseY < topPos + 27)
            guiGraphics.renderTooltip(font, Component.literal(GetOceanizeStateProcedure.execute(entity)), mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int gx, int gy) {
        RenderSystem.setShaderColor(1, 1, 1, 1);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/caerularecord.png"), this.leftPos, this.topPos, 0, 0, 168, 166, 168, 166);

        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/target_health.png"), this.leftPos + 4, this.topPos + 30, 0, 0, 24, 16, 24, 16);

        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/target_shield.png"), this.leftPos + 4, this.topPos + 50, 0, 0, 24, 16, 24, 16);

        var cap = entity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());

        if (cap.light >= 85) {
            guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/light.png"), this.leftPos + 36, this.topPos - 37, 0, 0, 64, 32, 64, 32);
        } else if (cap.light >= 50) {
            guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/light_waving.png"), this.leftPos + 36, this.topPos - 37, 0, 0, 64, 32, 64, 32);
        } else if (cap.light >= 1) {
            guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/light_dim.png"), this.leftPos + 36, this.topPos - 37, 0, 0, 64, 32, 64, 32);
        } else {
            guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/light_extinguish.png"), this.leftPos + 36, this.topPos - 37, 0, 0, 64, 32, 64, 32);
        }

        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/sanity.png"), this.leftPos + 106, this.topPos + 43, Mth.clamp(getSanityIndex(entity) * 16, 0, 304), 0, 16, 16, 320, 16);
//        switch (cap.disoclusion) {
//            case 1 ->
//                    guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/disoclution_attention.png"), this.leftPos + 96, this.topPos + 91, 0, 0, 64, 64, 64, 64);
//            case 2 ->
//                    guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/disoclution_blood.png"), this.leftPos + 101, this.topPos + 90, 0, 0, 64, 64, 64, 64);
//            case 3 ->
//                    guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/disoclution_neuro.png"), this.leftPos + 101, this.topPos + 90, 0, 0, 64, 64, 64, 64);
//            case 4 ->
//                    guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/disoclution_flesh.png"), this.leftPos + 99, this.topPos + 92, 0, 0, 64, 64, 64, 64);
//        }

        guiGraphics.blit(new ResourceLocation("caerula_arbor:textures/screens/oceanize_icon.png"), this.leftPos + 137, this.topPos + 7, Mth.clamp(cap.seabornization * 24, 0, 72), 0, 24, 20, 96, 20);

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
        var c = entity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());

        guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.caerula_record_gui.label_health1"), 45, 71, -10092442, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.caerula_record_gui.label_health"), 44, 71, -1, false);

        DecimalFormat twoDigit = new DecimalFormat("##.##");
        DecimalFormat oneDigit = new DecimalFormat("##.#");

        guiGraphics.drawString(this.font, twoDigit.format(entity.getHealth()) + "/" + oneDigit.format(entity.getMaxHealth()), 82, 71, -10092442, false);
        guiGraphics.drawString(this.font, twoDigit.format(entity.getHealth()) + "/" + oneDigit.format(entity.getMaxHealth()), 81, 71, -1, false);
        guiGraphics.drawString(this.font, "" + c.life, 18, 33, -16764058, false);
        guiGraphics.drawString(this.font, "" + c.life, 17, 33, -1, false);
        guiGraphics.drawString(this.font, "/" + c.maxLive, 36, 33, -16764058, false);
        guiGraphics.drawString(this.font, "/" + c.maxLive, 35, 33, -16724737, false);
        guiGraphics.drawString(this.font, "" + c.shield, 18, 53, -13421773, false);
        guiGraphics.drawString(this.font, "" + c.shield, 17, 53, -1, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.caerula_record_gui.label_discolution"), 102, 86, -13434829, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.caerula_record_gui.label_disoclution"), 101, 86, -3368449, false);
        guiGraphics.drawString(this.font, twoDigit.format(entity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable()).light), 100, -13, -1, false);
        if (c.light >= 85)
            guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.caerula_record_gui.label_lightsablaze"), 100, -29, -3342337, false);
        else if (c.light >= 50)
            guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.caerula_record_gui.label_lightsflickering"), 100, -29, -3342388, false);
        else if (c.light >= 1)
            guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.caerula_record_gui.label_lightsdim"), 100, -29, -13159, false);
        else
            guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.caerula_record_gui.label_lightstranquil"), 100, -29, -26215, false);

//        switch (c.disoclusion) {
//            case 1 ->
//                    guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.caerula_record_gui.label_disconcentration"), 101, 147, -3368449, false);
//            case 2 ->
//                    guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.caerula_record_gui.label_haemophilia"), 101, 147, -3368449, false);
//            case 3 ->
//                    guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.caerula_record_gui.label_neurodegression"), 101, 147, -3368449, false);
//            case 4 ->
//                    guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.caerula_record_gui.label_deformity"), 101, 147, -3368449, false);
//        }

        guiGraphics.drawString(this.font, GetSanityProcedure.execute(entity), 124, 50, -16737895, false);
        guiGraphics.drawString(this.font, GetSanityProcedure.execute(entity), 123, 50, -1, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.caerula_record_gui.label_sanity1"), 124, 41, -16737895, false);
        guiGraphics.drawString(this.font, Component.translatable("gui.caerula_arbor.caerula_record_gui.label_sanity"), 123, 41, -1, false);
    }

    @Override
    public void init() {
        super.init();
        imagebutton_relic_icon = new ImageButton(this.leftPos + 6, this.topPos + 99, 16, 16, 0, 0, 16, new ResourceLocation("caerula_arbor:textures/screens/atlas/imagebutton_relic_icon.png"), 16, 32, e -> ModNetwork.PACKET_HANDLER.sendToServer(new CaerulaRecordGUIButtonMessage(0)));
//        guistate.put("button:imagebutton_relic_icon", imagebutton_relic_icon);
        this.addRenderableWidget(imagebutton_relic_icon);
        boolean result = false;
        if (entity != null) {
            var cap = entity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());
            result = cap.show_stats;
        }
        show_hud = new Checkbox(this.leftPos + 4, this.topPos + 120, 20, 20, Component.translatable("gui.caerula_arbor.caerula_record_gui.show_hud"), result);
//        guistate.put("checkbox:show_hud", show_hud);
        this.addRenderableWidget(show_hud);
        show_ptc = new Checkbox(this.leftPos + 4, this.topPos + 142, 20, 20, Component.translatable("gui.caerula_arbor.caerula_record_gui.show_ptc"), entity.getCapability(ModCapabilities.PLAYER_VARIABLE)
                .map(c -> c.kingShowPtc)
                .orElse(false));
//        guistate.put("checkbox:show_ptc", show_ptc);
        this.addRenderableWidget(show_ptc);
    }

    /**
     * @param player 目标玩家
     * @return 0-19的int，代表损伤条从空到满
     */
    private static int getSanityIndex(Player player) {
        var effect = Optional.ofNullable(player.getEffect(ModMobEffects.SANITY_IMMUNE.get()));
        if (effect.isPresent()) {
            return Math.min(effect.get().getDuration() / 10, 19);
        }
        var capability = player.getCapability(ModCapabilities.SANITY_INJURY).resolve();
        if (capability.isPresent() && capability.get() instanceof SanityInjuryCapability capabilityImpl) {
            return Math.min((int) capabilityImpl.getValue() / 50, 19);
        }
        return 0;
    }
}
