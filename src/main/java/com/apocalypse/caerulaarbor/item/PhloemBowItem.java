package com.apocalypse.caerulaarbor.item;

import com.apocalypse.caerulaarbor.client.renderer.item.PhloemBowItemRenderer;
import com.apocalypse.caerulaarbor.init.ModEnchantments;
import com.apocalypse.caerulaarbor.init.ModItems;
import com.apocalypse.caerulaarbor.procedures.ShootarrowProcedure;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
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
import java.util.Map;
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
    public boolean hasCraftingRemainingItem() {
        return true;
    }

    @Override
    public ItemStack getCraftingRemainingItem(ItemStack itemstack) {
        ItemStack retval = new ItemStack(this);
        retval.setDamageValue(itemstack.getDamageValue() + 1);
        if (retval.getDamageValue() >= retval.getMaxDamage()) {
            return ItemStack.EMPTY;
        }
        return retval;
    }

    @Override
    public boolean isRepairable(@NotNull ItemStack itemstack) {
        return false;
    }

    @Override
    public int getEnchantmentValue() {
        return 16;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack itemstack) {
        return 72000;
    }

    @Override
    @ParametersAreNonnullByDefault
    public @NotNull InteractionResultHolder<ItemStack> use(Level world, Player entity, InteractionHand hand) {
        InteractionResultHolder<ItemStack> ar = super.use(world, entity, hand);
        ItemStack itemstack = ar.getObject();
        double x = entity.getX();
        double y = entity.getY();
        double z = entity.getZ();
        var blockPos = entity.blockPosition();

        boolean valid = true;
        if (entity.getMainHandItem().getItem() == itemstack.getItem()) {
            ItemStack offhandItem = entity.getOffhandItem();
            if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.POWER_ARROWS, offhandItem) != 0
                    && (offhandItem.getEnchantmentLevel(Enchantments.POWER_ARROWS) > itemstack.getEnchantmentLevel(ModEnchantments.REFLECTION.get()))) {
                {
                    Map<Enchantment, Integer> _enchantments = EnchantmentHelper.getEnchantments(itemstack);
                    if (_enchantments.containsKey(ModEnchantments.REFLECTION.get())) {
                        _enchantments.remove(ModEnchantments.REFLECTION.get());
                        EnchantmentHelper.setEnchantments(_enchantments, itemstack);
                    }
                }
                itemstack.enchant(ModEnchantments.REFLECTION.get(), offhandItem.getEnchantmentLevel(Enchantments.POWER_ARROWS));
                {
                    Map<Enchantment, Integer> _enchantments = EnchantmentHelper.getEnchantments(offhandItem);
                    if (_enchantments.containsKey(Enchantments.POWER_ARROWS)) {
                        _enchantments.remove(Enchantments.POWER_ARROWS);
                        EnchantmentHelper.setEnchantments(_enchantments, offhandItem);
                    }
                }
                if (world instanceof ServerLevel server) {
                    server.sendParticles(ParticleTypes.ENCHANT, x, y, z, 72, 1.2, 2, 1.2, 0.2);
                    server.playSound(null, blockPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 3, 1);
                } else {
                    world.playLocalSound(x, y, z, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 3, 1, false);
                }

                valid = false;
            } else if (EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, offhandItem) != 0
                    && EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.METABOLISM.get(), itemstack) == 0) {
                {
                    Map<Enchantment, Integer> _enchantments = EnchantmentHelper.getEnchantments(itemstack);
                    if (_enchantments.containsKey(ModEnchantments.METABOLISM.get())) {
                        _enchantments.remove(ModEnchantments.METABOLISM.get());
                        EnchantmentHelper.setEnchantments(_enchantments, itemstack);
                    }
                }
                itemstack.enchant(ModEnchantments.METABOLISM.get(), 1);
                {
                    Map<Enchantment, Integer> _enchantments = EnchantmentHelper.getEnchantments(offhandItem);
                    if (_enchantments.containsKey(Enchantments.INFINITY_ARROWS)) {
                        _enchantments.remove(Enchantments.INFINITY_ARROWS);
                        EnchantmentHelper.setEnchantments(_enchantments, offhandItem);
                    }
                }
                if ((LevelAccessor) world instanceof ServerLevel _level)
                    _level.sendParticles(ParticleTypes.ENCHANT, x, y, z, 72, 1.2, 2, 1.2, 0.2);
                if ((LevelAccessor) world instanceof Level _level) {
                    if (!_level.isClientSide()) {
                        _level.playSound(null, blockPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 3, 1);
                    } else {
                        _level.playLocalSound(x, y, z, SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 3, 1, false);
                    }
                }
                valid = false;
            }
        }
        if (valid) {
            if (!((Entity) entity instanceof Player _plrCldCheck32 && _plrCldCheck32.getCooldowns().isOnCooldown(itemstack.getItem()))) {
                if (((Entity) entity instanceof Player _playerHasItem && _playerHasItem.getInventory().contains(new ItemStack(ModItems.OCEAN_ARROW.get()))) || entity.isCreative()) {
                    ShootarrowProcedure.execute(world, x, y, z, entity, itemstack);
                    if (itemstack.getItem() instanceof PhloemBowItem)
                        itemstack.getOrCreateTag().putString("geckoAnim", "animation.bluebow.pull");


                    if (!world.isClientSide()) {
                        world.playSound(null, blockPos, SoundEvents.CROSSBOW_QUICK_CHARGE_1, SoundSource.NEUTRAL, 1.8F, 1);
                    } else {
                        world.playLocalSound(x, y, z, SoundEvents.CROSSBOW_QUICK_CHARGE_1, SoundSource.NEUTRAL, 1.8F, 1, false);
                    }

                    entity.getCooldowns().addCooldown(itemstack.getItem(), 30);
                }
            }
        }
        return ar;
    }
}
