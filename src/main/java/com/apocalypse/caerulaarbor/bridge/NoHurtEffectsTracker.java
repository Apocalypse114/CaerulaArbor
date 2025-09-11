package com.apocalypse.caerulaarbor.bridge;

import net.minecraft.world.damagesource.DamageSource;

public interface NoHurtEffectsTracker {
    DamageSource caerula_arbor$getNoHurtDamageSource();
    void caerula_arbor$setNoHurtDamageSource(DamageSource source, long gameTime);
}