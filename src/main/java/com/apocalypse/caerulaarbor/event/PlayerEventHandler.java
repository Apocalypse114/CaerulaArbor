package com.apocalypse.caerulaarbor.event;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.init.ModGameRules;
import com.apocalypse.caerulaarbor.init.ModParticleTypes;
import com.apocalypse.caerulaarbor.init.ModSounds;
import com.apocalypse.caerulaarbor.item.relic.IRelic;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class PlayerEventHandler {

    @SubscribeEvent
    public static void onPlayerCriticalHit(CriticalHitEvent event) {
        if (!event.isVanillaCritical()) return;
        var player = event.getEntity();

        var cap = player.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());
        cap.relics.forEach((relic, integer) -> {
            if (!relic.gained(player)) return;
            var relicItem = relic.item;
            if (relicItem instanceof IRelic iRelic) {
                int level = iRelic.onCriticalHit(player, integer);
                if (integer != level) {
                    relic.set(player, level);
                }
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerAttack(AttackEntityEvent event) {
        var player = event.getEntity();

        var cap = player.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());
        cap.relics.forEach((relic, integer) -> {
            if (!relic.gained(player)) return;
            var relicItem = relic.item;
            if (relicItem instanceof IRelic iRelic) {
                int level = iRelic.onAttack(player, integer);
                if (integer != level) {
                    relic.set(player, level);
                }
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;
        var player = event.player;
        var cap = player.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());
        cap.relics.forEach((relic, integer) -> {
            if (relic.item instanceof IRelic iRelic) {
                int level = iRelic.onPlayerTick(player, integer);
                if (integer != level) {
                    relic.set(player, level);
                }
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (!(entity instanceof Player player)) return;
        Level level = player.level();

        var cap = player.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());

        if (!level.getGameRules().getBoolean(GameRules.RULE_KEEPINVENTORY) && !level.isClientSide) {
            for (var relic : Relic.values()) {
                relic.remove(player);
            }
            cap.syncPlayerVariables(player);
        }

        var source = event.getSource();
        if (source.is(DamageTypes.GENERIC_KILL)) return;

        var pos = player.getOnPos();

        boolean shouldCancel = false;

        double lightCost = 0;

        if (cap.shield > 0) {
            cap.shield -= 1;

            sendSoundAndParticles(level, pos, ModParticleTypes.SHIELDLOSS.get());
            shouldCancel = true;
        } else if (cap.lives > 0) {
            cap.lives -= 1;

            sendSoundAndParticles(level, pos, ModParticleTypes.LIFELOSS.get());
            shouldCancel = true;

            if (source.is(DamageTypes.LIGHTNING_BOLT) || source.is(DamageTypes.FELL_OUT_OF_WORLD) || source.is(DamageTypes.OUTSIDE_BORDER) || source.is(DamageTypes.CRAMMING)) {
                lightCost = 15;
            } else if (source.is(DamageTypes.WITHER) || source.is(DamageTypes.WITHER_SKULL) || source.is(DamageTypes.EXPLOSION) || source.is(DamageTypes.FIREWORKS) || source.is(DamageTypes.SONIC_BOOM)
                    || source.is(DamageTypes.DRAGON_BREATH) || source.is(DamageTypes.LAVA) || source.is(DamageTypes.FALLING_ANVIL) || source.is(DamageTypes.FALLING_STALACTITE)
                    || source.is(DamageTypes.STALAGMITE)) {
                lightCost = 10;
            } else {
                lightCost = 5;
            }
        } else {
            lightCost = 50;
        }

        cap.light = Math.max(0, cap.light - lightCost);
        cap.syncPlayerVariables(player);

        if (shouldCancel && level.getGameRules().getBoolean(ModGameRules.TARGET_LIFE_FUNCTION)) {
            event.setCanceled(true);
            player.setHealth(player.getMaxHealth() * 0.5f);
            player.getCapability(ModCapabilities.SANITY_INJURY).ifPresent(capability -> capability.setValue(1000));
        }
    }

    private static void sendSoundAndParticles(Level level, BlockPos pos, ParticleOptions particleOptions) {
        if (level instanceof ServerLevel serverLevel) {
            serverLevel.playSound(null, pos, ModSounds.TARGET_DAMAGED.get(), SoundSource.PLAYERS, 1, 1);
            serverLevel.playSound(null, pos, SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1, 1);
            serverLevel.sendParticles(particleOptions, pos.getX(), pos.getY() + 0.95, pos.getZ(), 72, 0.75, 0.55, 0.75, 0.2);
        } else {
            level.playLocalSound(pos, ModSounds.TARGET_DAMAGED.get(), SoundSource.PLAYERS, 1, 1, false);
            level.playLocalSound(pos, SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1, 1, false);
        }
    }
}
