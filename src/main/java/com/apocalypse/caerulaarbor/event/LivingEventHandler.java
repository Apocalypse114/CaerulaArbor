package com.apocalypse.caerulaarbor.event;

import com.apocalypse.caerulaarbor.api.event.RelicEvent;
import com.apocalypse.caerulaarbor.block.SeaTrailBaseBlock;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.init.ModDamageTypes;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import com.apocalypse.caerulaarbor.item.relic.IRelic;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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

        living.getCapability(ModCapabilities.SANITY_INJURY).ifPresent(
                cap -> {
                    if (cap.isImmune()) {
                        cap.regenerate(5);
                        return;
                    }

                    if (cap.getValue() < 0) {
                        if (living.level().isClientSide) {
                            living.level().playLocalSound(living.getX(), living.getY(), living.getZ(), SoundEvents.ELDER_GUARDIAN_CURSE,
                                    SoundSource.NEUTRAL, 2.2f, 1, false);
                        } else {
                            if (living instanceof Player) {
                                living.addEffect(new MobEffectInstance(ModMobEffects.DIZZY.get(), 200, 0, false, false));
                                living.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0, false, true));
                                living.hurt(ModDamageTypes.causeNervousImpairmentDamage(living.level().registryAccess(), null), 12);
                            } else {
                                living.addEffect(new MobEffectInstance(ModMobEffects.DIZZY.get(), 60, 0, false, false));
                                living.hurt(ModDamageTypes.causeNervousImpairmentDamage(living.level().registryAccess(), null),
                                        Math.min(72, living.getMaxHealth() * 0.8f));
                            }

                            living.level().playSound(null, living.getX(), living.getY(), living.getZ(), SoundEvents.ELDER_GUARDIAN_CURSE,
                                    SoundSource.NEUTRAL, 2.2f, 1);
                        }

                        cap.setImmune(true);
                    }
                }
        );
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
