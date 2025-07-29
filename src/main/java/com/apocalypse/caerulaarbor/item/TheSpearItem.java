
package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;

public class TheSpearItem extends SwordItem {
    public TheSpearItem() {
        super(new Tier() {
            public int getUses() {
                return 385;
            }

            public float getSpeed() {
                return 8f;
            }

            public float getAttackDamageBonus() {
                return 8f;
            }

            public int getLevel() {
                return 3;
            }

            public int getEnchantmentValue() {
                return 9;
            }

            public @NotNull Ingredient getRepairIngredient() {
                return Ingredient.of();
            }
        }, 3, -2.5f, new Item.Properties().fireResistant());
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        ItemStack itemstack = ar.getObject();
        var cap = ModCapabilities.getPlayerVariables(entity);
        if (!Relic.KING_SPEAR.gained(cap)) {
            if ((LevelAccessor) world instanceof Level _level) {
                if (!_level.isClientSide()) {
                    _level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.TOTEM_USE, SoundSource.NEUTRAL, 2, 1);
                    ((ServerLevel) _level).sendParticles(ParticleTypes.ENCHANTED_HIT, x, y, z, 72, 1, 1, 1, 1);
                } else {
                    _level.playLocalSound(x, y, z, SoundEvents.TOTEM_USE, SoundSource.NEUTRAL, 2, 1, false);
                    Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
                }
            }
            Relic.KING_SPEAR.gain(cap);
            cap.syncPlayerVariables(entity);
        }
        return ar;
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);

        if (!selected || !(entity instanceof LivingEntity living) || !living.hasEffect(ModMobEffects.SPEAR_FIGHT.get()))
            return;

        if (!living.level().isClientSide) {
            living.addEffect(new MobEffectInstance(ModMobEffects.SPEAR_FIGHT.get(), 60, 0, false, false));
        }
    }
}
