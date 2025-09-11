package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.init.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelAccessor;

public class DetectForTrailProcedure {
    public static boolean execute(LevelAccessor world, double x, double y, double z) {
        boolean breaked = false;
        double dx;
        double dy;
        double dz;
        dx = -2;

        loop:
        for (int index0 = 0; index0 < 5; index0++) {
            dz = -2;
            for (int index1 = 0; index1 < 5; index1++) {
                dy = -2;
                for (int index2 = 0; index2 < 4; index2++) {
                    var pos = BlockPos.containing(x + dx, y + dy, z + dz);

                    if (world.getBlockState(pos).getBlock() == ModBlocks.NETHERSEA_BRAND_GROWN.get()) {
                        world.destroyBlock(pos, false);
                        breaked = true;
                        break loop;
                    }
                    dy = dy + 1;
                }
                dz = dz + 1;
            }
            dx = dx + 1;
        }
        return breaked;
    }
}
