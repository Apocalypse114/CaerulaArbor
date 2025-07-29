
package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.entity.FishSplashEntity;
import com.apocalypse.caerulaarbor.init.ModBlocks;
import com.apocalypse.caerulaarbor.init.ModEntities;
import com.apocalypse.caerulaarbor.init.ModTags;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class SplasherAttackMobEffect extends MobEffect {
    public SplasherAttackMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -13421773);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        var world = entity.level();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        double num = 0;
        double rand;

        for (Entity entityiterator : world.getEntities(entity, new AABB((x + 48), (y + 6), (z + 48), (x - 48), (y - 6), (z - 48)))) {
            if ((entityiterator instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) < 5) {
                continue;
            }
            if (entityiterator.getType().is(ModTags.EntityTypes.SEA_BORN)) {
                continue;
            }
            if (entityiterator instanceof Player) {
                if ((ModCapabilities.getPlayerVariables(entityiterator)).seabornization >= 3) {
                    continue;
                }
            }
            rand = Mth.nextDouble(RandomSource.create(), 7, 11);
            if (world.getBlockState(entityiterator.blockPosition()).getBlock() == ModBlocks.SEA_TRAIL_GROWN.get()) {
                if (world instanceof ServerLevel projectileLevel) {
                    Projectile _entityToSpawn = new Object() {
                        public Projectile getArrow(Level level, Entity shooter, float damage, int knockback) {
                            AbstractArrow entityToSpawn = new FishSplashEntity(ModEntities.FISH_SPLASH.get(), level);
                            entityToSpawn.setOwner(shooter);
                            entityToSpawn.setBaseDamage(damage);
                            entityToSpawn.setKnockback(knockback);
                            entityToSpawn.setSilent(true);
                            entityToSpawn.setCritArrow(true);
                            return entityToSpawn;
                        }
                    }.getArrow(projectileLevel, (Entity) entity, 5, 0);
                    _entityToSpawn.setPos((entityiterator.getX()), (entityiterator.getY() + rand), (entityiterator.getZ()));
                    _entityToSpawn.shoot(0, (-1), 0, 1.5F, 0);
                    projectileLevel.addFreshEntity(_entityToSpawn);
                }
                num += 1;

                for (int i = 0; i < 40; i++) {
                    int finalI = i;
                    CaerulaArborMod.queueServerWork(i, () -> {
                        if (world instanceof ServerLevel server)
                            server.sendParticles(ParticleTypes.END_ROD, (entityiterator.getX() + ((x - entityiterator.getX()) / 40) * finalI), (entityiterator.getY() + 9 + ((y - (entityiterator.getY() + 9)) / 40) * finalI),
                                    (entityiterator.getZ() + ((z - entityiterator.getZ()) / 40) * finalI), 4, 0.1, 0.1, 0.1, 0.01);
                    });
                }
            }

            if (num >= 3) {
                break;
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 40 == 0;
    }

    @Override
    public void initializeClient(java.util.function.Consumer<IClientMobEffectExtensions> consumer) {
        consumer.accept(new IClientMobEffectExtensions() {
            @Override
            public boolean isVisibleInInventory(MobEffectInstance effect) {
                return false;
            }

            @Override
            public boolean isVisibleInGui(MobEffectInstance effect) {
                return false;
            }
        });
    }
}
