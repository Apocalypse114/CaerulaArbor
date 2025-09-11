
package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.client.renderer.item.LegendarySpearItemRenderer;
import com.apocalypse.caerulaarbor.init.ModEnchantments;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Consumer;

public class LegendarySpearItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationprocedure = "empty";

    public LegendarySpearItem() {
        super(new Item.Properties().durability(7445).fireResistant().rarity(Rarity.UNCOMMON));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new LegendarySpearItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    private PlayState idlePredicate(AnimationState<?> event) {
        if (this.animationprocedure.equals("empty")) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.lengendspear.idle"));
            return PlayState.CONTINUE;
        }
        return PlayState.STOP;
    }

    String prevAnim = "empty";

    private PlayState procedurePredicate(AnimationState<?> event) {
        if (!this.animationprocedure.equals("empty") && event.getController().getAnimationState() == AnimationController.State.STOPPED || (!this.animationprocedure.equals(prevAnim) && !this.animationprocedure.equals("empty"))) {
            if (!this.animationprocedure.equals(prevAnim))
                event.getController().forceAnimationReset();
            event.getController().setAnimation(RawAnimation.begin().thenPlay(this.animationprocedure));
            if (event.getController().getAnimationState() == AnimationController.State.STOPPED) {
                this.animationprocedure = "empty";
                event.getController().forceAnimationReset();
            }
        } else if (this.animationprocedure.equals("empty")) {
            prevAnim = "empty";
            return PlayState.STOP;
        }
        prevAnim = this.animationprocedure;
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar data) {
        var procedureController = new AnimationController<>(this, "procedureController", 0, this::procedurePredicate);
        data.add(procedureController);
        var idleController = new AnimationController<>(this, "idleController", 0, this::idlePredicate);
        data.add(idleController);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 22;
    }

    @Override
    @ParametersAreNonnullByDefault
    public float getDestroySpeed(ItemStack par1ItemStack, BlockState par2Block) {
        return 1.5F;
    }

    @Override
    public @NotNull Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(@NotNull EquipmentSlot equipmentSlot) {
        if (equipmentSlot == EquipmentSlot.MAINHAND) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
            builder.putAll(super.getDefaultAttributeModifiers(equipmentSlot));
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Item modifier", 13d, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Item modifier", -2.4, AttributeModifier.Operation.ADDITION));
            return builder.build();
        }
        return super.getDefaultAttributeModifiers(equipmentSlot);
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        var ar = super.use(world, entity, hand);
        ItemStack stack = ar.getObject();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();

        if (entity.getMainHandItem().getItem() != stack.getItem()) return ar;

        ItemStack offhandItem = entity.getOffhandItem();

        if (offhandItem.getEnchantmentLevel(Enchantments.SHARPNESS) > stack.getEnchantmentLevel(ModEnchantments.SYNESTHESIA.get())) {

            var enchantments = stack.getAllEnchantments();
            enchantments.put(ModEnchantments.SYNESTHESIA.get(), enchantments.get(Enchantments.SHARPNESS));
            enchantments.remove(Enchantments.SHARPNESS);
            EnchantmentHelper.setEnchantments(enchantments, stack);

            if (world instanceof ServerLevel server) {
                server.sendParticles(ParticleTypes.ENCHANT, x, y, z, 72, 1.2, 2, 1.2, 0.2);
                server.playSound(null, BlockPos.containing(x, y, z), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 3, 1);
            } else {
                world.playLocalSound(x, y, z, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 3, 1, false);

            }
        }

        return ar;
    }

    @Override
    @ParametersAreNonnullByDefault
    public boolean hurtEnemy(ItemStack stack, LivingEntity entity, LivingEntity source) {
        var world = entity.level();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        var pos = entity.blockPosition();

        if (stack.hurt(1, RandomSource.create(), null)) {
            stack.shrink(1);
            stack.setDamageValue(0);
        }

        if (!(source instanceof Player player)
                || player.getCooldowns().isOnCooldown(stack.getItem())
                || player.getMainHandItem().getItem() != stack.getItem()
        ) return super.hurtEnemy(stack, entity, source);

        if (player.isShiftKeyDown()) {
            if (stack.getItem() instanceof LegendarySpearItem) {
                stack.getOrCreateTag().putString("geckoAnim", "animation.lengendspear.swing2");
            }

            player.getCooldowns().addCooldown(stack.getItem(), 25);
            CaerulaArborMod.queueServerWork(10, () -> {
                if (!world.isClientSide()) {
                    world.playSound(null, pos, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.NEUTRAL, 3.5f, 1);
                } else {
                    world.playLocalSound(x, y, z, SoundEvents.PLAYER_ATTACK_SWEEP, SoundSource.NEUTRAL, 3.5f, 1, false);
                }

                final Vec3 _center = new Vec3(x, y, z);
                for (var entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(3.5), e -> true)) {
                    if (entityiterator.isAlive() && entityiterator != source && entityiterator.distanceTo(source) <= 3) {
                        entityiterator.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.TRIDENT), source),
                                (float) ((player.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE)
                                        ? player.getAttribute(Attributes.ATTACK_DAMAGE).getValue()
                                        : 0) * (1 + 0.2 * stack.getEnchantmentLevel(ModEnchantments.SYNESTHESIA.get()))));
                    }
                }

            });
        } else if (source.getDeltaMovement().y() < -0.1) {
            if (stack.getItem() instanceof LegendarySpearItem) {
                stack.getOrCreateTag().putString("geckoAnim", "animation.lengendspear.srike");
            }
            player.getCooldowns().addCooldown(stack.getItem(), 25);

            CaerulaArborMod.queueServerWork(10, () -> {
                if (entity.isAlive()) {
                    if (!world.isClientSide()) {
                        world.playSound(null, pos, SoundEvents.TRIDENT_HIT_GROUND, SoundSource.NEUTRAL, 3.5f, 1);
                    } else {
                        world.playLocalSound(x, y, z, SoundEvents.TRIDENT_HIT_GROUND, SoundSource.NEUTRAL, 3.5f, 1, false);
                    }

                    if (entity.distanceTo(source) <= 4) {
                        entity.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.TRIDENT), source),
                                (float) ((source.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? source.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0)
                                        * (1 + 0.2 * stack.getEnchantmentLevel(ModEnchantments.SYNESTHESIA.get()))));
                    }
                }
            });
        } else if (source.getDeltaMovement().y() > 0.1) {
            if (stack.getItem() instanceof LegendarySpearItem) {
                stack.getOrCreateTag().putString("geckoAnim", "animation.lengendspear.swing");
            }
            player.getCooldowns().addCooldown(stack.getItem(), 25);

            CaerulaArborMod.queueServerWork(10, () -> {
                if (player.isAlive()) {
                    if (!world.isClientSide()) {
                        world.playSound(null, pos, SoundEvents.TRIDENT_THROW, SoundSource.NEUTRAL, 3.5f, 1);
                    } else {
                        world.playLocalSound(x, y, z, SoundEvents.TRIDENT_THROW, SoundSource.NEUTRAL, 3.5f, 1, false);
                    }

                    if (entity.distanceTo(source) <= 4) {
                        player.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.TRIDENT), source),
                                (float) ((player.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? player.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0)
                                        * (1 + 0.2 * stack.getEnchantmentLevel(ModEnchantments.SYNESTHESIA.get()))));
                        entity.push(0, 0.5, 0);
                    }
                }
            });
        } else {
            if (stack.getItem() instanceof LegendarySpearItem)
                stack.getOrCreateTag().putString("geckoAnim", "animation.lengendspear.stab");
            player.getCooldowns().addCooldown(stack.getItem(), 25);

            CaerulaArborMod.queueServerWork(10, () -> {
                if (player.isAlive()) {
                    if (!world.isClientSide()) {
                        world.playSound(null, pos, SoundEvents.TRIDENT_HIT, SoundSource.NEUTRAL, 3.5f, 1);
                    } else {
                        world.playLocalSound(x, y, z, SoundEvents.TRIDENT_HIT, SoundSource.NEUTRAL, 3.5f, 1, false);
                    }
                    if (entity.distanceTo(source) <= 4) {
                        player.hurt(new DamageSource(world.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(DamageTypes.TRIDENT), source),
                                (float) ((player.getAttributes().hasAttribute(Attributes.ATTACK_DAMAGE) ? player.getAttribute(Attributes.ATTACK_DAMAGE).getValue() : 0)
                                        * (1 + 0.2 * stack.getEnchantmentLevel(ModEnchantments.SYNESTHESIA.get()))));
                        entity.push((source.getLookAngle().x), 0, (source.getLookAngle().z));
                    }
                }
            });
        }

        return super.hurtEnemy(stack, entity, source);
    }

    @Override
    @ParametersAreNonnullByDefault
    public void inventoryTick(ItemStack itemstack, Level world, Entity entity, int slot, boolean selected) {
        super.inventoryTick(itemstack, world, entity, slot, selected);

        if (!selected || !(entity instanceof LivingEntity living) || !living.hasEffect(ModMobEffects.SPEAR_FIGHT.get()))
            return;

        if (!living.level().isClientSide) {
            living.addEffect(new MobEffectInstance(ModMobEffects.SPEAR_FIGHT.get(), 60, 0, false, false));
        }
    }
}
