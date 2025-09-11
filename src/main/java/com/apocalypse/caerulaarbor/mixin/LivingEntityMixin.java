package com.apocalypse.caerulaarbor.mixin;

import com.apocalypse.caerulaarbor.bridge.NoHurtEffectsTracker;
import com.apocalypse.caerulaarbor.init.ModTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public class LivingEntityMixin implements NoHurtEffectsTracker {

    @Unique
    @Nullable
    private DamageSource caerula_arbor$noHurtDamageSource;

    @Unique
    private long caerula_arbor$noHurtDamageStamp;

    @Inject(method = "playHurtSound", at = @At("HEAD"), cancellable = true)
    protected void playHurtSound(DamageSource pSource, CallbackInfo ci) {
        if (pSource.is(ModTags.DamageTypes.NO_HURT_EFFECTS)) {
            ci.cancel();
        }
    }

    @Inject(method = "handleDamageEvent", at = @At("HEAD"), cancellable = true)
    public void handleDamageEvent(DamageSource pSource, CallbackInfo ci) {
        if (pSource.is(ModTags.DamageTypes.NO_HURT_EFFECTS)) {
            ci.cancel();

            LivingEntity living = (LivingEntity) (Object) this;
            living.invulnerableTime = 0;
            living.hurtTime = 0;
            living.hurtDuration = 0;
            // 记录最近一次不触发受伤特效的伤害来源及时间戳
            this.caerula_arbor$noHurtDamageSource = pSource;
            this.caerula_arbor$noHurtDamageStamp = living.level().getGameTime();
        }
    }

    @Override
    public DamageSource caerula_arbor$getNoHurtDamageSource() {
        return this.caerula_arbor$noHurtDamageSource;
    }

    @Override
    public void caerula_arbor$setNoHurtDamageSource(DamageSource source, long gameTime) {
        this.caerula_arbor$noHurtDamageSource = source;
        this.caerula_arbor$noHurtDamageStamp = gameTime;
    }
}
