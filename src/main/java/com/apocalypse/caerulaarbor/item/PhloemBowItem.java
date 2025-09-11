package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.client.renderer.item.PhloemBowItemRenderer;
import com.apocalypse.caerulaarbor.init.ModEnchantments;
import com.apocalypse.caerulaarbor.init.ModItems;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
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

public class PhloemBowItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String animationprocedure = "empty";

    public PhloemBowItem() {
        super(new Item.Properties().durability(768).rarity(Rarity.COMMON));
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public void initializeClient(@NotNull Consumer<IClientItemExtensions> consumer) {
        super.initializeClient(consumer);
        consumer.accept(new IClientItemExtensions() {
            private final BlockEntityWithoutLevelRenderer renderer = new PhloemBowItemRenderer();

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                return renderer;
            }
        });
    }

    private PlayState idlePredicate(AnimationState<?> event) {
        if (this.animationprocedure.equals("empty")) {
            event.getController().setAnimation(RawAnimation.begin().thenLoop("animation.bluebow.idle"));
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
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack itemstack) {
        return UseAnim.BOW;
    }

    @Override
    public boolean hasCraftingRemainingItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemstack) {
        ItemStack stack = new ItemStack(this);
        stack.setDamageValue(itemstack.getDamageValue() + 1);
        if (stack.getDamageValue() >= stack.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return stack;
    }

    @Override
    public boolean isRepairable(@NotNull ItemStack itemstack) {
        return false;
    }

    @Override
    public int getEnchantmentValue(ItemStack stack) {
        return 16;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemstack) {
        return 72000;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, player, hand);
        ItemStack stack = ar.getObject();
        double x = player.getX();
        double y = player.getY();
        double z = player.getZ();
        var blockPos = player.blockPosition();

        boolean valid = true;

        if (player.getMainHandItem().getItem() == stack.getItem()) {
            ItemStack offhandItem = player.getOffhandItem();

            if (offhandItem.getEnchantmentLevel(Enchantments.POWER_ARROWS) > stack.getEnchantmentLevel(ModEnchantments.REFLECTION.get())) {
                var enchantments = stack.getAllEnchantments();
                enchantments.put(ModEnchantments.REFLECTION.get(), enchantments.get(Enchantments.POWER_ARROWS));
                enchantments.remove(Enchantments.POWER_ARROWS);
                EnchantmentHelper.setEnchantments(enchantments, stack);

                if (world instanceof ServerLevel server) {
                    server.sendParticles(ParticleTypes.ENCHANT, x, y, z, 72, 1.2, 2, 1.2, 0.2);
                    server.playSound(null, blockPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 3, 1);
                } else {
                    world.playLocalSound(x, y, z, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 3, 1, false);
                }

                valid = false;
            } else if (offhandItem.getEnchantmentLevel(Enchantments.INFINITY_ARROWS) != 0
                    && stack.getEnchantmentLevel(ModEnchantments.METABOLISM.get()) == 0
            ) {
                var enchantments = stack.getAllEnchantments();
                enchantments.put(ModEnchantments.METABOLISM.get(), 1);
                enchantments.remove(Enchantments.INFINITY_ARROWS);
                EnchantmentHelper.setEnchantments(enchantments, stack);

                if (world instanceof ServerLevel server) {
                    server.sendParticles(ParticleTypes.ENCHANT, x, y, z, 72, 1.2, 2, 1.2, 0.2);
                    server.playSound(null, blockPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 3, 1);
                } else {
                    world.playLocalSound(x, y, z, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 3, 1, false);
                }
                valid = false;
            }
        }

        if (!valid || player.getCooldowns().isOnCooldown(stack.getItem())) return ar;

        if (player.getInventory().contains(new ItemStack(ModItems.OCEAN_ARROW.get())) || player.isCreative()) {
            CaerulaArborMod.queueServerWork(24, () -> {
                if ((player.getInventory().contains(new ItemStack(ModItems.OCEAN_ARROW.get()))
                        || player.isCreative()
                        || stack.getEnchantmentLevel(ModEnchantments.METABOLISM.get()) != 0)
                        && (player.getMainHandItem().getItem() == stack.getItem()
                        || player.getOffhandItem().getItem() == stack.getItem())
                ) {
                    if (!world.isClientSide()) {
                        world.playSound(null, blockPos, SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.8F, 1);
                    } else {
                        world.playLocalSound(x, y, z, SoundEvents.ARROW_SHOOT, SoundSource.PLAYERS, 1.8F, 1, false);
                    }

                    if (stack.hurt(1, RandomSource.create(), null)) {
                        stack.shrink(1);
                        stack.setDamageValue(0);
                    }

                    float damage = 7 + 1.5f * stack.getEnchantmentLevel(ModEnchantments.REFLECTION.get());
                    float velocity = 3 + 0.2f * stack.getEnchantmentLevel(ModEnchantments.REFLECTION.get());

                    boolean creativeOrMetabolism = player.isCreative() || stack.getEnchantmentLevel(ModEnchantments.METABOLISM.get()) != 0;

                    if (!creativeOrMetabolism) {
                        player.getInventory().clearOrCountMatchingItems(p -> ModItems.OCEAN_ARROW.get() == p.getItem(), 1, player.inventoryMenu.getCraftSlots());
                    }

                    var arrow = new Arrow(EntityType.ARROW, player.level());
                    arrow.setOwner(player);
                    arrow.setBaseDamage(damage);
                    arrow.setPierceLevel((byte) 1);
                    arrow.setCritArrow(true);
                    arrow.pickup = creativeOrMetabolism ? AbstractArrow.Pickup.CREATIVE_ONLY : AbstractArrow.Pickup.ALLOWED;

                    arrow.setPos(player.getX(), player.getEyeY() - 0.1, player.getZ());
                    arrow.shoot(player.getLookAngle().x, player.getLookAngle().y, player.getLookAngle().z, velocity, 0);
                    player.level().addFreshEntity(arrow);
                }
            });

            if (stack.getItem() instanceof PhloemBowItem) {
                stack.getOrCreateTag().putString("geckoAnim", "animation.bluebow.pull");
            }

            if (!world.isClientSide()) {
                world.playSound(null, blockPos, SoundEvents.CROSSBOW_QUICK_CHARGE_1, SoundSource.NEUTRAL, 1.8F, 1);
            } else {
                world.playLocalSound(x, y, z, SoundEvents.CROSSBOW_QUICK_CHARGE_1, SoundSource.NEUTRAL, 1.8F, 1, false);
            }

            player.getCooldowns().addCooldown(stack.getItem(), 30);
        }
        return ar;
    }
}
