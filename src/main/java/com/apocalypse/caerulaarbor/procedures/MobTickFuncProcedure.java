//package com.apocalypse.caerulaarbor.procedures;
//
//import com.apocalypse.caerulaarbor.capability.map.MapVariables;
//import com.apocalypse.caerulaarbor.init.ModGameRules;
//import com.apocalypse.caerulaarbor.init.ModMobEffects;
//import com.apocalypse.caerulaarbor.init.ModTags;
//import net.minecraft.core.BlockPos;
//import net.minecraft.util.Mth;
//import net.minecraft.util.RandomSource;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.effect.MobEffects;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.LevelAccessor;
//import net.minecraft.world.phys.AABB;
//import net.minecraft.world.phys.Vec3;
//import net.minecraftforge.event.entity.living.LivingEvent;
//import net.minecraftforge.eventbus.api.Event;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import net.minecraftforge.fml.common.Mod;
//
//import javax.annotation.Nullable;
//import java.util.Comparator;
//import java.util.List;
//
//@Mod.EventBusSubscriber
//public class MobTickFuncProcedure {
//    @SubscribeEvent
//    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
//        execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getEntity());
//    }
//
//    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
//        execute(null, world, x, y, z, entity);
//    }
//
//    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
//        if (entity == null)
//            return;
//        double amplifi;
//        double pnt;
//        if (!(world.getBlockState(BlockPos.containing(x, y, z))).is(ModTags.Blocks.SEA_TRAIL)) {
//            if (entity instanceof LivingEntity _entity)
//                _entity.removeEffect(ModMobEffects.TRAIL_BUFF.get());
//        }
//        if (entity.getType().is(ModTags.EntityTypes.SEA_BORN)) {
//            if (MapVariables.get(world).strategySubsisting >= 3) {
//                if (!(entity instanceof LivingEntity _livEnt14 && _livEnt14.hasEffect(MobEffects.DAMAGE_RESISTANCE))) {
//                    if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
//                        _entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 20, (int) (MapVariables.get(world).strategySubsisting - 3)));
//                }
//            }
//            if (MapVariables.get(world).strategySilence > 0) {
//                if (MapVariables.get(world).strategySilence >= 3) {
//                    if (!(entity instanceof LivingEntity _livEnt16 && _livEnt16.hasEffect(ModMobEffects.BOOST_OF_SILENCE.get()))) {
//                        if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
//                            _entity.addEffect(new MobEffectInstance(ModMobEffects.BOOST_OF_SILENCE.get(), 20, (int) (MapVariables.get(world).strategySilence - 1)));
//                    }
//                    if (!(entity instanceof LivingEntity _livEnt18 && _livEnt18.hasEffect(ModMobEffects.STRENGTH_OF_CROWD.get()))) {
//                        amplifi = -1;
//                        if (MapVariables.get(world).strategySilence >= 4) {
//                            {
//                                final Vec3 _center = new Vec3(x, y, z);
//                                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(64 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
//                                for (Entity entityiterator : _entfound) {
//                                    if (!(entityiterator == entity) && entityiterator.getType().is(ModTags.EntityTypes.SEA_BORN)) {
//                                        amplifi = amplifi + 2;
//                                    }
//                                    if (amplifi > 29) {
//                                        amplifi = 29;
//                                        break;
//                                    }
//                                }
//                            }
//                        } else {
//                            {
//                                final Vec3 _center = new Vec3(x, y, z);
//                                List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(32 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
//                                for (Entity entityiterator : _entfound) {
//                                    if (!(entityiterator == entity) && entityiterator.getType().is(ModTags.EntityTypes.SEA_BORN)) {
//                                        amplifi = amplifi + 1;
//                                    }
//                                    if (amplifi > 9) {
//                                        amplifi = 9;
//                                        break;
//                                    }
//                                }
//                            }
//                        }
//                        if (amplifi >= 0) {
//                            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
//                                _entity.addEffect(new MobEffectInstance(ModMobEffects.STRENGTH_OF_CROWD.get(), 20, (int) amplifi, false, false));
//                        }
//                    }
//                } else {
//                    if ((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) < (entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) * 0.5) {
//                        if (!(entity instanceof LivingEntity _livEnt28 && _livEnt28.hasEffect(ModMobEffects.BOOST_OF_SILENCE.get()))) {
//                            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
//                                _entity.addEffect(new MobEffectInstance(ModMobEffects.BOOST_OF_SILENCE.get(), 20, (int) (MapVariables.get(world).strategySilence - 1)));
//                        }
//                    }
//                }
//                if (!(entity instanceof LivingEntity _livEnt30 && _livEnt30.hasEffect(MobEffects.REGENERATION))) {
//                    if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
//                        _entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20, (int) MapVariables.get(world).strategySilence));
//                }
//                if (entity instanceof Mob _mobEnt32 && _mobEnt32.isAggressive()) {
//                    if (!_mobEnt32.hasEffect(MobEffects.MOVEMENT_SPEED)) {
//                        if (!_mobEnt32.level().isClientSide())
//                            _mobEnt32.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 20, (int) MapVariables.get(world).strategySilence));
//                    }
//                }
//            }
//            if (world.getLevelData().getGameRules().getBoolean(ModGameRules.NATURAL_EVOLUTION)) {
//                if (Math.random() < 0.008 && !world.getEntitiesOfClass(Player.class, AABB.ofSize(new Vec3(x, y, z), 96, 96, 96), e -> true).isEmpty()) {
//                    pnt = Mth.nextDouble(RandomSource.create(), 0, 0.05);
//                    MapVariables.get(world).evoPointMigration = MapVariables.get(world).evoPointMigration + pnt;
//                    MapVariables.get(world).syncData(world);
//                    UpgradeMigraProcedure.execute(world);
//                    UpgradeSilenceProcedure.execute(world, entity, pnt);
//                }
//            }
//        }
//    }
//}
