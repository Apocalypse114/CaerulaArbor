
package com.apocalypse.caerulaarbor.item;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class ThermographItem extends Item {
    public ThermographItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        if (!world.isClientSide() && world.getServer() != null)
            world.getServer().getPlayerList().broadcastSystemMessage(Component.literal(("Temperature:" + world.getBiome(BlockPos.containing(entity.getX(), entity.getY(), entity.getZ())).value().getBaseTemperature() * 100f)), false);
        return super.use(world, entity, hand);
    }
}
