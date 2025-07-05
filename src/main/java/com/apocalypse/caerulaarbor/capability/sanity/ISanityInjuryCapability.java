package com.apocalypse.caerulaarbor.capability.sanity;

import net.minecraft.nbt.DoubleTag;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;

@AutoRegisterCapability
public interface ISanityInjuryCapability extends INBTSerializable<DoubleTag> {
    boolean hurt(double amount);
    void heal(double amount);
}
