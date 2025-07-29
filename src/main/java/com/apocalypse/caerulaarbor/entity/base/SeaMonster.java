package com.apocalypse.caerulaarbor.entity.base;

import com.apocalypse.caerulaarbor.capability.map.MapVariables;
import com.apocalypse.caerulaarbor.capability.map.MapVariablesHandler;
import com.apocalypse.caerulaarbor.client.font.ModFontHelper;
import com.apocalypse.caerulaarbor.config.server.MiscConfig;
import com.apocalypse.caerulaarbor.init.ModGameRules;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class SeaMonster extends Monster implements GeoEntity {

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected boolean swinging;
    protected long lastSwing;

    protected SeaMonster(EntityType<? extends Monster> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean hurt(DamageSource source, float amount) {
        boolean flag = super.hurt(source, amount);
        if (source.is(DamageTypes.DROWN)) {
            return false;
        }
        if (this.level().getLevelData().getGameRules().getBoolean(ModGameRules.NATURAL_EVOLUTION) && source.getEntity() != null) {
            MapVariablesHandler.addEvoPoint(MapVariables.StrategyType.SUBSISTING, this.level(), Math.min(amount * 0.025, this.getMaxHealth()));
            MapVariablesHandler.addEvoPoint(MapVariables.StrategyType.SILENCE, this.level(), Math.min(amount * 0.025, this.getMaxHealth()));
        }
        return flag;
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    /**
     * 策略-生长到达3级及以上时，攻击时额外造成魔法伤害
     */
    @Override
    public boolean doHurtTarget(@NotNull Entity pEntity) {
        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);
        var level = this.level();
        double grow = MapVariables.get(level).strategyGrow;
        if (grow >= 3) {
            boolean flag = pEntity.hurt(level.damageSources().indirectMagic(this, this), (float) (damage * 0.2 * (grow - 2)));
            if (flag) {
                pEntity.invulnerableTime = 0;
            }
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.ENCHANTED_HIT, pEntity.getX(), pEntity.getY() + 1, pEntity.getZ(), 48, 1.5, 1.5, 1.5, 0.2);
            }
            return flag && super.doHurtTarget(pEntity);
        }
        return super.doHurtTarget(pEntity);
    }

    // TODO 目前还没有在其他component渲染的地方实现海嗣文转写，需要完成这个
    @Override
    public Component getDisplayName() {
        if (this.hasCustomName()) {
            return super.getDisplayName();
        }
        return ModFontHelper.translatableSeaborn(this.getType().getDescriptionId(), MiscConfig.USE_SEABORN_LANGUAGE.get(), this.uuid.getLeastSignificantBits() % 2 == 0)
                .withStyle(style -> style.withHoverEvent(this.createHoverEvent()).withInsertion(this.getStringUUID()).withColor(this.getTeamColor()));
    }
}
