package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.config.common.RelicsConfig;
import com.apocalypse.caerulaarbor.init.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class PlayerHitFuncProcedure {
    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event != null && event.getEntity() != null) {
            execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getSource(), event.getEntity(), event.getSource().getDirectEntity(), event.getSource().getEntity(),
                    event.getAmount());
        }
    }

    public static void execute(LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity, Entity immediatesourceentity, Entity sourceentity, double amount) {
        execute(null, world, x, y, z, damagesource, entity, immediatesourceentity, sourceentity, amount);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity, Entity immediatesourceentity, Entity sourceentity, double amount) {
        if (damagesource == null || entity == null || immediatesourceentity == null || sourceentity == null)
            return;
        boolean validItem;
        double light_cost;
        var cap = ModCapabilities.getPlayerVariables(entity);
        if (entity instanceof Player ent) {
            light_cost = amount * 0.01;

            if (Relic.HAND_THORNS.gained(cap)) {
                CaerulaArborMod.queueServerWork(10, () -> {
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()), SoundEvents.THORNS_HIT, SoundSource.NEUTRAL, 2, 1);
                        } else {
                            _level.playLocalSound((sourceentity.getX()), (sourceentity.getY()), (sourceentity.getZ()), SoundEvents.THORNS_HIT, SoundSource.NEUTRAL, 2, 1, false);
                        }
                    }
                    if (world instanceof ServerLevel _level)
                        _level.sendParticles(ParticleTypes.SMOKE, (sourceentity.getX()), (sourceentity.getY()), (sourceentity.getZ()), 72, 0.85, 1, 0.85, 0.2);
                    sourceentity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.THORNS)), ent.getArmorValue());
                });
            }
            if (Relic.TREATY.gained(cap)) {
                validItem = false;
                if (sourceentity.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("forge:nether_mobs")))) {
                    validItem = true;
                } else {
                    for (String stringiterator : RelicsConfig.CRIMSON_TREATY.get()) {
                        if ((ForgeRegistries.ENTITY_TYPES.getKey(sourceentity.getType()).toString()).equals(stringiterator)) {
                            validItem = true;
                            break;
                        }
                    }
                }
                if (validItem) {
                    light_cost = 0;
                    if (event != null && event.isCancelable()) {
                        event.setCanceled(true);
                    }
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.LAVA_EXTINGUISH, SoundSource.PLAYERS, 1, 1);
                        } else {
                            _level.playLocalSound(x, y, z, SoundEvents.LAVA_EXTINGUISH, SoundSource.PLAYERS, 1, 1, false);
                        }
                    }
                    if (world instanceof ServerLevel _level)
                        _level.sendParticles(ParticleTypes.ASH, x, y, z, 64, 0.5, 0.75, 0.5, 1);
                    if (Relic.HAND_THORNS.gained(cap)) {
                        CaerulaArborMod.queueServerWork(10, () -> {
                            if (world instanceof Level _level) {
                                if (!_level.isClientSide()) {
                                    _level.playSound(null, BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()), SoundEvents.THORNS_HIT, SoundSource.NEUTRAL, 2,
                                            1);
                                } else {
                                    _level.playLocalSound((sourceentity.getX()), (sourceentity.getY()), (sourceentity.getZ()), SoundEvents.THORNS_HIT, SoundSource.NEUTRAL, 2, 1, false);
                                }
                            }
                            if (world instanceof ServerLevel _level)
                                _level.sendParticles(ParticleTypes.SMOKE, (sourceentity.getX()), (sourceentity.getY()), (sourceentity.getZ()), 72, 0.85, 1, 0.85, 0.2);
                            sourceentity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.THORNS)), ent.getArmorValue());
                        });
                    }
                    if (immediatesourceentity instanceof Arrow) {
                        if (!immediatesourceentity.level().isClientSide())
                            immediatesourceentity.discard();
                    }
                }
            }
            {
                double _setval = cap.light - light_cost;
                entity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
                    capability.light = _setval;
                    capability.syncPlayerVariables(entity);
                });
            }
            if (cap.light < 0) {
                {
                    double _setval = 0;
                    entity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
                        capability.light = _setval;
                        capability.syncPlayerVariables(entity);
                    });
                }
            }
        }
        if (immediatesourceentity instanceof Player livEnt) {
            var cap1 = ModCapabilities.getPlayerVariables(immediatesourceentity);
            if (livEnt.getMainHandItem().is(ItemTags.create(new ResourceLocation("minecraft:hoes")))) {
                if (Relic.HAND_FERTILITY.gained(cap)) {
                    entity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC)), (float) ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) * 0.1));
                    if (world instanceof ServerLevel _level)
                        _level.sendParticles(ParticleTypes.SQUID_INK, x, y, z, 8, 0.75, 0.9, 0.75, 0.1);
                }
            }
            if (livEnt.getMainHandItem().is(ItemTags.create(new ResourceLocation("minecraft:swords")))) {
                if (Relic.HAND_SWORD.gained(cap1)) {
                    if (!(entity instanceof LivingEntity _livEnt59 && _livEnt59.hasEffect(ModMobEffects.ROCK_BREAK.get()))
                            && (!(entity instanceof LivingEntity _entity) || _entity.canBeAffected(new MobEffectInstance(ModMobEffects.ROCK_BREAK.get())))) {
                        if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                            _entity.addEffect(new MobEffectInstance(ModMobEffects.ROCK_BREAK.get(), 120, 0));
                    }
                    livEnt.setHealth((float) (livEnt.getHealth() + livEnt.getMaxHealth() * 0.1));
                    if (!livEnt.level().isClientSide())
                        livEnt.addEffect(new MobEffectInstance(ModMobEffects.ADD_REACH.get(), 120, 3, false, false));
                }
            }
        }
        if (sourceentity instanceof Player _livEnt1) {
            var cap1 = ModCapabilities.getPlayerVariables(sourceentity);
            if (immediatesourceentity instanceof Arrow) {
                if (Relic.HAND_STRANGLE.gained(cap1)) {
                    validItem = false;
                    if (_livEnt1.getMainHandItem().getItem() == Items.CROSSBOW) {
                        validItem = true;
                    } else {
                        for (String stringiterator : RelicsConfig.HAND_OF_CHOKER.get()) {
                            if ((ForgeRegistries.ITEMS.getKey(_livEnt1.getMainHandItem().getItem()).toString()).equals(stringiterator)) {
                                validItem = true;
                                break;
                            }
                        }
                    }
                    if (validItem && (entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.25) {
                        CaerulaArborMod.queueServerWork(5, () -> {
                            if (entity.isAlive()) {
                                entity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.GENERIC_KILL), sourceentity),
                                        (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 99);
                                if (world instanceof ServerLevel _level)
                                    _level.sendParticles(ParticleTypes.GLOW_SQUID_INK, (entity.getX()), (entity.getY()), (entity.getZ()), 128, 1, 1, 1, 0.33);
                                if (world instanceof Level _level) {
                                    if (!_level.isClientSide()) {
                                        _level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), SoundEvents.WITHER_HURT, SoundSource.NEUTRAL, 2, 1);
                                    } else {
                                        _level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), SoundEvents.WITHER_HURT, SoundSource.NEUTRAL, 2, 1, false);
                                    }
                                }
                            }
                        });
                    }
                }
                if (Relic.HAND_FIREWORK.gained(cap1)) {
                    validItem = false;
                    if (_livEnt1.getMainHandItem().getItem() == Items.BOW) {
                        validItem = true;
                    } else {
                        if (_livEnt1.getMainHandItem().getItem() == ModItems.PHLOEM_BOW.get()) {
                            validItem = true;
                        } else {
                            if (_livEnt1.getMainHandItem().is(ItemTags.create(new ResourceLocation("forge:tools/bows")))) {
                                validItem = true;
                            } else {
                                for (String stringiterator : RelicsConfig.HAND_FIREWORK.get()) {
                                    if ((ForgeRegistries.ITEMS.getKey(_livEnt1.getMainHandItem().getItem()).toString()).equals(stringiterator)) {
                                        validItem = true;
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    if (validItem && Math.random() < 0.33) {
                        if (world instanceof Level _level) {
                            if (!_level.isClientSide()) {
                                _level.playSound(null, BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()), SoundEvents.FIREWORK_ROCKET_LAUNCH,
                                        SoundSource.PLAYERS, 3.6F, 1);
                            } else {
                                _level.playLocalSound((sourceentity.getX()), (sourceentity.getY()), (sourceentity.getZ()), SoundEvents.FIREWORK_ROCKET_LAUNCH, SoundSource.PLAYERS, 3.6F,
                                        1, false);
                            }
                        }
                        CaerulaArborMod.queueServerWork(10, () -> {
                            if (world instanceof ServerLevel _level)
                                _level.sendParticles(ParticleTypes.FIREWORK, x, y, z, 85, 2, 2, 2, 0.22);
                            if (world instanceof Level _level) {
                                if (!_level.isClientSide()) {
                                    _level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.FIREWORK_ROCKET_TWINKLE, SoundSource.PLAYERS, 3.6F, 1);
                                } else {
                                    _level.playLocalSound(x, y, z, SoundEvents.FIREWORK_ROCKET_TWINKLE, SoundSource.PLAYERS, 3.6F, 1, false);
                                }
                            }
                            {
                                final Vec3 _center = new Vec3(x, y, z);
                                for (var entityiterator : world.getEntitiesOfClass(Monster.class, new AABB(_center, _center).inflate(5 / 2d), e -> true)) {
                                    if (ForgeRegistries.ENTITY_TYPES.getKey(entityiterator.getType()).toString().equals(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString())) {
                                        entityiterator.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FIREWORKS), sourceentity), (float) (amount * 3));
                                    }
                                }
                            }
                        });
                    }
                }
            }
            if (Relic.LEGEND_CHITIN.gained(cap1) && Math.random() < 0.05) {
                sourceentity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
                    capability.chitin_knife_selected = _livEnt1.getMainHandItem().copy();
                    capability.syncPlayerVariables(sourceentity);
                });
                if (!_livEnt1.level().isClientSide())
                    _livEnt1.addEffect(new MobEffectInstance(ModMobEffects.TIDE_OF_CHITIN.get(), 500, 0, false, false));
                if (world instanceof Level _level) {
                    if (_level.isClientSide()) {
                        _level.playLocalSound(x, y, z, SoundEvents.BEACON_ACTIVATE, SoundSource.NEUTRAL, 3.2F, 1, false);
                    }
                }
            }
        }
        if ((sourceentity instanceof LivingEntity _livingEntity122 && _livingEntity122.getAttributes().hasAttribute(ModAttributes.SANITY_RATE.get())
                ? _livingEntity122.getAttribute(ModAttributes.SANITY_RATE.get()).getValue()
                : 0) > 0) {
            DeductPlayerSanityProcedure.execute(entity,
                    amount * (sourceentity instanceof LivingEntity _livingEntity123 && _livingEntity123.getAttributes().hasAttribute(ModAttributes.SANITY_RATE.get())
                            ? _livingEntity123.getAttribute(ModAttributes.SANITY_RATE.get()).getValue()
                            : 0));
            new Object() {
                void timedLoop(int timedloopiterator, int timedlooptotal, int ticks) {
                    if (world instanceof ServerLevel _level)
                        _level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, (y + entity.getBbHeight() * 0.5), z,
                                (int) (4 * (sourceentity instanceof LivingEntity _livingEntity125 && _livingEntity125.getAttributes().hasAttribute(ModAttributes.SANITY_RATE.get())
                                        ? _livingEntity125.getAttribute(ModAttributes.SANITY_RATE.get()).getValue()
                                        : 0)),
                                1.2, 1.5, 1.2, 0.1);
                    final int tick2 = ticks;
                    CaerulaArborMod.queueServerWork(tick2, () -> {
                        if (timedlooptotal > timedloopiterator + 1) {
                            timedLoop(timedloopiterator + 1, timedlooptotal, tick2);
                        }
                    });
                }
            }.timedLoop(0, 5, 1);
        }
        if (entity.getType().is(ModTags.EntityTypes.SEA_BORN)) {
            if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.OCEANOSPR_KILLER.get(), (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0
                    && !damagesource.is(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("caerula_arbor:oceankiller_damage")))) {
                entity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("caerula_arbor:oceankiller_damage"))), sourceentity),
                        (float) (3 * (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getEnchantmentLevel(ModEnchantments.OCEANOSPR_KILLER.get())));
                if (world instanceof ServerLevel _level)
                    _level.sendParticles(ParticleTypes.ENCHANTED_HIT, x, (y + entity.getBbHeight() * 0.5), z, 32, 0.75, 1, 0.75, 0.1);
            }
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SANITY_REAPER.get(), (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY)) != 0) {
            DeductPlayerSanityProcedure.execute(entity, amount * 2 * (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getEnchantmentLevel(ModEnchantments.SANITY_REAPER.get()));
            new Object() {
                void timedLoop(int timedloopiterator, int timedlooptotal, int ticks) {
                    if (world instanceof ServerLevel _level)
                        _level.sendParticles(ParticleTypes.ELECTRIC_SPARK, x, (y + entity.getBbHeight() * 0.5), z,
                                8 * (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getEnchantmentLevel(ModEnchantments.SANITY_REAPER.get()), 1.2, 1.5, 1.2, 0.1);
                    final int tick2 = ticks;
                    CaerulaArborMod.queueServerWork(tick2, () -> {
                        if (timedlooptotal > timedloopiterator + 1) {
                            timedLoop(timedloopiterator + 1, timedlooptotal, tick2);
                        }
                    });
                }
            }.timedLoop(0, 5, 1);
        }
    }
}
