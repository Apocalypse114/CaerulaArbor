
package com.apocalypse.caerulaarbor.item.relic.normal;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.init.ModBlocks;
import com.apocalypse.caerulaarbor.item.relic.RelicItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class StoneGargoyleItem extends RelicItem {
    public StoneGargoyleItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
    }

    @Override
    public void appendHoverText(ItemStack itemstack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(Component.translatable("item.caerula_arbor.stone_gargoyle.description_0").withStyle(ChatFormatting.AQUA));
        list.add(Component.translatable("item.caerula_arbor.stone_gargoyle.description_1").withStyle(ChatFormatting.GRAY));
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        ItemStack stack = ar.getObject();

        if (world instanceof ServerLevel server) {
            world.playSound(null, BlockPos.containing(x, y, z), SoundEvents.ALLAY_AMBIENT_WITH_ITEM, SoundSource.NEUTRAL, 3.5f, 1);
            server.sendParticles(ParticleTypes.RAIN, x, y, z, 72, 1, 1, 1, 0.1);
        } else {
            world.playLocalSound(x, y, z, SoundEvents.ALLAY_AMBIENT_WITH_ITEM, SoundSource.NEUTRAL, 3.5f, 1, false);
            Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
        }

        var cap = entity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());

        Relic.UTIL_ALLEY.gain(cap);
        cap.maxLive += 3;
        cap.lives += 3;
        cap.syncPlayerVariables(entity);

        stack.shrink(1);
        ItemHandlerHelper.giveItemToPlayer(entity, new ItemStack(ModBlocks.ALLAY_BLOCK.get()).copy());

        return ar;
    }
}
