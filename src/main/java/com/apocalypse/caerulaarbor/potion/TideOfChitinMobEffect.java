
package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.init.ModParticleTypes;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class TideOfChitinMobEffect extends InvisibleMobEffect {
    public TideOfChitinMobEffect() {
        super(MobEffectCategory.BENEFICIAL, -13382401);
        this.addAttributeModifier(Attributes.ATTACK_DAMAGE, "6d392e1f-11d2-3f61-a82f-7dcfd9a507be", 1, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.addAttributeModifier(Attributes.MAX_HEALTH, "45591a17-debe-3f82-bab4-0fe652f64fd9", 1, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    @Override
    @ParametersAreNonnullByDefault
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.addAttributeModifiers(entity, attributeMap, amplifier);

        entity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
            capability.chitin_knife_selected = entity.getMainHandItem().copy();
            capability.syncPlayerVariables(entity);
        });

        CaerulaArborMod.queueServerWork(5, () -> {
            entity.setHealth(entity.getMaxHealth());
        });
    }

    @Override
    @ParametersAreNonnullByDefault
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        LevelAccessor world = entity.level();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        if (!(entity.getMainHandItem().getItem() == entity.getCapability(ModCapabilities.PLAYER_VARIABLE)
                .orElse(new PlayerVariable())
                .chitin_knife_selected.getItem())
        ) {
            if (world instanceof ServerLevel server) {
                server.playLocalSound(x, y, z, SoundEvents.BEACON_DEACTIVATE, SoundSource.NEUTRAL, 3.2f, 1, false);
            }
            entity.removeEffect(ModMobEffects.TIDE_OF_CHITIN.get());
        }
        if (entity.getCapability(ModCapabilities.PLAYER_VARIABLE)
                .orElse(new PlayerVariable())
                .kingShowPtc
        ) {
            world.addParticle(ModParticleTypes.KNIFEPTC.get(),
                    x + Mth.nextDouble(RandomSource.create(), -0.45, 0.45),
                    y + Mth.nextDouble(RandomSource.create(), 0, entity.getBbHeight() * 0.8),
                    z + Mth.nextDouble(RandomSource.create(), -0.45, 0.45),
                    Math.sin(Mth.nextDouble(RandomSource.create(), 0, 6.283)),
                    0.1,
                    Math.cos(Mth.nextDouble(RandomSource.create(), 0, 6.283))
            );
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}
