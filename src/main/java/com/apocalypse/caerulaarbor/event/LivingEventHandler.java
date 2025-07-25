package com.apocalypse.caerulaarbor.event;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.api.event.RelicEvent;
import com.apocalypse.caerulaarbor.block.SeaTrailBaseBlock;
import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.map.MapVariables;
import com.apocalypse.caerulaarbor.capability.sanity.ISanityInjuryCapability;
import com.apocalypse.caerulaarbor.init.ModAttributes;
import com.apocalypse.caerulaarbor.init.ModTags;
import com.apocalypse.caerulaarbor.item.relic.IRelic;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
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
        // 这个不能改成ModCapabilities.getSanityInjury，重生时候需要重新复制一遍cap
        event.getEntity().getCapability(ModCapabilities.SANITY_INJURY).ifPresent(ISanityInjuryCapability::tick);
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
        handleSeaBornSpawn(entity, event.getSpawnType());
    }

    /**
     * 在生物刷新时，提供一定的损伤减免
     */
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

    /**
     * 在海嗣刷新时，根据当前的策略，给予不同的属性加成
     */
    private static void handleSeaBornSpawn(LivingEntity entity, MobSpawnType type) {
        if (!entity.getType().is(ModTags.EntityTypes.SEA_BORN)) return;
        var level = entity.level();
        var mapVariables = MapVariables.get(level);
        int subsisting = mapVariables.strategySubsisting;
        int breed = mapVariables.strategyBreed;
        int grow = mapVariables.strategyGrow;

        var swimSpeed = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (swimSpeed != null) {
            swimSpeed.addPermanentModifier(new AttributeModifier(CaerulaArborMod.ATTRIBUTE_MODIFIER, 2, AttributeModifier.Operation.MULTIPLY_BASE));
        }

        // 策略-存续
        var maxHealth = entity.getAttribute(Attributes.MAX_HEALTH);
        if (maxHealth != null) {
            maxHealth.addPermanentModifier(new AttributeModifier(CaerulaArborMod.ATTRIBUTE_MODIFIER, 0.3 * subsisting, AttributeModifier.Operation.MULTIPLY_BASE));
            entity.setHealth(entity.getMaxHealth());
        }
        var armor = entity.getAttribute(Attributes.ARMOR);
        if (armor != null) {
            armor.addPermanentModifier(new AttributeModifier(CaerulaArborMod.ATTRIBUTE_MODIFIER, 2 * subsisting, AttributeModifier.Operation.ADDITION));
        }
        var armorToughness = entity.getAttribute(Attributes.ARMOR_TOUGHNESS);
        if (armorToughness != null) {
            armorToughness.addPermanentModifier(new AttributeModifier(CaerulaArborMod.ATTRIBUTE_MODIFIER, 2 * subsisting, AttributeModifier.Operation.ADDITION));
        }
        if (subsisting >= 3) {
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, -1, subsisting - 3, false, false));
        }

        // 策略-生长
        var attackDamage = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attackDamage != null) {
            attackDamage.addPermanentModifier(new AttributeModifier(CaerulaArborMod.ATTRIBUTE_MODIFIER, 0.25 * grow, AttributeModifier.Operation.MULTIPLY_BASE));
        }

        // 策略-繁育
        if (type != MobSpawnType.SPAWN_EGG && type != MobSpawnType.COMMAND && type != MobSpawnType.DISPENSER && type != MobSpawnType.BUCKET) {
            if (breed > 0 && !entity.getType().is(ModTags.EntityTypes.SEA_BORN_CREATURE) && !entity.getType().is(ModTags.EntityTypes.SEA_BORN_BOSS)) {
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
