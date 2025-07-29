
package com.apocalypse.caerulaarbor.potion;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import com.apocalypse.caerulaarbor.procedures.DeductPlayerSanityProcedure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

public class InfestedMobEffect extends MobEffect {
    public InfestedMobEffect() {
        super(MobEffectCategory.NEUTRAL, -3407668);
    }

    @Override
    public List<ItemStack> getCurativeItems() {
        return new ArrayList<>();
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity entity, int amplifier) {
        var world = entity.level();
        if ((ModCapabilities.getPlayerVariables(entity)).seabornization < 3) {
            double dam = entity.getMaxHealth() * Mth.nextDouble(RandomSource.create(), 0.1, 0.25) * (amplifier + 1);
            if (entity.hasEffect(ModMobEffects.POWER_OF_ANCHOR.get())) {
                dam *= 0.1;
            }
            entity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(ResourceKey.create(Registries.DAMAGE_TYPE, new ResourceLocation("caerula_arbor:oceanize_damage")))), (float) dam);

            if (!entity.hasEffect(ModMobEffects.POWER_OF_ANCHOR.get()) && Math.random() < 0.33) {
                var effect = switch (Mth.nextInt(RandomSource.create(), 0, 7)) {
                    case 0 -> MobEffects.BLINDNESS;
                    case 1 -> MobEffects.POISON;
                    case 2 -> MobEffects.HUNGER;
                    case 3 -> MobEffects.MOVEMENT_SLOWDOWN;
                    case 4 -> MobEffects.WEAKNESS;
                    case 5 -> MobEffects.CONFUSION;
                    case 6 -> ModMobEffects.FROZEN.get();
                    case 7 -> ModMobEffects.DIZZY.get();
                    default -> throw new IllegalStateException("Unknown damage type");
                };

                if (!entity.level().isClientSide) {
                    entity.addEffect(new MobEffectInstance(effect, 160, (int) (double) amplifier));
                }
            }
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMap, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMap, amplifier);
        var world = entity.level();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        if (entity instanceof Player) {
            entity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
                capability.seabornization = Math.min(amplifier, 2) + 1;
                capability.syncPlayerVariables(entity);
            });
            DeductPlayerSanityProcedure.execute(entity, 750 * (amplifier + 1));

            if (!world.isClientSide()) {
                world.playSound(null, BlockPos.containing(x, y, z), SoundEvents.ZOMBIE_INFECT, SoundSource.PLAYERS, 2, 1);
            } else {
                world.playLocalSound(x, y, z, SoundEvents.ZOMBIE_INFECT, SoundSource.PLAYERS, 2, 1, false);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 40 == 0;
    }
}
