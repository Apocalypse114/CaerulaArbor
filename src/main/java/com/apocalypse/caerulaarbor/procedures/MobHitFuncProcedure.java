package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
import com.apocalypse.caerulaarbor.init.ModAttributes;
import com.apocalypse.caerulaarbor.init.ModEntities;
import com.apocalypse.caerulaarbor.init.ModGameRules;
import com.apocalypse.caerulaarbor.init.ModTags;
import com.apocalypse.caerulaarbor.network.CaerulaArborModVariables;
import net.minecraft.commands.CommandSource;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.TagKey;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import javax.annotation.Nullable;

// TODO 把这坨铲了
@Mod.EventBusSubscriber
public class MobHitFuncProcedure {
    @SubscribeEvent
    public static void onEntityAttacked(LivingAttackEvent event) {
        if (event != null && event.getEntity() != null) {
//            execute(event, event.getEntity().level(), event.getEntity().getX(), event.getEntity().getY(), event.getEntity().getZ(), event.getSource(), event.getEntity(), event.getSource().getDirectEntity(), event.getSource().getEntity(),
//                    event.getAmount());
        }
    }

    public static void execute(LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity, Entity immediatesourceentity, Entity sourceentity, double amount) {
        execute(null, world, x, y, z, damagesource, entity, immediatesourceentity, sourceentity, amount);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, DamageSource damagesource, Entity entity, Entity immediatesourceentity, Entity sourceentity, double amount) {
        if (damagesource == null || entity == null || immediatesourceentity == null || sourceentity == null)
            return;

        double amplifi;
        double sklp;

        if (entity.getType().is(ModTags.EntityTypes.SEA_BORN) && !sourceentity.getType().is(ModTags.EntityTypes.SEA_BORN)) {
            if (CaerulaArborModVariables.MapVariables.get(world).strategyMigration > 0) {
                for (Entity entityiterator : world.getEntities(entity,
                        new AABB((x - (8 + CaerulaArborModVariables.MapVariables.get(world).strategyMigration * 24)), (y - 16), (z - (8 + CaerulaArborModVariables.MapVariables.get(world).strategyMigration * 24)),
                                (x + 8 + CaerulaArborModVariables.MapVariables.get(world).strategyMigration * 24), (y + 16), (z + 8 + CaerulaArborModVariables.MapVariables.get(world).strategyMigration * 24)))) {
                    if (entityiterator.getType().is(ModTags.EntityTypes.SEA_BORN)) {
                        if (entityiterator == sourceentity) {
                            continue;
                        }
                        if (entityiterator instanceof Mob _entity) {
                            _entity.getNavigation().moveTo(x, y, z,
                                    (_entity.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED) ? _entity.getAttribute(Attributes.MOVEMENT_SPEED).getValue() : 0) * 1.2);
                        }
                        if (entityiterator instanceof Mob _entity && sourceentity instanceof LivingEntity _ent)
                            _entity.setTarget(_ent);
                    }
                }
            }
        }
        if (entity instanceof Player) {
            if ((entity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable())).player_oceanization >= 3) {
                if (CaerulaArborModVariables.MapVariables.get(world).strategyMigration > 0) {
                    for (Entity entityiterator : world.getEntities(entity,
                            new AABB((x - (8 + CaerulaArborModVariables.MapVariables.get(world).strategyMigration * 24)), (y - 16), (z - (8 + CaerulaArborModVariables.MapVariables.get(world).strategyMigration * 24)),
                                    (x + 8 + CaerulaArborModVariables.MapVariables.get(world).strategyMigration * 24), (y + 16), (z + 8 + CaerulaArborModVariables.MapVariables.get(world).strategyMigration * 24)))) {
                        if (entityiterator.getType().is(ModTags.EntityTypes.SEA_BORN)) {
                            if (entityiterator == sourceentity) {
                                continue;
                            }
                            if (entityiterator instanceof Mob _entity) {
                                _entity.getNavigation().moveTo(x, y, z,
                                        _entity.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED) ? _entity.getAttribute(Attributes.MOVEMENT_SPEED).getValue() : 0);
                            }
                            if (entityiterator instanceof Mob _entity && sourceentity instanceof LivingEntity _ent)
                                _entity.setTarget(_ent);
                        }
                    }
                }
            }
        }
        if (sourceentity.getType().is(ModTags.EntityTypes.SEA_BORN)) {
            if (world.getLevelData().getGameRules().getBoolean(ModGameRules.NATURAL_EVOLUTION)) {
                CaerulaArborModVariables.MapVariables.get(world).evo_point_grow = CaerulaArborModVariables.MapVariables.get(world).evo_point_grow + amount * 0.025;
                CaerulaArborModVariables.MapVariables.get(world).syncData(world);
                UpgradeGrowProcedure.execute(world);
                UpgradeSilenceProcedure.execute(world, entity, amount * 0.025);
            }
        }
        if (entity.getType().is(ModTags.EntityTypes.SEA_BORN)) {
            if (world.getLevelData().getGameRules().getBoolean(ModGameRules.NATURAL_EVOLUTION)) {
                CaerulaArborModVariables.MapVariables.get(world).evo_point_subsisting = CaerulaArborModVariables.MapVariables.get(world).evo_point_subsisting + amount * 0.025;
                CaerulaArborModVariables.MapVariables.get(world).syncData(world);
                UpgradeSubsisProcedure.execute(world);
                UpgradeSilenceProcedure.execute(world, entity, amount * 0.025);
            }
            if (entity.isPassenger() && GameplayConfig.ENABLE_MOB_BREAK.get() && world.getLevelData().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                (entity.getVehicle()).hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MOB_ATTACK), entity), 8);
            }
            if (CaerulaArborModVariables.MapVariables.get(world).strategySubsisting >= 3 && Math.random() < 0.5) {
                if (GameplayConfig.ENABLE_MOB_BREAK.get() && world.getLevelData().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                    if (!entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("caerula_arbor:oceanspawn"))) && DetectForTrailProcedure.execute(world, x, y, z)) {
                        new Object() {
                            void timedLoop(int timedloopiterator, int timedlooptotal, int ticks) {
                                if (world instanceof Level _level) {
                                    if (!_level.isClientSide()) {
                                        _level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 1, 1);
                                    } else {
                                        _level.playLocalSound(x, y, z, SoundEvents.GENERIC_EAT, SoundSource.NEUTRAL, 1, 1, false);
                                    }
                                }
                                final int tick2 = ticks;
                                CaerulaArborMod.queueServerWork(tick2, () -> {
                                    if (timedlooptotal > timedloopiterator + 1) {
                                        timedLoop(timedloopiterator + 1, timedlooptotal, tick2);
                                    }
                                });
                            }
                        }.timedLoop(0, 3, 5);
                        if (entity instanceof LivingEntity _entity)
                            _entity.setHealth((entity instanceof LivingEntity _livEnt ? _livEnt.getHealth() : -1) + 8);
                    }
                }
            }
            if (CaerulaArborModVariables.MapVariables.get(world).strategy_silence > 0) {
                if (!entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("caerula_arbor:bossoffspring"))) && !entity.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("caerula_arbor:oceanspawn")))) {
                    if (Math.random() < -0.01 + 0.02 * CaerulaArborModVariables.MapVariables.get(world).strategy_silence && !(sourceentity == entity)) {
                        if ((entity instanceof LivingEntity _livingEntity50 && _livingEntity50.getAttributes().hasAttribute(ModAttributes.SUMMONABLE.get())
                                ? _livingEntity50.getAttribute(ModAttributes.SUMMONABLE.get()).getBaseValue()
                                : 0) == 1) {
                            {
                                if (!entity.level().isClientSide() && entity.getServer() != null) {
                                    entity.getServer().getCommands().performPrefixedCommand(
                                            new CommandSourceStack(CommandSource.NULL, entity.position(), entity.getRotationVector(), entity.level() instanceof ServerLevel ? (ServerLevel) entity.level() : null, 4, entity.getName().getString(),
                                                    entity.getDisplayName(), entity.level().getServer(), entity),
                                            ("summon " + ForgeRegistries.ENTITY_TYPES.getKey(entity.getType()).toString() + " ~" + Mth.nextDouble(RandomSource.create(), -1, 1) + " ~ ~" + Mth.nextDouble(RandomSource.create(), -1, 1)));
                                }
                            }
                            if (world instanceof ServerLevel _level)
                                _level.sendParticles(ParticleTypes.CLOUD, x, y, z, 64, 1.5, 2, 1.5, 0.1);
                            if (CaerulaArborModVariables.MapVariables.get(world).strategy_silence >= 3) {
                                if (Math.random() < 0.5) {
                                    if (world instanceof ServerLevel server) {
                                        var type = switch (Mth.nextInt(RandomSource.create(), 0, 8)) {
                                            case 0 -> ModEntities.NETHERSEA_FOUNDER;
                                            case 1 -> ModEntities.NETHERSEA_REEFBREAKER;
                                            case 2 -> ModEntities.POCKET_SEA_CRAWLER;
                                            case 3 -> ModEntities.NETHERSEA_BRANDGUIDER;
                                            case 4 -> ModEntities.PRIMAL_SEA_PIERCER;
                                            case 5 -> ModEntities.BASIN_SEA_REAPER;
                                            case 6 -> ModEntities.NETHERSEA_SWARMCALLER;
                                            case 7 -> ModEntities.RETCHING_BROODMOTHER;
                                            case 8 -> ModEntities.SKIMMING_SEA_DRIFTER;
                                            default -> null;
                                        };

                                        if (type != null) {
                                            var entityToSpawn = ((RegistryObject<? extends EntityType<?>>) type).get().spawn(server, BlockPos.containing(Mth.nextDouble(RandomSource.create(), -1, 1) + x, y, Mth.nextDouble(RandomSource.create(), -1, 1) + z), MobSpawnType.MOB_SUMMONED);
                                            if (entityToSpawn != null) {
                                                entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
                                            }
                                        }

                                        server.sendParticles(ParticleTypes.CLOUD, x, y, z, 32, 1, 1, 1, 0.1);
                                    }
                                }
                            }
                            if (entity instanceof LivingEntity _livingEntity56 && _livingEntity56.getAttributes().hasAttribute(ModAttributes.SUMMONABLE.get()))
                                _livingEntity56.getAttribute(ModAttributes.SUMMONABLE.get()).setBaseValue(0);
                        }
                    }
                }
                if (CaerulaArborModVariables.MapVariables.get(world).strategy_silence >= 3) {
                    if (Math.random() < -0.4 + CaerulaArborModVariables.MapVariables.get(world).strategy_silence * 0.2) {
                        sourceentity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.THORNS)),
                                (float) ((entity instanceof LivingEntity _livingEntity57 && _livingEntity57.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? _livingEntity57.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0) * 0.5));
                    }
                }
            }
        }
    }
}
