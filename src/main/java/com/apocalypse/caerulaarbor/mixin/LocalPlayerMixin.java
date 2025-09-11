package com.apocalypse.caerulaarbor.mixin;

import com.apocalypse.caerulaarbor.bridge.NoHurtEffectsTracker;
import com.apocalypse.caerulaarbor.init.ModTags;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {

    @Shadow
    private boolean flashOnSetHealth;

    protected LocalPlayerMixin(ClientLevel level, GameProfile profile) {
        super(level, profile);
    }

    @Inject(method = "hurtTo", at = @At("HEAD"), cancellable = true)
    public void hurtTo(float pHealth, CallbackInfo ci) {
        if (this.flashOnSetHealth) {
            LocalPlayer player = (LocalPlayer) (Object) this;
            var src = player.getLastDamageSource();
            if (src == null && player instanceof NoHurtEffectsTracker tracker) {
                src = tracker.caerula_arbor$getNoHurtDamageSource();
            }
            if (player.getHealth() > pHealth && src != null && src.is(ModTags.DamageTypes.NO_HURT_EFFECTS)) {
                ci.cancel();
                this.lastHurt = player.getHealth() - pHealth;
                this.invulnerableTime = 0;
                this.setHealth(pHealth);
                this.hurtDuration = 0;
                this.hurtTime = 0;
            }
        }
    }
}
