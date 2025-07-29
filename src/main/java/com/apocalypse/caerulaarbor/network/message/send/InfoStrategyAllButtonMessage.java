
package com.apocalypse.caerulaarbor.network.message.send;

import com.apocalypse.caerulaarbor.menu.InfoStrategyAllMenu;
import com.apocalypse.caerulaarbor.menu.InfoStrategyMigrationMenu;
import com.apocalypse.caerulaarbor.menu.InfoStrategySubsisMenu;
import com.apocalypse.caerulaarbor.procedures.OpenEvoTreeProcedure;
import com.apocalypse.caerulaarbor.procedures.OpenStraBreedProcedure;
import com.apocalypse.caerulaarbor.procedures.OpenStraGrowProcedure;
import io.netty.buffer.Unpooled;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
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
        Level world = entity.level();
        var guistate = InfoStrategyAllMenu.guistate;
        // security measure to prevent arbitrary chunk generation
        if (!world.hasChunkAt(new BlockPos(x, y, z)))
            return;
        if (buttonID == 0) {

            OpenEvoTreeProcedure.execute(world, x, y, z, entity);
        }
        if (buttonID == 1) {

            OpenStraBreedProcedure.execute(world, x, y, z, entity);
        }
        if (buttonID == 2) {

            OpenStraGrowProcedure.execute(world, x, y, z, entity);
        }
        if (buttonID == 3) {

            entity.closeContainer();
            if ((Entity) entity instanceof ServerPlayer _ent) {
                BlockPos _bpos = BlockPos.containing(x, y, z);
                NetworkHooks.openScreen(_ent, new MenuProvider() {
                    @Override
                    public Component getDisplayName() {
                        return Component.literal("InfoStrategyMigration");
                    }

                    @Override
                    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                        return new InfoStrategyMigrationMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
                    }
                }, _bpos);
            }
        }
        if (buttonID == 4) {
            if ((Entity) entity instanceof Player _player)
                _player.closeContainer();
            if ((Entity) entity instanceof ServerPlayer _ent) {
                BlockPos _bpos = BlockPos.containing(x, y, z);
                NetworkHooks.openScreen(_ent, new MenuProvider() {
                    @Override
                    public @NotNull Component getDisplayName() {
                        return Component.literal("InfoStrategySubsis");
                    }

                    @Override
                    @ParametersAreNonnullByDefault
                    public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) {
                        return new InfoStrategySubsisMenu(id, inventory, new FriendlyByteBuf(Unpooled.buffer()).writeBlockPos(_bpos));
                    }
                }, _bpos);
            }
        }
    }
}
