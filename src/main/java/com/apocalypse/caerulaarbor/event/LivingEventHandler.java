package com.apocalypse.caerulaarbor.event;

import com.apocalypse.caerulaarbor.api.event.RelicEvent;
import com.apocalypse.caerulaarbor.block.SeaTrailBaseBlock;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.sanity.SanityInjuryCapability;
import com.apocalypse.caerulaarbor.item.relic.IRelic;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class LivingEventHandler {

    @SubscribeEvent
    public static void onEntityTick(LivingEvent.LivingTickEvent event) {
        var living = event.getEntity();

        living.getCapability(ModCapabilities.SANITY_INJURY).ifPresent(cap -> {
            if (cap instanceof SanityInjuryCapability capImpl) capImpl.tick();
        });
    }

    @SubscribeEvent
    public static void onGainRelic(RelicEvent.Gain event) {
        if (!(event.player instanceof Player player)) return;
        if (!(event.relic.item instanceof IRelic iRelic)) return;
        if (player.level().isClientSide) return;

        for (Map.Entry<Attribute, AttributeModifier> entry : iRelic.getRelicAttributeModifiers(player).entrySet()) {
            AttributeInstance attributeinstance = player.getAttributes().getInstance(entry.getKey());
            if (attributeinstance != null) {
                AttributeModifier attributemodifier = entry.getValue();
                attributeinstance.removeModifier(attributemodifier);
                attributeinstance.addPermanentModifier(new AttributeModifier(attributemodifier.getId(), attributemodifier.getName(),
                        attributemodifier.getAmount(), attributemodifier.getOperation()));
            }
        }
    }

    @SubscribeEvent
    public static void onUpdateRelic(RelicEvent.Update event) {
        if (!(event.player instanceof Player player)) return;
        if (!(event.relic.item instanceof IRelic iRelic)) return;
        if (player.level().isClientSide) return;

        for (Map.Entry<Attribute, AttributeModifier> entry : iRelic.getRelicAttributeModifiers(player).entrySet()) {
            AttributeInstance attributeinstance = player.getAttributes().getInstance(entry.getKey());
            if (attributeinstance != null) {
                attributeinstance.removeModifier(entry.getValue());
            }
        }

        for (Map.Entry<Attribute, AttributeModifier> entry : iRelic.getRelicAttributeModifiers(player).entrySet()) {
            AttributeInstance attributeinstance = player.getAttributes().getInstance(entry.getKey());
            if (attributeinstance != null) {
                AttributeModifier attributemodifier = entry.getValue();
                attributeinstance.removeModifier(attributemodifier);
                attributeinstance.addPermanentModifier(new AttributeModifier(attributemodifier.getId(), attributemodifier.getName(),
                        attributemodifier.getAmount(), attributemodifier.getOperation()));
            }
        }
    }

    @SubscribeEvent
    public static void onRemoveRelic(RelicEvent.Remove event) {
        if (!(event.player instanceof Player player)) return;
        if (!(event.relic.item instanceof IRelic iRelic)) return;
        if (player.level().isClientSide) return;

        for (Map.Entry<Attribute, AttributeModifier> entry : iRelic.getRelicAttributeModifiers(player).entrySet()) {
            AttributeInstance attributeinstance = player.getAttributes().getInstance(entry.getKey());
            if (attributeinstance != null) {
                attributeinstance.removeModifier(entry.getValue());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if (!(level instanceof ServerLevel serverLevel)) return;
        BlockPos entityPos = entity.blockPosition();
        Iterable<BlockPos> iter = BlockPos.betweenClosed(
                entityPos.offset(-1, -1, -1),
                entityPos.offset(1, 1, 1));
        for (BlockPos pos : iter) {
            if (!level.isInWorldBounds(pos)) continue;
            BlockState state = level.getBlockState(pos);
            if (state.getBlock() instanceof SeaTrailBaseBlock seaTrailBaseBlock) {
                seaTrailBaseBlock.onEntityDeathNearby(serverLevel, pos, state);
            }
        }
    }
}
