package com.apocalypse.caerulaarbor.event;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.client.screens.TideObservationScreen;
import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) return;

        Minecraft mc = Minecraft.getInstance();
        var player = mc.player;
        if (player == null) return;

        handleRejection(player);
    }

    /**
     * 触发排异反应“专注失调”时，有1%的概率随机触发一次左键或右键
     *
     * @param player 客户端玩家
     */
    private static void handleRejection(LocalPlayer player) {
        var variable = ModCapabilities.getPlayerVariables(player);
        if (variable.isRejectionInvoked(PlayerVariable.Rejection.CONCENTRATION_DISORDER)) {
            if (player.getRandom().nextDouble() <= 0.01) {
                if (player.getRandom().nextDouble() > 0.5) {
                    KeyMapping.click(Minecraft.getInstance().options.keyUse.getKey());
                } else {
                    KeyMapping.click(Minecraft.getInstance().options.keyAttack.getKey());
                }
            }
        }
    }

    /**
     * 处理物品悬浮提示事件，当玩家灯火值小于50时，在“时运”附魔行后追加“（厄运缠身）”。
     */
    @SubscribeEvent
    public static void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.getItemStack();
        if (event.getEntity() == null) return;

        int fortuneLevel = stack.getEnchantmentLevel(Enchantments.BLOCK_FORTUNE);
        if (fortuneLevel <= 0) return;

        var cap = ModCapabilities.getPlayerVariables(event.getEntity());
        if (cap.light >= 50) return;

        // 期望的“时运X”文本（当前语言环境下）
        String target = Enchantments.BLOCK_FORTUNE.getFullname(fortuneLevel).getString();

        var tooltip = event.getToolTip();
        for (int i = 0; i < tooltip.size(); i++) {
            Component line = tooltip.get(i);
            if (line.getString().equals(target)) {
                Component replaced = line.copy().append(Component.literal("（厄运缠身）").withStyle(ChatFormatting.DARK_RED));
                tooltip.set(i, replaced);
                break;
            }
        }
    }

    public static void openTideObservationStation() {
        Minecraft.getInstance().setScreen(new TideObservationScreen());
    }
}
