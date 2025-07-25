package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.init.ModTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;

public class RangedSettleProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
		if (entity == null)
			return;
		for (Entity entityiterator : world.getEntities(entity, new AABB((x - 4), (y - 0.5), (z - 4), (x + 4), (y + 2), (z + 4)))) {
			if ((entityiterator != null ? entity.distanceTo(entityiterator) : -1) <= 4) {
				if (entityiterator.getType().is(ModTags.EntityTypes.SEA_BORN)) {
					if (!(entityiterator == (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null))) {
						continue;
					}
				}
				DeductPlayerSanityProcedure.execute(entityiterator,
						(entity instanceof LivingEntity _livingEntity4 && _livingEntity4.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? _livingEntity4.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0) * 4);
				entityiterator.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC)),
						(float) (entity instanceof LivingEntity _livingEntity5 && _livingEntity5.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? _livingEntity5.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0));
			}
		}
//		if (MapVariables.get(world).strategyGrow >= 3) {
//			for (Entity entityiterator : world.getEntities(entity, new AABB((x - 7), (y - 1), (z - 7), (x + 7), (y + 3), (z + 7)))) {
//				if ((entityiterator != null ? entity.distanceTo(entityiterator) : -1) <= 7) {
//					if (entityiterator.getType().is(ModTags.EntityTypes.SEA_BORN)) {
//						if (!(entityiterator == (entity instanceof Mob _mobEnt ? (Entity) _mobEnt.getTarget() : null))) {
//							continue;
//						}
//					}
//					DeductPlayerSanityProcedure.execute(entityiterator,
//							(entity instanceof LivingEntity _livingEntity13 && _livingEntity13.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? _livingEntity13.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0) * 4);
//					entityiterator.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.MAGIC)),
//							(float) (entity instanceof LivingEntity _livingEntity14 && _livingEntity14.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? _livingEntity14.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0));
//				}
//			}
//		}
	}
}
