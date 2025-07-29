package com.apocalypse.caerulaarbor.network.message.send;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.init.ModItems;
import com.apocalypse.caerulaarbor.menu.CaerulaRecorderMenu;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class RelicShowcaseButtonMessage {
    private final int buttonID, x, y, z;

    public RelicShowcaseButtonMessage(FriendlyByteBuf buffer) {
        this.buttonID = buffer.readInt();
        this.x = buffer.readInt();
        this.y = buffer.readInt();
        this.z = buffer.readInt();
    }

    public RelicShowcaseButtonMessage(int buttonID, int x, int y, int z) {
        this.buttonID = buttonID;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static void buffer(RelicShowcaseButtonMessage message, FriendlyByteBuf buffer) {
        buffer.writeInt(message.buttonID);
        buffer.writeInt(message.x);
        buffer.writeInt(message.y);
        buffer.writeInt(message.z);
    }

    public static void handler(RelicShowcaseButtonMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
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

    public static void handleButtonAction(Player player, int buttonID, int x, int y, int z) {
        var world = player.level();

        // security measure to prevent arbitrary chunk generation
        if (!world.hasChunkAt(new BlockPos(x, y, z))) return;

        if (buttonID == 0) {
            player.openMenu(new SimpleMenuProvider((i, inventory, p) -> new CaerulaRecorderMenu(i, inventory), Component.empty()));
        }

        var relic = switch (buttonID) {
            case 1 -> Relic.KING_CROWN;
            case 2 -> Relic.KING_SPEAR;
            case 3 -> Relic.KING_ARMOR;
            case 4 -> Relic.KING_EXTENSION;
            case 5 -> Relic.KING_CRYSTAL;
            case 10 -> Relic.HAND_THORNS;
            case 11 -> Relic.HAND_STRANGLE;
            case 12 -> Relic.HAND_FERTILITY;
            case 13 -> Relic.HAND_OF_PULVERIZATION;
            case 14 -> Relic.HAND_SWIPE;
            case 19 -> Relic.CURSED_EMELIGHT;
            case 20 -> Relic.CURSED_GLOWBODY;
            case 21 -> Relic.CURSED_RESEARCH;
            case 37 -> Relic.HAND_SPEED;
            default -> null;
        };

        if (relic != null) {
            unGainRelic(relic, player);
        }
    }

    public static void unGainRelic(Relic relic, Player player) {
        ItemStack togive = ItemStack.EMPTY;

        var cap = player.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());

        if (player.getInventory().contains(new ItemStack(ModItems.COIN_OF_TRADE.get()))) {
            if (relic.gained(cap)) {
                player.getInventory().clearOrCountMatchingItems(p -> ModItems.COIN_OF_TRADE.get() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());

                for (int index0 = 0; index0 < 64; index0++) {
                    togive = new ItemStack((ForgeRegistries.ITEMS.tags().getTag(ItemTags.create(new ResourceLocation("caerula_arbor:relic_generic"))).getRandomElement(RandomSource.create()).orElse(Items.AIR))).copy();
                    if (togive.getItem() != ItemStack.EMPTY.getItem()) {
                        break;
                    }
                }

                ItemHandlerHelper.giveItemToPlayer(player, togive.copy());

                relic.reset(cap);
                cap.syncPlayerVariables(player);
            }
        } else if (player.isCreative() && relic.gained(cap)) {
            relic.reset(cap);
            cap.syncPlayerVariables(player);
        }
    }

}
