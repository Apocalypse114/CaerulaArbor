
package com.apocalypse.caerulaarbor.item.relic.epic;

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

public class KingOfSarkazVesselItem extends RelicItem {
    public KingOfSarkazVesselItem() {
        super(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(Component.translatable("item.caerula_arbor.king_of_sarkaz_vessel.description_0").withStyle(ChatFormatting.AQUA));
        list.add(Component.translatable("item.caerula_arbor.king_of_sarkaz_vessel.description_1").withStyle(ChatFormatting.GRAY));
    }

    @Override
    public @NotNull ParticleOptions getGainParticle() {
        return ParticleTypes.DRIPPING_LAVA;
    }

    @Override
    public @NotNull SoundEvent getGainSound() {
        return SoundEvents.TOTEM_USE;
    }

    @Override
    public @Nullable Relic getRelic() {
        return Relic.SARKAZ_KING_ARTIFACT;
    }
}
