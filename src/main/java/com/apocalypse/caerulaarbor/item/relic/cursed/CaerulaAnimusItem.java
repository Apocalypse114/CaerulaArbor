
package com.apocalypse.caerulaarbor.item.relic.cursed;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.network.CaerulaArborModVariables;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class CaerulaAnimusItem extends Item {
    public CaerulaAnimusItem() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
        return UseAnim.BOW;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemstack) {
        return 40;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(Component.translatable("item.caerula_arbor.caerula_animus.description_0").withStyle(ChatFormatting.AQUA));
        list.add(Component.translatable("item.caerula_arbor.caerula_animus.description_1").withStyle(ChatFormatting.GRAY));
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
        entity.startUsingItem(hand);
        return ar;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull ItemStack finishUsingItem(ItemStack itemstack, Level world, LivingEntity entity) {
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        if (!CaerulaArborModVariables.MapVariables.get(world).silence_enabled) {
            CaerulaArborModVariables.MapVariables.get(world).silence_enabled = true;
            CaerulaArborModVariables.MapVariables.get(world).syncData(world);
            if (world instanceof ServerLevel) {
                world.playSound(null, BlockPos.containing(x, y, z), SoundEvents.END_PORTAL_SPAWN, SoundSource.MASTER, 4, 0.85F);
                world.getServer().getPlayerList().broadcastSystemMessage(Component.literal((Component.translatable("item.caerula_arbor.language_key.description_14").getString())), false);
            } else {
                world.playLocalSound(x, y, z, SoundEvents.END_PORTAL_SPAWN, SoundSource.MASTER, 4, 0.85F, false);
            }
            itemstack.shrink(1);
        }
        return super.finishUsingItem(itemstack, world, entity);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        var cap = entity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());

        if (!Relic.CURSED_HEART.gained(cap)) {
            Relic.CURSED_HEART.set(cap, 1);
            cap.light = Math.max(0, cap.light - 50);
            cap.disoclusion = Mth.nextInt(RandomSource.create(), 1, 4);
            cap.syncPlayerVariables(entity);

            if (world instanceof ServerLevel server) {
                world.playSound(null, entity.blockPosition(), SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD.get(), SoundSource.NEUTRAL, 2, 1);
                server.sendParticles(ParticleTypes.CRIMSON_SPORE, x, y, z, 99, 1, 1, 1, 1);
            } else {
                world.playLocalSound(x, y, z, SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD.get(), SoundSource.NEUTRAL, 2, 1, false);
                Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
            }
        }
    }
}
