package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.config.common.RelicsConfig;
import com.apocalypse.caerulaarbor.entity.CreeperFishEntity;
import com.apocalypse.caerulaarbor.init.ModAttributes;
import com.apocalypse.caerulaarbor.init.ModEnchantments;
import com.apocalypse.caerulaarbor.init.ModItems;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import com.apocalypse.caerulaarbor.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
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
import java.util.Comparator;

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
        ItemStack item_temp;
        boolean validItem;
        double light_cost;
        double rate;
        double time;
        var cap = entity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());
        if (entity instanceof Player) {
            light_cost = amount * 0.01;
            if (cap.disoclusion == 1) {
                if (Math.random() < 0.1) {
                    item_temp = (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).copy();
                    if (entity instanceof LivingEntity _entity) {
                        ItemStack _setstack = (entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).copy();
                        _setstack.setCount((entity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getCount());
                        _entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
                        if (_entity instanceof Player _player)
                            _player.getInventory().setChanged();
                    }
                    if (entity instanceof LivingEntity _entity) {
                        ItemStack _setstack = item_temp.copy();
                        _setstack.setCount(item_temp.getCount());
                        _entity.setItemInHand(InteractionHand.OFF_HAND, _setstack);
                        if (_entity instanceof Player _player)
                            _player.getInventory().setChanged();
                    }
                }
            }
            if (cap.disoclusion == 3) {
                rate = 0.08;
                time = 160;
                if (world.getBiome(BlockPos.containing(x, y, z)).value().getBaseTemperature() * 100f >= 180) {
                    rate = 0.02;
                    time = 40;
                } else if (world.getBiome(BlockPos.containing(x, y, z)).value().getBaseTemperature() * 100f <= 10) {
                    rate = 0.12;
                    time = 240;
                }
                if (Math.random() < rate) {
                    if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                        _entity.addEffect(new MobEffectInstance(ModMobEffects.FROZEN.get(), (int) time, 0, false, false));
                }
            }
            if (Relic.HAND_THORNS.gained(cap)) {
                CaerulaArborMod.queueServerWork(10, () -> {
                    if (world instanceof Level _level) {
                        if (!_level.isClientSide()) {
                            _level.playSound(null, BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("enchant.thorns.hit")), SoundSource.NEUTRAL, 2, 1);
                        } else {
                            _level.playLocalSound((sourceentity.getX()), (sourceentity.getY()), (sourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("enchant.thorns.hit")), SoundSource.NEUTRAL, 2, 1, false);
                        }
                    }
                    if (world instanceof ServerLevel _level)
                        _level.sendParticles(ParticleTypes.SMOKE, (sourceentity.getX()), (sourceentity.getY()), (sourceentity.getZ()), 72, 0.85, 1, 0.85, 0.2);
                    sourceentity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.THORNS)), entity instanceof LivingEntity _livEnt ? _livEnt.getArmorValue() : 0);
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
                            _level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.lava.extinguish")), SoundSource.PLAYERS, 1, 1);
                        } else {
                            _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.lava.extinguish")), SoundSource.PLAYERS, 1, 1, false);
                        }
                    }
                    if (world instanceof ServerLevel _level)
                        _level.sendParticles(ParticleTypes.ASH, x, y, z, 64, 0.5, 0.75, 0.5, 1);
                    if (Relic.HAND_THORNS.gained(cap)) {
                        CaerulaArborMod.queueServerWork(10, () -> {
                            if (world instanceof Level _level) {
                                if (!_level.isClientSide()) {
                                    _level.playSound(null, BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("enchant.thorns.hit")), SoundSource.NEUTRAL, 2,
                                            1);
                                } else {
                                    _level.playLocalSound((sourceentity.getX()), (sourceentity.getY()), (sourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("enchant.thorns.hit")), SoundSource.NEUTRAL, 2, 1, false);
                                }
                            }
                            if (world instanceof ServerLevel _level)
                                _level.sendParticles(ParticleTypes.SMOKE, (sourceentity.getX()), (sourceentity.getY()), (sourceentity.getZ()), 72, 0.85, 1, 0.85, 0.2);
                            sourceentity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.THORNS)), entity instanceof LivingEntity _livEnt ? _livEnt.getArmorValue() : 0);
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
        if (immediatesourceentity instanceof Player) {
            var cap1 = immediatesourceentity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());
            if (cap1.disoclusion == 1) {
                if (Math.random() < 0.1) {
                    item_temp = (immediatesourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).copy();
                    if (immediatesourceentity instanceof LivingEntity _entity) {
                        ItemStack _setstack = (immediatesourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).copy();
                        _setstack.setCount((immediatesourceentity instanceof LivingEntity _livEnt ? _livEnt.getOffhandItem() : ItemStack.EMPTY).getCount());
                        _entity.setItemInHand(InteractionHand.MAIN_HAND, _setstack);
                        if (_entity instanceof Player _player)
                            _player.getInventory().setChanged();
                    }
                    if (immediatesourceentity instanceof LivingEntity _entity) {
                        ItemStack _setstack = item_temp.copy();
                        _setstack.setCount(item_temp.getCount());
                        _entity.setItemInHand(InteractionHand.OFF_HAND, _setstack);
                        if (_entity instanceof Player _player)
                            _player.getInventory().setChanged();
                    }
                }
            }
            if ((immediatesourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("minecraft:hoes")))) {
                if (Relic.HAND_FERTILITY.gained(cap)) {
                    entity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC)), (float) ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) * 0.1));
                    if (world instanceof ServerLevel _level)
                        _level.sendParticles(ParticleTypes.SQUID_INK, x, y, z, 8, 0.75, 0.9, 0.75, 0.1);
                }
            }
            if ((immediatesourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("minecraft:swords")))) {
                if (Relic.HAND_SWORD.gained(cap1)) {
                    if (!(entity instanceof LivingEntity _livEnt59 && _livEnt59.hasEffect(ModMobEffects.ROCK_BREAK.get()))
                            && !!(entity instanceof LivingEntity _entity ? _entity.canBeAffected(new MobEffectInstance(ModMobEffects.ROCK_BREAK.get())) : true)) {
                        if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                            _entity.addEffect(new MobEffectInstance(ModMobEffects.ROCK_BREAK.get(), 120, 0));
                    }
                    if (immediatesourceentity instanceof LivingEntity _entity)
                        _entity.setHealth((float) ((immediatesourceentity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) + (immediatesourceentity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.1));
                    if (immediatesourceentity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                        _entity.addEffect(new MobEffectInstance(ModMobEffects.ADD_REACH.get(), 120, 3, false, false));
                }
            }
        }
        if (sourceentity instanceof Player) {
            var cap1 = sourceentity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());
            if (immediatesourceentity instanceof Arrow) {
                if (Relic.HAND_STRANGLE.gained(cap1)) {
                    validItem = false;
                    LivingEntity _livEnt11 = (LivingEntity) sourceentity;
                    if (_livEnt11.getMainHandItem().getItem() == Items.CROSSBOW) {
                        validItem = true;
                    } else {
                        for (String stringiterator : RelicsConfig.HAND_OF_CHOKER.get()) {
                            LivingEntity _livEnt1 = (LivingEntity) sourceentity;
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
                                        (float) ((entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 99));
                                if (world instanceof ServerLevel _level)
                                    _level.sendParticles(ParticleTypes.GLOW_SQUID_INK, (entity.getX()), (entity.getY()), (entity.getZ()), 128, 1, 1, 1, 0.33);
                                if (world instanceof Level _level) {
                                    if (!_level.isClientSide()) {
                                        _level.playSound(null, BlockPos.containing(entity.getX(), entity.getY(), entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.wither.hurt")), SoundSource.NEUTRAL, 2, 1);
                                    } else {
                                        _level.playLocalSound((entity.getX()), (entity.getY()), (entity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.wither.hurt")), SoundSource.NEUTRAL, 2, 1, false);
                                    }
                                }
                            }
                        });
                    }
                }
                if (Relic.HAND_FIREWORK.gained(cap1)) {
                    validItem = false;
                    if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == Items.BOW) {
                        validItem = true;
                    } else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem() == ModItems.PHLOEM_BOW.get()) {
                        validItem = true;
                    } else if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("forge:tools/bows")))) {
                        validItem = true;
                    } else {
                        for (String stringiterator : RelicsConfig.HAND_FIREWORK.get()) {
                            if ((ForgeRegistries.ITEMS.getKey((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getItem()).toString()).equals(stringiterator)) {
                                validItem = true;
                                break;
                            }
                        }
                    }
                    if (validItem && Math.random() < 0.33) {
                        if (world instanceof Level _level) {
                            if (!_level.isClientSide()) {
                                _level.playSound(null, BlockPos.containing(sourceentity.getX(), sourceentity.getY(), sourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.firework_rocket.launch")),
                                        SoundSource.PLAYERS, (float) 3.6, 1);
                            } else {
                                _level.playLocalSound((sourceentity.getX()), (sourceentity.getY()), (sourceentity.getZ()), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.firework_rocket.launch")), SoundSource.PLAYERS, (float) 3.6,
                                        1, false);
                            }
                        }
                        CaerulaArborMod.queueServerWork(10, () -> {
                            if (world instanceof ServerLevel _level)
                                _level.sendParticles(ParticleTypes.FIREWORK, x, y, z, 85, 2, 2, 2, 0.22);
                            if (world instanceof Level _level) {
                                if (!_level.isClientSide()) {
                                    _level.playSound(null, BlockPos.containing(x, y, z), ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.firework_rocket.twinkle")), SoundSource.PLAYERS, (float) 3.6, 1);
                                } else {
                                    _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("entity.firework_rocket.twinkle")), SoundSource.PLAYERS, (float) 3.6, 1, false);
                                }
                            }
                            {
                                final Vec3 _center = new Vec3(x, y, z);
                                for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(5 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
                                    if (((ForgeRegistries.ENTITY_TYPES.getKey(entityiterator.getType()).toString()).equals(ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString()) || entityiterator instanceof Monster)
                                            && !(entityiterator == sourceentity)) {
                                        entityiterator.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.FIREWORKS), sourceentity), (float) (amount * 3));
                                    }
                                }
                            }
                        });
                    }
                }
            }
            if (Relic.LEGEND_CHITIN.gained(cap1)) {
                if (Math.random() < 0.05) {
                    {
                        ItemStack _setval = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY);
                        sourceentity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
                            capability.chitin_knife_selected = _setval.copy();
                            capability.syncPlayerVariables(sourceentity);
                        });
                    }
                    if (sourceentity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                        _entity.addEffect(new MobEffectInstance(ModMobEffects.TIDE_OF_CHITIN.get(), 500, 0, false, false));
                    if (world instanceof Level _level) {
                        if (_level.isClientSide()) {
                            _level.playLocalSound(x, y, z, ForgeRegistries.SOUND_EVENTS.getValue(new ResourceLocation("block.beacon.activate")), SoundSource.NEUTRAL, (float) 3.2, 1, false);
                        }
                    }
                }
            }
        }
        if (entity instanceof CreeperFishEntity) {
            if (entity instanceof CreeperFishEntity _datEntSetI)
                _datEntSetI.getEntityData().set(CreeperFishEntity.DATA_deal, (int) ((entity instanceof CreeperFishEntity _datEntI ? _datEntI.getEntityData().get(CreeperFishEntity.DATA_deal) : 0) + amount));
            if ((entity instanceof CreeperFishEntity _datEntI ? _datEntI.getEntityData().get(CreeperFishEntity.DATA_deal) : 0) >= (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.15) {
                RangedSanityAttackProcedure.execute(world, x, y, z, entity, entity);
                if (entity instanceof CreeperFishEntity _datEntSetI)
                    _datEntSetI.getEntityData().set(CreeperFishEntity.DATA_deal, 0);
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
        if (entity.getType().is(ModTags.EntityTypes.OCEAN_OFFSPRING)) {
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
                                (int) (8 * (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).getEnchantmentLevel(ModEnchantments.SANITY_REAPER.get())), 1.2, 1.5, 1.2, 0.1);
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
