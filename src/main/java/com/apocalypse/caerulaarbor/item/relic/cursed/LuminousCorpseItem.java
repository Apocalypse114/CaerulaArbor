
package com.apocalypse.caerulaarbor.item.relic.cursed;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.procedures.DeductPlayerSanityProcedure;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class LuminousCorpseItem extends Item {
    public LuminousCorpseItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
        return UseAnim.EAT;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemstack) {
        return 60;
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(Component.translatable("item.caerula_arbor.luminous_corpse.description_0"));
        list.add(Component.translatable("item.caerula_arbor.luminous_corpse.description_1"));
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

        if (world instanceof ServerLevel level) {
            entity.addEffect(new MobEffectInstance(MobEffects.GLOWING, 100, 0));
            level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, y, z, 72, 1, 2, 1, 0.1);
        }

        DeductPlayerSanityProcedure.execute(entity, 500);

        if (entity instanceof Player player) {
            player.getCooldowns().addCooldown(itemstack.getItem(), 200);
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

        var cap = ModCapabilities.getPlayerVariables(entity);

        if (Relic.CURSED_GLOWBODY.gained(cap)) return;

        if (!world.isClientSide()) {
            world.playSound(null, BlockPos.containing(x, y, z), SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD.get(), SoundSource.NEUTRAL, 2, 1);
        } else {
            world.playLocalSound(x, y, z, SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD.get(), SoundSource.NEUTRAL, 2, 1, false);
        }

        if (world instanceof ServerLevel server) {
            server.sendParticles(ParticleTypes.CRIMSON_SPORE, x, y, z, 99, 1, 1, 1, 1);
        } else {
            Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
        }

        Relic.CURSED_GLOWBODY.set(cap, 1);
        cap.syncPlayerVariables(entity);
    }
}
