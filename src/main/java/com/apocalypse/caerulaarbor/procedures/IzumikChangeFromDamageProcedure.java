//package com.apocalypse.caerulaarbor.procedures;
//
//import com.apocalypse.caerulaarbor.CaerulaArborMod;
//import net.minecraft.world.level.LevelAccessor;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.core.particles.ParticleTypes;
//
//public class IzumikChangeFromDamageProcedure {
//    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, Entity sourceentity) {
//        if (entity == null || sourceentity == null)
//            return;
//        boolean success = false;
//        if (entity.distanceTo(sourceentity) <= 3.5) {
//            if (!world.isClientSide()) {
//                if (world instanceof ServerLevel serverLevel) {
//                    success = EntitySpawnHelper.spawnRandomEntityFromTag(serverLevel, x, y, z);
//                }
//            }
//            if (success) {
//                CaerulaArborMod.LOGGER.info(("offspring at "+x+" "+y+" "+z+" changes"));
//                if (world instanceof ServerLevel _level)
//                    _level.sendParticles(ParticleTypes.CLOUD, x, (y + 0.75), z, 32, 0.75, 0.75, 0.75, 0.1);
//                if (!entity.level().isClientSide())
//                    entity.discard();
//            }
//        }
//    }
//}
