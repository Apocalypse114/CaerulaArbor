
package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.Relic;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class RelicCursedRESEARCHItem extends Item {
    public RelicCursedRESEARCHItem() {
        super(new Item.Properties().stacksTo(1).rarity(Rarity.EPIC));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack itemstack, Level level, @NotNull List<Component> list, @NotNull TooltipFlag flag) {
        super.appendHoverText(itemstack, level, list, flag);
        list.add(Component.translatable("item.caerula_arbor.relic_cursed_research.description_0"));
        list.add(Component.translatable("item.caerula_arbor.relic_cursed_research.description_1"));
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        var cap = ModCapabilities.getPlayerVariables(entity);

        if (!Relic.CURSED_RESEARCH.gained(cap)) {
            Relic.CURSED_RESEARCH.set(cap, 1);
            cap.syncPlayerVariables(entity);

            if ((LevelAccessor) world instanceof Level _level) {
                if (!_level.isClientSide()) {
                    _level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD.get(), SoundSource.NEUTRAL, 2, 1);
                } else {
                    _level.playLocalSound(x, y, z, SoundEvents.AMBIENT_SOUL_SAND_VALLEY_MOOD.get(), SoundSource.NEUTRAL, 2, 1, false);
                }
            }
            if ((LevelAccessor) world instanceof ServerLevel _level)
                _level.sendParticles(ParticleTypes.CRIMSON_SPORE, x, y, z, 99, 1, 1, 1, 1);
            if (world.isClientSide())
                Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
        }
    }
}
