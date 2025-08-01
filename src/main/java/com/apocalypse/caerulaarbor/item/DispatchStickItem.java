
package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.init.ModMobEffects;
import com.apocalypse.caerulaarbor.init.ModTags;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class DispatchStickItem extends Item {
    public DispatchStickItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.COMMON));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(stack, level, list, flag);
        list.add(Component.translatable("item.caerula_arbor.dispatch_stick.description_0"));
        list.add(Component.translatable("item.caerula_arbor.dispatch_stick.description_1"));
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        final Vec3 center = new Vec3(entity.getX(), entity.getY(), entity.getZ());
        for (var entityiterator : world.getEntitiesOfClass(LivingEntity.class,
                new AABB(center, center).inflate(32), e -> true)
        ) {
            if (entityiterator.getType().is(ModTags.EntityTypes.SEA_BORN)
                    && !entityiterator.level().isClientSide()
            ) {
                entityiterator.addEffect(new MobEffectInstance(ModMobEffects.ANGER_OF_TIDE.get(), 131072, 0, false, true));
            }
        }
        return super.use(world, entity, hand);
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        final Vec3 center = new Vec3(target.getX(), target.getY(), target.getZ());
        for (var entity : target.level()
                .getEntitiesOfClass(Mob.class, new AABB(center, center).inflate(64 / 2d), e -> true)
        ) {
            if (entity.getType().is(ModTags.EntityTypes.SEA_BORN)
                    && target != entity
            ) {
                entity.setTarget(target);
            }
        }

        return super.hurtEnemy(stack, target, attacker);
    }
}
