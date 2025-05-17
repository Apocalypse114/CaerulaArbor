package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.entity.RouteFractalEntity;
import com.apocalypse.caerulaarbor.init.CaerulaArborModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.Comparator;
import java.util.List;

public class SummonFractalProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z) {
		double num = 0;
		double dx = 0;
		double dz = 0;
		num = 0;
		{
			final Vec3 _center = new Vec3(x, y, z);
			List<Entity> _entfound = world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(64 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList();
			for (Entity entityiterator : _entfound) {
				if (entityiterator instanceof RouteFractalEntity) {
					num = num + 1;
				}
			}
		}
		if (num < 12) {
			dx = Mth.nextDouble(RandomSource.create(), -0.5, 0.5);
			dz = Mth.nextDouble(RandomSource.create(), -0.5, 0.5);
			if (world instanceof ServerLevel _level) {
				Entity entityToSpawn = CaerulaArborModEntities.ROUTE_FRACTAL.get().spawn(_level, BlockPos.containing(x + dx, y, z + dz), MobSpawnType.MOB_SUMMONED);
				if (entityToSpawn != null) {
					entityToSpawn.setYRot(world.getRandom().nextFloat() * 360F);
				}
			}
			if (world instanceof ServerLevel _level)
				_level.sendParticles(ParticleTypes.CLOUD, (x + dx), y, (z + dz), 48, 0.5, 1, 0.5, 0.1);
		}
	}
}
