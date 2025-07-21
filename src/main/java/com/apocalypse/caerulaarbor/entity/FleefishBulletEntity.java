
package com.apocalypse.caerulaarbor.entity;

import com.apocalypse.caerulaarbor.init.ModEntities;
import com.apocalypse.caerulaarbor.init.ModMobEffects;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;

@OnlyIn(value = Dist.CLIENT, _interface = ItemSupplier.class)
public class FleefishBulletEntity extends AbstractArrow implements ItemSupplier {
    public static final ItemStack PROJECTILE_ITEM = new ItemStack(Items.MAGMA_CREAM);

    public FleefishBulletEntity(PlayMessages.SpawnEntity packet, Level world) {
        super(ModEntities.FLEEFISH_BULLET.get(), world);
    }

    public FleefishBulletEntity(EntityType<? extends FleefishBulletEntity> type, Level world) {
        super(type, world);
    }

    public FleefishBulletEntity(EntityType<? extends FleefishBulletEntity> type, double x, double y, double z, Level world) {
        super(type, x, y, z, world);
    }

    public FleefishBulletEntity(EntityType<? extends FleefishBulletEntity> type, LivingEntity entity, Level world) {
        super(type, entity, world);
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public @NotNull ItemStack getItem() {
        return PROJECTILE_ITEM;
    }

    @Override
    protected @NotNull ItemStack getPickupItem() {
        return PROJECTILE_ITEM;
    }

    @Override
    protected void doPostHurtEffects(@NotNull LivingEntity entity) {
        super.doPostHurtEffects(entity);
        entity.setArrowCount(entity.getArrowCount() - 1);
    }

    @Override
    public void onHitEntity(@NotNull EntityHitResult entityHitResult) {
        super.onHitEntity(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        Entity sourceentity = this.getOwner();
        if (sourceentity == null) return;

        if (entity != sourceentity && entity instanceof LivingEntity living && !living.level().isClientSide) {
            living.addEffect(new MobEffectInstance(ModMobEffects.ARMOR_BREAKING.get(), 300, 3), this);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.inGround)
            this.discard();
    }

    public static FleefishBulletEntity shoot(Level world, LivingEntity entity, RandomSource source) {
        return shoot(world, entity, source, 1.2f, 4.5, 1);
    }

    public static FleefishBulletEntity shoot(Level world, LivingEntity entity, RandomSource source, float pullingPower) {
        return shoot(world, entity, source, pullingPower * 1.2f, 4.5, 1);
    }

    public static FleefishBulletEntity shoot(Level world, LivingEntity entity, RandomSource random, float power, double damage, int knockback) {
        FleefishBulletEntity entityarrow = new FleefishBulletEntity(ModEntities.FLEEFISH_BULLET.get(), entity, world);
        entityarrow.shoot(entity.getViewVector(1).x, entity.getViewVector(1).y, entity.getViewVector(1).z, power * 2, 0);
        entityarrow.setSilent(true);
        entityarrow.setCritArrow(false);
        entityarrow.setBaseDamage(damage);
        entityarrow.setKnockback(knockback);
        world.addFreshEntity(entityarrow);
        world.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SQUID_SQUIRT, SoundSource.PLAYERS, 1, 1f / (random.nextFloat() * 0.5f + 1) + (power / 2));
        return entityarrow;
    }

    public static FleefishBulletEntity shoot(LivingEntity entity, LivingEntity target) {
        FleefishBulletEntity entityarrow = new FleefishBulletEntity(ModEntities.FLEEFISH_BULLET.get(), entity, entity.level());
        double dx = target.getX() - entity.getX();
        double dy = target.getY() + target.getEyeHeight() - 1.1;
        double dz = target.getZ() - entity.getZ();
        entityarrow.shoot(dx, dy - entityarrow.getY() + Math.hypot(dx, dz) * 0.2F, dz, 1.2f * 2, 12.0F);
        entityarrow.setSilent(true);
        entityarrow.setBaseDamage(4.5);
        entityarrow.setKnockback(1);
        entityarrow.setCritArrow(false);
        entity.level().addFreshEntity(entityarrow);
        entity.level().playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.SQUID_SQUIRT, SoundSource.PLAYERS, 1, 1f / (RandomSource.create().nextFloat() * 0.5f + 1));
        return entityarrow;
    }
}
