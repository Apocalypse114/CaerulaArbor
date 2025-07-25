package com.apocalypse.caerulaarbor.capability.sanity;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface ISanityInjuryCapability extends INBTSerializable<CompoundTag> {
    boolean hurt(double amount);

    void heal(double amount);

    void tick();
}
