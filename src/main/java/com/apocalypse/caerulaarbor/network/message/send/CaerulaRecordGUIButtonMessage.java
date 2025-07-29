package com.apocalypse.caerulaarbor.network.message.send;

import com.apocalypse.caerulaarbor.menu.RelicShowcaseMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CaerulaRecordGUIButtonMessage {

    private final int buttonID;

    public CaerulaRecordGUIButtonMessage(int buttonID) {
        this.buttonID = buttonID;
    }

    public static CaerulaRecordGUIButtonMessage decode(FriendlyByteBuf buffer) {
        return new CaerulaRecordGUIButtonMessage(buffer.readInt());
    }

    public static void encode(CaerulaRecordGUIButtonMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.buttonID);
    }

    public static void handler(CaerulaRecordGUIButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            ServerPlayer entity = context.getSender();
            if (entity == null) return;

            NetworkHooks.openScreen(entity, new MenuProvider() {
                @Nullable
                @Override
                public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
                    return new RelicShowcaseMenu(pContainerId, pPlayerInventory);
                }

                @Override
                public Component getDisplayName() {
                    return Component.literal("RelicShowcase");
                }
            });
        });
        context.setPacketHandled(true);
    }
}
