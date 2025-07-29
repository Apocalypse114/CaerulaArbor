
package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.init.ModTags;
import com.apocalypse.caerulaarbor.procedures.DeductPlayerSanityProcedure;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class FishReapMobEffect extends InvisibleMobEffect {
    public FishReapMobEffect() {
        super(MobEffectCategory.NEUTRAL, -6710785);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        LevelAccessor world = entity.level();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        double angle;
        if (entity instanceof Mob mob && mob.isAggressive() && entity.isAlive()) {
            for (int index0 = 0; index0 < 120; index0++) {
                angle = Mth.nextDouble(RandomSource.create(), 0, 6.283);
                if (world instanceof ServerLevel server) {
                    server.sendParticles(ParticleTypes.ELECTRIC_SPARK, (x + 4 * Math.sin(angle)), y, (z + 4 * Math.cos(angle)), 8, 0.1, 0.1, 0.1, 0.2);
                }
            }
            {
                final Vec3 _center = new Vec3(x, y, z);
                for (var entityiterator : world.getEntitiesOfClass(Mob.class, new AABB(_center, _center).inflate(8 / 2d), e -> true)) {
                    if (entityiterator.getType().is(ModTags.EntityTypes.SEA_BORN) && !(entityiterator == mob.getTarget())) {
                        continue;
                    }
                    if (entityiterator != entity) {
                        DeductPlayerSanityProcedure.execute(entityiterator,
                                (mob.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? mob.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0) * 12);
                    }
                }
            }
            entity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.DRY_OUT), entity),
                    (float) ((mob.getAttributes().hasAttribute(Attributes.MAX_HEALTH) ? mob.getAttribute(Attributes.MAX_HEALTH).getValue() : 0) * 0.01));
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
