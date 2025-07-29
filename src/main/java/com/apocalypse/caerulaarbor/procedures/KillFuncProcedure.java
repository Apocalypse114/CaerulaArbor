//package com.apocalypse.caerulaarbor.procedures;
//
//import com.apocalypse.caerulaarbor.capability.ModCapabilities;
//import com.apocalypse.caerulaarbor.capability.Relic;
//import com.apocalypse.caerulaarbor.capability.map.MapVariables;
//import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
//import com.apocalypse.caerulaarbor.config.common.RelicsConfig;
//import com.apocalypse.caerulaarbor.init.ModEntities;
//import com.apocalypse.caerulaarbor.init.ModGameRules;
//import com.apocalypse.caerulaarbor.init.ModItems;
//import com.apocalypse.caerulaarbor.init.ModTags;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.particles.ParticleTypes;
//import net.minecraft.core.registries.Registries;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.resources.ResourceLocation;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.tags.ItemTags;
//import net.minecraft.util.Mth;
//import net.minecraft.util.RandomSource;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.damagesource.DamageTypes;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.MobSpawnType;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.enchantment.Enchantments;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.LevelAccessor;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.event.entity.living.LivingDeathEvent;
//import net.minecraftforge.eventbus.api.Event;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//import net.minecraftforge.registries.ForgeRegistries;
//
//import javax.annotation.Nullable;
//
//@Mod.EventBusSubscriber
//public class KillFuncProcedure {
//    @SubscribeEvent
//    public static void onEntityDeath(LivingDeathEvent event) {
//        if (event != null && event.getEntity() != null) {
//            execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getSource(), event.getEntity(), event.getSource().getEntity());
//        }
//    }
//
//    public static void execute(LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity, Entity sourceentity) {
//        execute(null, world, x, y, z, damagesource, entity, sourceentity);
//    }
//
//    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity, Entity sourceentity) {
//        if (damagesource == null || entity == null || sourceentity == null)
//            return;
//        boolean validweapon;
//        double dama;
//        ItemStack weapon;
//        if (sourceentity instanceof Player _livEnt) {
//            var cap = ModCapabilities.getPlayerVariables(sourceentity);
//            if (Relic.CURSED_EMELIGHT.gained(cap)) {
//                sourceentity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
//                    capability.light = cap.light - Mth.nextDouble(RandomSource.create(), 0.1, 0.2);
//                    capability.syncPlayerVariables(sourceentity);
//                });
//            }
//            if (Relic.CURSED_GLOWBODY.gained(cap)) {
//                sourceentity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
//                    capability.light = cap.light - Mth.nextDouble(RandomSource.create(), 0.2, 0.3);
//                    capability.syncPlayerVariables(sourceentity);
//                });
//            }
//            if (Relic.CURSED_RESEARCH.gained(cap)) {
//                sourceentity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
//                    capability.light = cap.light - Mth.nextDouble(RandomSource.create(), 0.3, 0.5);
//                    capability.syncPlayerVariables(sourceentity);
//                });
//            }
//            if (cap.light < 0) {
//                sourceentity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
//                    capability.light = 0;
//                    capability.syncPlayerVariables(sourceentity);
//                });
//            }
//            if (Relic.KING_ARMOR.gained(cap)) {
//                if (Math.random() < 0.08) {
//                    if (cap.lives > 1) {
//                        sourceentity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
//                            capability.lives = cap.lives - 1;
//                            capability.syncPlayerVariables(sourceentity);
//                        });
//                    }
//                    sourceentity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
//                        capability.shield = cap.shield + 1;
//                        capability.syncPlayerVariables(sourceentity);
//                    });
//                }
//            }
//            if (Relic.KING_CRYSTAL.gained(cap)) {
//                if (Math.random() < 0.1) {
//                    if (cap.lives > 1) {
//                        sourceentity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
//                            capability.lives = cap.lives - 2;
//                            capability.syncPlayerVariables(sourceentity);
//                        });
//                        if (cap.light < 1) {
//                            sourceentity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
//                                capability.light = 1;
//                                capability.syncPlayerVariables(sourceentity);
//                            });
//                        }
//                    }
//                    if (sourceentity instanceof Player _player)
//                        _player.giveExperienceLevels(1);
//                    if (world instanceof ServerLevel _level) {
//                        ItemEntity entityToSpawn = new ItemEntity(_level, x, y, z, new ItemStack(ModItems.REDSTONE_INGOT.get()));
//                        entityToSpawn.setPickUpDelay(10);
//                        _level.addFreshEntity(entityToSpawn);
//                    }
//                }
//            }
//            if (Relic.HAND_ENGRAVE.get(cap) >= 0 && Relic.HAND_ENGRAVE.get(cap) < 99) {
//                validweapon = false;
//                if (_livEnt.getMainHandItem().getItem() == Items.TRIDENT) {
//                    validweapon = true;
//                } else if (damagesource.is(DamageTypes.TRIDENT)) {
//                    validweapon = true;
//                } else {
//                    for (String stringiterator : RelicsConfig.HAND_ENGRAVE.get()) {
//                        if ((ForgeRegistries.ITEMS.getKey(_livEnt.getMainHandItem().getItem()).toString()).equals(stringiterator)) {
//                            validweapon = true;
//                            break;
//                        }
//                    }
//                }
//                if (validweapon) {
//                    Relic.HAND_ENGRAVE.set(cap, Relic.HAND_ENGRAVE.get(cap) + 1);
//                    cap.syncPlayerVariables(sourceentity);
//
////                    sourceentity.getCapability(CaerulaArborModVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(capability -> {
////                        capability.relic_hand_ENGRAVE = Relic.HAND_ENGRAVE.get(cap) + 1;
////                        capability.syncPlayerVariables(sourceentity);
////                    });
//                }
//            }
//            if (Relic.SURVIVOR_CONTRACT.get(cap) >= 0 && Relic.SURVIVOR_CONTRACT.get(cap) < 32 && Math.random() < 0.02) {
//                Relic.SURVIVOR_CONTRACT.set(cap, Relic.SURVIVOR_CONTRACT.get(cap) + 1);
//                cap.syncPlayerVariables(sourceentity);
//
////                sourceentity.getCapability(CaerulaArborModVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(capability -> {
////                    capability.relic_SURVIVOR = Relic.SURVIVOR.get(cap) + 1;
////                    capability.syncPlayerVariables(sourceentity);
////                });
//                if (world instanceof ServerLevel _level)
//                    _level.sendParticles(ParticleTypes.WAX_ON, x, y, z, 48, 0.7, 1.5, 0.7, 0.2);
//            }
//        }
//        if (entity.getType().is(ModTags.EntityTypes.SEA_BORN)) {
//            if (world.getLevelData().getGameRules().getBoolean(ModGameRules.NATURAL_EVOLUTION)) {
//                if (!world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 128, 128, 128), e -> true).isEmpty()) {
//                    MapVariables.get(world).evoPointBreed = MapVariables.get(world).evoPointBreed + (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.1;
//                    MapVariables.get(world).syncData(world);
//                    if (world instanceof Level level) {
//                        UpgradeBreedProcedure.execute(level);
//                    }
//                    UpgradeSilenceProcedure.execute(world, entity, (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.1);
//                }
//            }
//        }
//        if ((sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("caerula_arbor:self_mendable")))) {
//            weapon = (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).copy();
//            dama = weapon.getDamageValue() - Mth.nextInt(RandomSource.create(), 1, 5 + weapon.getEnchantmentLevel(Enchantments.UNBREAKING));
//            if (dama <= 0) {
//                (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).setDamageValue(0);
//            } else {
//                (sourceentity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).setDamageValue((int) dama);
//            }
//        }
//        if (entity instanceof Player) {
//            if ((ModCapabilities.getPlayerVariables(entity)).player_oceanization < 3) {
//                entity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
//                    capability.player_oceanization = 0;
//                    capability.syncPlayerVariables(entity);
//                });
//            }
//            if (damagesource.is(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("caerula_arbor:oceanize_damage")))) {
//                if (world instanceof ServerLevel _level) {
//                    Entity entityToSpawn = ModEntities.DEEP_SEA_SLIDER.get().spawn(_level, BlockPos.containing(x, y, z), MobSpawnType.MOB_SUMMONED);
//                    if (entityToSpawn != null) {
//                        entityToSpawn.setDeltaMovement(0, 0.15, 0);
//                    }
//                }
//                if (world instanceof Level _level) {
//                    if (!_level.isClientSide()) {
//                        _level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.SCULK_VEIN_PLACE, SoundSource.PLAYERS, 0.75F, 1);
//                    } else {
//                        _level.playLocalSound(x, y, z, SoundEvents.SCULK_VEIN_PLACE, SoundSource.PLAYERS, 0.75F, 1, false);
//                    }
//                }
//            }
//        }
//    }
//}
