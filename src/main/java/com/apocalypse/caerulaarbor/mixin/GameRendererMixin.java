package com.apocalypse.caerulaarbor.mixin;

import com.apocalypse.caerulaarbor.bridge.NoHurtEffectsTracker;
import com.apocalypse.caerulaarbor.init.ModDamageTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    public abstract Minecraft getMinecraft();

    @Inject(method = "bobHurt", at = @At("HEAD"), cancellable = true)
    private void bobHurt(PoseStack pMatrixStack, float pPartialTicks, CallbackInfo ci) {
        if (this.getMinecraft().getCameraEntity() instanceof LivingEntity living) {
            DamageSource source = living.getLastDamageSource();
            if (source == null && living instanceof NoHurtEffectsTracker tracker) {
                source = tracker.caerula_arbor$getNoHurtDamageSource();
            }
            if (source != null && source.is(ModDamageTypes.HEMOPOIETIC_DISORDER)) {
                ci.cancel();
            }
        }
    }
}
