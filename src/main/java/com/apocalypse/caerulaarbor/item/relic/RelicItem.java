package com.apocalypse.caerulaarbor.item.relic;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemHandlerHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public abstract class RelicItem extends Item implements IRelic {

    public RelicItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, @NotNull List<Component> pTooltipComponents, @NotNull TooltipFlag pIsAdvanced) {
        if (pStack.getTag() != null && pStack.getTag().getBoolean("Used")) {
            pTooltipComponents.add(Component.translatable("item.caerula_arbor.relics.used").withStyle(ChatFormatting.ITALIC));
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        var stack = pPlayer.getItemInHand(pUsedHand);
        if (checkUsedMark() && stack.getOrCreateTag().getBoolean("Used")) {
            return InteractionResultHolder.fail(stack);
        }

        if (!this.isInstantUse()) {
            return super.use(pLevel, pPlayer, pUsedHand);
        }
        this.afterUse(stack, pLevel, pPlayer);

        return InteractionResultHolder.consume(stack);
    }

    public void afterUse(ItemStack stack, Level pLevel, LivingEntity entity) {
        var cap = entity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());

        if (pLevel instanceof ServerLevel serverLevel) {
//            serverLevel.sendParticles(this.getGainParticle(), entity.getX(), entity.getY(), entity.getZ(), 72, 0.75, 1, 0.75, 1);

            if (this.getAddedExperience() > 0) {
                serverLevel.addFreshEntity(new ExperienceOrb(serverLevel, entity.getX(), entity.getY(), entity.getZ(), this.getAddedExperience()));
            }
        } else {
            Minecraft.getInstance().gameRenderer.displayItemActivation(stack);

            if (this.getAddedLives() != 0) {
                cap.maxLive += this.getAddedLives();
            }
            if (this.getAddedMaxLives() != 0) {
                cap.life += this.getAddedMaxLives();
            }
            if (this.getAddedShield() != 0) {
                cap.shield += this.getAddedShield();
            }
        }

        this.playGainSound(pLevel, entity);

        if (!this.getRewardItemStack().isEmpty() && entity instanceof Player player) {
            ItemHandlerHelper.giveItemToPlayer(player, this.getRewardItemStack());
            stack.shrink(1);
        }

        // 增加遗物属性
        var relic = getRelic();
        if (relic != null && !relic.gained(entity)) {
            var level = entity.level();

            if (level instanceof ServerLevel server && this.getGainParticle() != null) {
                server.sendParticles(this.getGainParticle(), entity.getX(), entity.getY(), entity.getZ(), 72, 1, 1, 1, 1);
            } else {
                Minecraft.getInstance().gameRenderer.displayItemActivation(stack);
            }

            relic.gain(entity);
        }

        cap.syncPlayerVariables(entity);

        if (checkUsedMark()) {
            stack.getOrCreateTag().putBoolean("Used", true);
        }
    }

    public void playGainSound(Level pLevel, LivingEntity entity) {
        var sound = this.getGainSound();
        if (sound == null) return;

        if (!pLevel.isClientSide) {
            pLevel.playSound(null, entity.getOnPos(), sound, SoundSource.NEUTRAL, 2, 1);
        } else {
            pLevel.playLocalSound(entity.getOnPos(), sound, SoundSource.NEUTRAL, 2, 1, false);
        }
    }

    /**
     * 是否是右键就立刻使用的，如果不是则走原版使用流程
     */
    public boolean isInstantUse() {
        return !this.isEdible();
    }

    public boolean checkUsedMark() {
        return false;
    }

    public int getAddedLives() {
        return 0;
    }

    public int getAddedMaxLives() {
        return 0;
    }

    public int getAddedShield() {
        return 0;
    }

    public int getAddedExperience() {
        return 0;
    }

    @Nullable
    public SoundEvent getGainSound() {
        return SoundEvents.PLAYER_LEVELUP;
    }

    @Nullable
    public ParticleOptions getGainParticle() {
        return ParticleTypes.HAPPY_VILLAGER;
    }

    public ItemStack getRewardItemStack() {
        return ItemStack.EMPTY;
    }
}
