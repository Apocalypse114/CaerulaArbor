package com.apocalypse.caerulaarbor.event;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.api.event.RelicEvent;
import com.apocalypse.caerulaarbor.block.SeaTrailBaseBlock;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.sanity.SanityInjuryCapability;
import com.apocalypse.caerulaarbor.init.ModAttributes;
import com.apocalypse.caerulaarbor.init.ModTags;
import com.apocalypse.caerulaarbor.item.relic.IRelic;
import com.apocalypse.caerulaarbor.network.CaerulaArborModVariables;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.warden.Warden;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
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

    @SubscribeEvent
    public static void onFinalizeSpawn(MobSpawnEvent.FinalizeSpawn event) {
        var entity = event.getEntity();
        handleSanityInjuryResistance(entity);
        handleSeaBornSpawn(entity);
    }

    private static void handleSanityInjuryResistance(LivingEntity entity) {
        var attribute = entity.getAttribute(ModAttributes.SANITY_INJURY_RESISTANCE.get());
        if (attribute == null) return;
        if (entity.getType().is(ModTags.EntityTypes.SEA_BORN_BOSS)) {
            attribute.addPermanentModifier(new AttributeModifier(CaerulaArborMod.ATTRIBUTE_MODIFIER, 85, AttributeModifier.Operation.ADDITION));
        } else if (entity.getType().is(ModTags.EntityTypes.SEA_BORN)) {
            attribute.addPermanentModifier(new AttributeModifier(CaerulaArborMod.ATTRIBUTE_MODIFIER, 60, AttributeModifier.Operation.ADDITION));
        }
        if (entity instanceof IronGolem) {
            attribute.addPermanentModifier(new AttributeModifier(CaerulaArborMod.ATTRIBUTE_MODIFIER, 90, AttributeModifier.Operation.ADDITION));
        }
        if (entity instanceof Warden) {
            attribute.addPermanentModifier(new AttributeModifier(CaerulaArborMod.ATTRIBUTE_MODIFIER, 75, AttributeModifier.Operation.ADDITION));
        }
        if (entity.getMobType() == MobType.UNDEAD) {
            attribute.addPermanentModifier(new AttributeModifier(CaerulaArborMod.ATTRIBUTE_MODIFIER, 50, AttributeModifier.Operation.ADDITION));
        }
    }

    private static void handleSeaBornSpawn(LivingEntity entity) {
        if (!entity.getType().is(ModTags.EntityTypes.SEA_BORN)) return;
        var level = entity.level();
        double subsisting = CaerulaArborModVariables.MapVariables.get(level).strategySubsisting;
        double breed = CaerulaArborModVariables.MapVariables.get(level).strategyBreed;
        double grow = CaerulaArborModVariables.MapVariables.get(level).strategyGrow;

        var swimSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (swimSpeed != null) {
            swimSpeed.addPermanentModifier(new AttributeModifier(CaerulaArborMod.ATTRIBUTE_MODIFIER, 2, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        var maxHealth = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.addPermanentModifier(new AttributeModifier(CaerulaArborMod.ATTRIBUTE_MODIFIER, 0.3 * subsisting, AttributeModifier.Operation.MULTIPLY_BASE));
        }
        var armor = entity.getAttribute(Attributes.ARMOR);
        if (armor != null) {
            armor.addPermanentModifier(new AttributeModifier(CaerulaArborMod.ATTRIBUTE_MODIFIER, 2 * subsisting, AttributeModifier.Operation.ADDITION));
        }
        var armorToughness = entity.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (armorToughness != null) {
            armorToughness.addPermanentModifier(new AttributeModifier(CaerulaArborMod.ATTRIBUTE_MODIFIER, subsisting, AttributeModifier.Operation.ADDITION));
        }

        var attackDamage = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null) {
            attackDamage.addPermanentModifier(new AttributeModifier(CaerulaArborMod.ATTRIBUTE_MODIFIER, 0.25 * grow, AttributeModifier.Operation.MULTIPLY_BASE));
        }

        if (breed > 0 && !entity.getType().is(ModTags.EntityTypes.SEA_BORN_CREATURE)) {
            double random = Math.random();
            if (random < 0.05 + 0.05 * breed) {
                var copyEntity = entity.getType().create(level);
                if (copyEntity != null) {
                    copyEntity.setPos(entity.getX() + Mth.nextDouble(level.random, -1, 1), entity.getY(), entity.getZ() + Mth.nextDouble(level.random, -1, 1));
                    level.addFreshEntity(copyEntity);
                }
            }
            if (breed >= 3) {
                if (random < 0.05 * (breed - 2)) {
                    for (int index0 = 0; index0 < 2; index0++) {
                        var copyEntity = entity.getType().create(level);
                        if (copyEntity != null) {
                            copyEntity.setPos(entity.getX() + Mth.nextDouble(level.random, -1, 1), entity.getY(), entity.getZ() + Mth.nextDouble(level.random, -1, 1));
                            level.addFreshEntity(copyEntity);
                        }
                    }
                }
            }
        }
    }
}
