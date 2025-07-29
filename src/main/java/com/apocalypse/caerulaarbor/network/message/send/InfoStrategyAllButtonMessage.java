
package com.apocalypse.caerulaarbor.network.message.send;

import com.apocalypse.caerulaarbor.menu.*;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiFunction;
import java.util.function.Supplier;

public class InfoStrategyAllButtonMessage {
    private final int buttonID, x, y, z;

    public InfoStrategyAllButtonMessage(FriendlyByteBuf buffer) {
        this.buttonID = buffer.readInt();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
    }

    public InfoStrategyAllButtonMessage(int buttonID, int x, int y, int z) {
        this.buttonID = buttonID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void buffer(InfoStrategyAllButtonMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.buttonID);
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
    }

    public static void handler(InfoStrategyAllButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> {
            Player entity = context.getSender();
            int buttonID = message.buttonID;
            int x = message.x;
            int y = message.y;
            int z = message.z;
            handleButtonAction(entity, buttonID, x, y, z);
        });
        context.setPacketHandled(true);
    }

    public static void handleButtonAction(Player entity, int buttonID, int x, int y, int z) {
        var world = entity.level();
        // security measure to prevent arbitrary chunk generation
        var pos = new BlockPos(x, y, z);
        if (!world.hasChunkAt(pos))
            return;

        if (buttonID == 0) {
            openMenu(entity, pos, "EvoTree", (id, inventory) -> new EvoTreeMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos)));
        } else if (buttonID == 1) {
            openMenu(entity, pos, "InfoStrategyBreed", (id, inventory) -> new InfoStrategyBreedMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos)));
        } else if (buttonID == 2) {
            openMenu(entity, pos, "InfoStrategyGrow", (id, inventory) -> new InfoStrategyGrowMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos)));
        } else if (buttonID == 3) {
            openMenu(entity, pos, "InfoStrategyMigration", (id, inventory) -> new InfoStrategyMigrationMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos)));
        } else if (buttonID == 4) {
            openMenu(entity, pos, "InfoStrategySubsis", (id, inventory) -> new InfoStrategySubsisMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(pos)));
        }
    }

    private static void openMenu(Player player, BlockPos pos, String name, BiFunction<Integer, Inventory, AbstractContainerMenu> menu) {
        player.closeContainer();

        if (player instanceof ServerPlayer serverPlayer) {
            NetworkHooks.openScreen(serverPlayer, new MenuProvider() {
                @Override
                public @NotNull Component getDisplayName() {
                    return Component.literal(name);
                }

                @Override
                @ParametersAreNonnullByDefault
                public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                    return menu.apply(id, inventory);
                }
            }, pos);
        }
    }
}
