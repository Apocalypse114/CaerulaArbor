
package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.init.ModTags;
import com.apocalypse.caerulaarbor.procedures.DeductPlayerSanityProcedure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.extensions.common.IClientMobEffectExtensions;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class UmbrellaSettleMobEffect extends MobEffect {
    public UmbrellaSettleMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -6710785);
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

        for (var target : world.getEntitiesOfClass(
                LivingEntity.class,
                new AABB(x - 4, y - 0.5, z - 4, x + 4, y + 2, z + 4),
                e -> e != entity
                        && e.distanceTo(entity) <= 4
                        && !e.getType().is(ModTags.EntityTypes.SEA_BORN)
                        && (e == (entity instanceof Mob _mobEnt ? _mobEnt.getTarget() : null))
        )) {
            var attribute = entity.getAttribute(Attributes.ATTACK_DAMAGE);
            var attackDamage = attribute != null ? attribute.getValue() : 0;

            DeductPlayerSanityProcedure.execute(target, attackDamage * 4);
            target.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC)), (float) attackDamage);
        }
//		if (MapVariables.get(world).strategyGrow >= 3) {
//			for (Entity target : world.getEntities(entity, new AABB((x - 7), (y - 1), (z - 7), (x + 7), (y + 3), (z + 7)))) {
//				if ((target != null ? entity.distanceTo(target) : -1) <= 7) {
//					if (target.getType().is(ModTags.EntityTypes.SEA_BORN)) {
//						if (!(target == (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null))) {
//							continue;
//						}
//					}
//					DeductPlayerSanityProcedure.execute(target,
//							(entity instanceof LivingEntity _livingEntity13 && _livingEntity13.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? _livingEntity13.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0) * 4);
//					target.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC)),
//							(float) (entity instanceof LivingEntity _livingEntity14 && _livingEntity14.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? _livingEntity14.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0));
//				}
//			}
//		}
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 20 == 0;
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
