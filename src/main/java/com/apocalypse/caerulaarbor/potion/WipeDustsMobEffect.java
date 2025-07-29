
package com.apocalypse.caerulaarbor.potion;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class WipeDustsMobEffect extends InvisibleMobEffect {
    public WipeDustsMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -6684724);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        var world = entity.level();

        if (entity.hasEffect(MobEffects.REGENERATION) && (entity instanceof Player _playerHasItem && _playerHasItem.getInventory().contains(new ItemStack(Items.BRUSH)))) {
            final Vec3 _center = new Vec3(entity.getX(), entity.getY(), entity.getZ());
            for (Entity entityiterator : world.getEntitiesOfClass(Monster.class, new AABB(_center, _center).inflate(10 / 2d), e -> true)) {
                if (entityiterator.getType().is(TagKey.create(Registries.ENTITY_TYPE, new ResourceLocation("caeula_arbor:oceanoffspring")))) {
                    entityiterator.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC), entity),
                            (float) (5 * (1 + (entity.hasEffect(MobEffects.REGENERATION) ? entity.getEffect(MobEffects.REGENERATION).getAmplifier() : 0))));
                    if (world instanceof ServerLevel _level)
                        _level.sendParticles(ParticleTypes.WAX_OFF, (entityiterator.getX()), (entityiterator.getY()), (entityiterator.getZ()), 48, 0.8, 1, 0.8, 0.1);
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
    }
}
