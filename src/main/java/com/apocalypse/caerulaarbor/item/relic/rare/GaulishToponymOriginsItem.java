
package com.apocalypse.caerulaarbor.item.relic.rare;

import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.item.relic.RelicItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class GaulishToponymOriginsItem extends RelicItem {
    public GaulishToponymOriginsItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.UNCOMMON));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(Component.translatable("item.caerula_arbor.gaulish_toponym_origins.description_0").withStyle(ChatFormatting.AQUA));
        list.add(Component.translatable("item.caerula_arbor.gaulish_toponym_origins.description_1").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public @Nullable Relic getRelic() {
        return Relic.UTIL_TOPONYM;
    }

    @Override
    public int getAddedShield() {
        return 6;
    }

    @Override
    public @Nullable SoundEvent getGainSound() {
        return SoundEvents.AMETHYST_BLOCK_RESONATE;
    }

    @Override
    public @Nullable ParticleOptions getGainParticle() {
        return ParticleTypes.NAUTILUS;
    }

    @Override
    public boolean checkUsedMark() {
        return true;
    }
}
