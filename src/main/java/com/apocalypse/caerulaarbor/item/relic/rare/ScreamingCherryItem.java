package com.apocalypse.caerulaarbor.item.relic.rare;

import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.init.ModBlocks;
import com.apocalypse.caerulaarbor.item.relic.IRelic;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class ScreamingCherryItem extends BlockItem implements IRelic {

    public ScreamingCherryItem() {
        super(ModBlocks.SCREAMING_CHERRY.get(), new Item.Properties().stacksTo(64).rarity(Rarity.UNCOMMON)
                .food((new FoodProperties.Builder()).nutrition(6).saturationMod(0.1f)
                        .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 240, 1), 1.0f)
                        .alwaysEat().build())
        );
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, Level level, List<Component> list, @NotNull TooltipFlag flag) {
        list.add(Component.translatable("item.caerula_arbor.screaming_cherry.des_1").withStyle(ChatFormatting.AQUA));
        list.add(Component.translatable("item.caerula_arbor.screaming_cherry.des_2").withStyle(ChatFormatting.GRAY).withStyle(ChatFormatting.ITALIC));
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull ItemStack finishUsingItem(ItemStack pStack, Level world, LivingEntity entity) {
        Relic.modify(entity, cap -> Relic.SCREAMING_CHERRY.gain(entity));

        ItemStack rt = new ItemStack(Items.GLASS_BOTTLE);
        if (super.finishUsingItem(pStack, world, entity).isEmpty()) {
            return rt;
        } else {
            if (entity instanceof Player player && !player.getAbilities().instabuild) {
                if (!player.getInventory().add(rt)) {
                    player.drop(rt, false);
                }
            }
            return pStack;
        }
    }

    @Override
    @ParametersAreNonnullByDefault
    protected @NotNull SoundEvent getPlaceSound(BlockState state, Level world, BlockPos pos, Player entity) {
        return SoundEvents.GLASS_PLACE;
    }
}
