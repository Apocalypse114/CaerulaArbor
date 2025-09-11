package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.Relic;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

public class GainRelicARMORProcedure {
    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;

        var cap = ModCapabilities.getPlayerVariables(entity);
        if (!Relic.KING_ARMOR.gained(cap)) {
            if (world instanceof Level _level) {
                if (!_level.isClientSide()) {
                    _level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.TOTEM_USE, SoundSource.NEUTRAL, 2, 1);
                    ((ServerLevel) _level).sendParticles(ParticleTypes.ENCHANTED_HIT, x, y, z, 72, 1, 1, 1, 1);
                } else {
                    _level.playLocalSound(x, y, z, SoundEvents.TOTEM_USE, SoundSource.NEUTRAL, 2, 1, false);
                    Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
                }
            }

            Relic.KING_ARMOR.gain(cap);
            double lives_left = cap.life;

            if (cap.life > 1) {
                cap.life = 1;
            }

            cap.shield = (int) (cap.shield + lives_left + 3);
            cap.syncPlayerVariables(entity);
        }
    }
}
