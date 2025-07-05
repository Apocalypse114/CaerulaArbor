package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.apocalypse.caerulaarbor.config.common.RelicsConfig;
import com.apocalypse.caerulaarbor.init.ModAttributes;
import com.apocalypse.caerulaarbor.init.ModEnchantments;
import com.apocalypse.caerulaarbor.init.ModItems;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.Comparator;

@Mod.EventBusSubscriber
public class PlayerTickFuncProcedure {
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            execute(event, event.player.level(), event.player.getX(), event.player.getY(), event.player.getZ(), event.player);
        }
    }

    public static void execute(LevelAccessor world, double x, double y, double z, Entity entity) {
        execute(null, world, x, y, z, entity);
    }

    private static void execute(@Nullable Event event, LevelAccessor world, double x, double y, double z, Entity entity) {
        if (entity == null)
            return;
        boolean valid;
        double modifi;
        double suitKing;
        double amplifi;
        double suitArchfi;
        double enchant;
        var cap = entity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());

        if (cap.disoclusion == 2) {
            if (!(entity instanceof LivingEntity _livEnt0 && _livEnt0.hasEffect(ModMobEffects.HAEMOPHILIA.get()))) {
                if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                    _entity.addEffect(new MobEffectInstance(ModMobEffects.HAEMOPHILIA.get(), 299, 0, false, false));
            }
        } else {
            if (entity instanceof LivingEntity _livEnt2 && _livEnt2.hasEffect(ModMobEffects.HAEMOPHILIA.get())) {
                if (entity instanceof LivingEntity _entity)
                    _entity.removeEffect(ModMobEffects.HAEMOPHILIA.get());
            }
        }
        if (cap.disoclusion == 4) {
            if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ModMobEffects.FLESHDEFORMITY.get()) ? _livEnt.getEffect(ModMobEffects.FLESHDEFORMITY.get()).getDuration() : 0) < 5) {
                if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                    _entity.addEffect(new MobEffectInstance(ModMobEffects.FLESHDEFORMITY.get(), 200, 1, false, false));
            }
        } else {
            if (entity instanceof LivingEntity _livEnt6 && _livEnt6.hasEffect(ModMobEffects.FLESHDEFORMITY.get())) {
                if (entity instanceof LivingEntity _entity)
                    _entity.removeEffect(ModMobEffects.HAEMOPHILIA.get());
            }
        }
        modifi = 1;
        if (entity.getCapability(ModCapabilities.PLAYER_VARIABLE)
                .map(c -> c.light >= 1 && c.light < 50)
                .orElse(false)) {
            modifi = 1.2;
        } else if (entity.getCapability(ModCapabilities.PLAYER_VARIABLE)
                .map(c -> c.light < 1)
                .orElse(false)) {
            modifi = 1.5;
        }
        if (cap.player_oceanization >= 3) {
            modifi = 0.33;
        }

        if (entity instanceof LivingEntity _livingEntity8 && _livingEntity8.getAttributes().hasAttribute(ModAttributes.SANITY_INJURY_RESISTANCE.get()))
            _livingEntity8.getAttribute(ModAttributes.SANITY_INJURY_RESISTANCE.get()).setBaseValue(modifi);
        if (cap.lives <= 1) {
            suitKing = 0;
            if (Relic.KING_SPEAR.gained(cap)) {
                suitKing = suitKing + 1;
                if (!(entity instanceof LivingEntity _livEnt9 && _livEnt9.hasEffect(ModMobEffects.KINGS_BOOST.get()))) {
                    if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                        _entity.addEffect(new MobEffectInstance(ModMobEffects.KINGS_BOOST.get(), 100, 1, false, false));
                }
            }
            if (Relic.KING_ARMOR.gained(cap)) {
                suitKing = suitKing + 1;
            }
            if (Relic.KING_EXTENSION.gained(cap)) {
                suitKing = suitKing + 1;
                amplifi = Math.ceil((entity instanceof LivingEntity _livEnt ? _livEnt.getMaxHealth() : -1) / 20);
                if (amplifi > 24) {
                    amplifi = 24;
                }
                if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.REGENERATION) ? _livEnt.getEffect(MobEffects.REGENERATION).getAmplifier() : 0) < amplifi) {
                    if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                        _entity.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 100, (int) amplifi, false, false));
                }
            }
            if (Relic.KING_CROWN.gained(cap)) {
                suitKing = suitKing + 1;
                if (!(entity instanceof LivingEntity _livEnt14 && _livEnt14.hasEffect(ModMobEffects.KINGS_BREATH.get()))) {
                    if (suitKing < 3) {
                        if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                            _entity.addEffect(new MobEffectInstance(ModMobEffects.KINGS_BREATH.get(), 30, 0, false, false));
                        {
                            double _setval = 1;
                            entity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
                                capability.player_king_suit = _setval;
                                capability.syncPlayerVariables(entity);
                            });
                        }
                    } else {
                        if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                            _entity.addEffect(new MobEffectInstance(ModMobEffects.KINGS_BREATH.get(), 3, 2, false, false));
                        entity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
                            capability.player_king_suit = 2;
                            capability.syncPlayerVariables(entity);
                        });
                    }
                }
            } else {
                entity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
                    capability.player_king_suit = 0;
                    capability.syncPlayerVariables(entity);
                });
            }
        }
        if (Relic.HAND_SPEED.gained(cap)
                && (entity instanceof LivingEntity _livEnt ? _livEnt.getMainHandItem() : ItemStack.EMPTY).is(ItemTags.create(new ResourceLocation("minecraft:pickaxes")))) {
            valid = true;
            {
                final Vec3 _center = new Vec3(x, y, z);
                for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(8 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
                    if (entityiterator == entity) {
                        continue;
                    }
                    if (entityiterator instanceof Player || entityiterator instanceof Animal) {
                        valid = false;
                        break;
                    }
                }
            }
            if (valid) {
                if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(MobEffects.DIG_SPEED) ? _livEnt.getEffect(MobEffects.DIG_SPEED).getAmplifier() : 0) < 2) {
                    if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                        _entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 20, 2));
                }
                if (!(entity instanceof LivingEntity _livEnt26 && _livEnt26.hasEffect(ModMobEffects.HANDS_SPEED.get()))) {
                    if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                        _entity.addEffect(new MobEffectInstance(ModMobEffects.HANDS_SPEED.get(), 20, 2));
                }
            }
        }
        if (Relic.HAND_SWIPE.gained(cap)) {
            if (!(entity instanceof LivingEntity _livEnt28 && _livEnt28.hasEffect(ModMobEffects.WIPE_DUSTS.get()))
                    && (entity instanceof Player _playerHasItem && _playerHasItem.getInventory().contains(new ItemStack(Items.BRUSH)))) {
                if (!_playerHasItem.level().isClientSide())
                    _playerHasItem.addEffect(new MobEffectInstance(ModMobEffects.WIPE_DUSTS.get(), 100, 0, false, false));
            }
        }
        if (cap.lives >= cap.maxLive) {
            suitArchfi = 0;
            if (Relic.SARKAZ_KING_FLAG.gained(cap)) {
                suitArchfi = suitArchfi + 1;
                if (!(entity instanceof LivingEntity _livEnt31 && _livEnt31.hasEffect(ModMobEffects.FLAG_SWINGS.get()))) {
                    if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                        _entity.addEffect(new MobEffectInstance(ModMobEffects.FLAG_SWINGS.get(), 30, 2, false, false));
                }
            }
            if (Relic.SARKAZ_KING_BED.gained(cap)) {
                suitArchfi = suitArchfi + 1;
                if (!(entity instanceof LivingEntity _livEnt33 && _livEnt33.hasEffect(ModMobEffects.KEEP_BEDDING.get()))) {
                    if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                        _entity.addEffect(new MobEffectInstance(ModMobEffects.KEEP_BEDDING.get(), 30, 0, false, false));
                }
            }
            if (Relic.SARKAZ_KING_ARTIFACT.gained(cap)) {
                suitArchfi = suitArchfi + 1;
                if ((entity instanceof LivingEntity _livEnt && _livEnt.hasEffect(ModMobEffects.SACREFICE.get()) ? _livEnt.getEffect(ModMobEffects.SACREFICE.get()).getDuration() : 0) < 5) {
                    if (suitArchfi < 3) {
                        if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                            _entity.addEffect(new MobEffectInstance(ModMobEffects.SACREFICE.get(), 30, 0, false, false));

                        entity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
                            capability.player_demon_suit = 1;
                            capability.syncPlayerVariables(entity);
                        });
                    } else {
                        if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                            _entity.addEffect(new MobEffectInstance(ModMobEffects.SACREFICE.get(), 30, 2, false, false));

                        entity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
                            capability.player_demon_suit = 2;
                            capability.syncPlayerVariables(entity);
                        });
                    }
                }
            } else {
                entity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
                    capability.player_demon_suit = 0;
                    capability.syncPlayerVariables(entity);
                });
            }
        }
        if (Relic.HAND_ENGRAVE.get(cap) > 0) {
            valid = false;
            if (entity instanceof Player _playerHasItem && _playerHasItem.getInventory().contains(new ItemStack(Items.TRIDENT))) {
                valid = true;
            } else {
                for (String stringiterator : RelicsConfig.HAND_ENGRAVE.get()) {
                    if ((entity instanceof Player _playerHasItem && _playerHasItem.getInventory().contains(new ItemStack(ForgeRegistries.ITEMS.getValue(new ResourceLocation((stringiterator).toLowerCase(java.util.Locale.ENGLISH))))))
                            && !(ForgeRegistries.ITEMS.getValue(new ResourceLocation((stringiterator).toLowerCase(java.util.Locale.ENGLISH))) == ItemStack.EMPTY.getItem()
                            || ForgeRegistries.ITEMS.getValue(new ResourceLocation((stringiterator).toLowerCase(java.util.Locale.ENGLISH))) == Blocks.AIR.asItem())) {
                        valid = true;
                    }
                }
            }
            if (valid) {
                LivingEntity _entity = (LivingEntity) entity;
                if (!_entity.level().isClientSide()) {
                    _entity.addEffect(new MobEffectInstance(ModMobEffects.ENGRAVED_TRIUMPH.get(), 40,
                            Relic.HAND_ENGRAVE.get(cap) - 1, false, false));
                }
            }
        }
        if (Relic.SURVIVOR_CONTRACT.get(cap) > 0) {
            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide()) {
                _entity.addEffect(new MobEffectInstance(ModMobEffects.SURVIVORS_GUIDE.get(), 40,
                        Relic.SURVIVOR_CONTRACT.get(cap) - 1, false, false));
            }
        }
        if (cap.lives > cap.maxLive) {
            entity.getCapability(ModCapabilities.PLAYER_VARIABLE).ifPresent(capability -> {
                capability.lives = cap.maxLive;
                capability.syncPlayerVariables(entity);
            });
        }

        enchant = 0;
        if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SANITY_DEFEND.get(), (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY)) != 0) {
            enchant = enchant + (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.FEET) : ItemStack.EMPTY).getEnchantmentLevel(ModEnchantments.SANITY_DEFEND.get());
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SANITY_DEFEND.get(), (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY)) != 0) {
            enchant = enchant + (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.LEGS) : ItemStack.EMPTY).getEnchantmentLevel(ModEnchantments.SANITY_DEFEND.get());
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SANITY_DEFEND.get(), (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY)) != 0) {
            enchant = enchant + (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.CHEST) : ItemStack.EMPTY).getEnchantmentLevel(ModEnchantments.SANITY_DEFEND.get());
        }
        if (EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.SANITY_DEFEND.get(), (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY)) != 0) {
            enchant = enchant + (entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getEnchantmentLevel(ModEnchantments.SANITY_DEFEND.get());
        }
        if (enchant > 16) {
            enchant = 16;
        }
        if (enchant > 0 && !(entity instanceof LivingEntity _livEnt70 && _livEnt70.hasEffect(ModMobEffects.SANIDY_DEFENDER.get()))) {
            if (entity instanceof LivingEntity _entity && !_entity.level().isClientSide())
                _entity.addEffect(new MobEffectInstance(ModMobEffects.SANIDY_DEFENDER.get(), 20, (int) (enchant - 1), false, false));
        }
        if ((entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY).getItem() == ModItems.WEARABLE_CROWN_HELMET.get()) {
            ItemStack itemstack = entity instanceof LivingEntity _entGetArmor ? _entGetArmor.getItemBySlot(EquipmentSlot.HEAD) : ItemStack.EMPTY;
            if (entity == null)
                return;
            var cap1 = entity.getCapability(ModCapabilities.PLAYER_VARIABLE).orElse(new PlayerVariable());
            if (!Relic.KING_CROWN.gained(cap1)) {
                if (world instanceof Level _level) {
                    if (!_level.isClientSide()) {
                        _level.playSound(null, BlockPos.containing(x, y, z), SoundEvents.TOTEM_USE, SoundSource.NEUTRAL, 2, 1);
                        ((ServerLevel) _level).sendParticles(ParticleTypes.ENCHANTED_HIT, x, y, z, 72, 1, 1, 1, 1);
                    } else {
                        _level.playLocalSound(x, y, z, SoundEvents.TOTEM_USE, SoundSource.NEUTRAL, 2, 1, false);
                        Minecraft.getInstance().gameRenderer.displayItemActivation(itemstack);
                    }
                }
                Relic.KING_CROWN.gain(cap1);
                cap1.syncPlayerVariables(entity);
            }
        }
    }
}
