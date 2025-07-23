package com.apocalypse.caerulaarbor.capability.map;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.network.message.s2c.SavedDataSyncMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

public class MapVariables extends SavedData {

    public static MapVariables clientSide = new MapVariables();

    public static final String DATA_NAME = "caerula_arbor_map_variables";

    public double evoPointGrow = 0;
    public double evoPointSubsisting = 0;
    public double evoPointBreed = 0;
    public double evoPointMigration = 0;
    public double evoPointSilence = 0;

    public int strategyGrow = 0;
    public int strategySubsisting = 0;
    public int strategyBreed = 0;
    public int strategyMigration = 0;
    public int strategySilence = 0;
    public boolean enabledStrategySilence = false;

    public static MapVariables load(CompoundTag tag) {
        MapVariables data = new MapVariables();
        data.read(tag);
        return data;
    }

    public void read(CompoundTag nbt) {
        evoPointGrow = nbt.getDouble("EvoPointGrow");
        evoPointSubsisting = nbt.getDouble("EvoPointSubsisting");
        evoPointBreed = nbt.getDouble("EvoPointBreed");
        evoPointMigration = nbt.getDouble("EvoPointMigration");
        evoPointSilence = nbt.getDouble("EvoPointSilence");

        strategyGrow = nbt.getInt("StrategyGrow");
        strategySubsisting = nbt.getInt("StrategySubsisting");
        strategyBreed = nbt.getInt("StrategyBreed");
        strategyMigration = nbt.getInt("StrategyMigration");
        strategySilence = nbt.getInt("StrategySilence");

        enabledStrategySilence = nbt.getBoolean("EnabledStrategySilence");
    }

    @Override
    public @NotNull CompoundTag save(CompoundTag nbt) {
        nbt.putDouble("EvoPointGrow", evoPointGrow);
        nbt.putDouble("EvoPointSubsisting", evoPointSubsisting);
        nbt.putDouble("EvoPointBreed", evoPointBreed);
        nbt.putDouble("EvoPointMigration", evoPointMigration);
        nbt.putDouble("EvoPointSilence", evoPointSilence);

        nbt.putInt("StrategyGrow", strategyGrow);
        nbt.putInt("StrategySubsisting", strategySubsisting);
        nbt.putInt("StrategyBreed", strategyBreed);
        nbt.putInt("StrategyMigration", strategyMigration);
        nbt.putInt("StrategySilence", strategySilence);

        nbt.putBoolean("EnabledStrategySilence", enabledStrategySilence);
        return nbt;
    }

    public void syncData(Level level) {
        this.setDirty();
        if (!level.isClientSide()) {
            CaerulaArborMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new SavedDataSyncMessage(this));
        }
    }

    @NotNull
    public static MapVariables get(Level level) {
        if (level instanceof ServerLevel serverLevel) {
            var overworld = serverLevel.getServer().getLevel(Level.OVERWORLD);
            if (overworld == null) {
                CaerulaArborMod.LOGGER.warn("No overworld server level found!");
                return new MapVariables();
            }
            return overworld.getDataStorage().computeIfAbsent(MapVariables::load, MapVariables::new, DATA_NAME);
        } else {
            return clientSide;
        }
    }

    public enum StrategyType {
        GROW, SUBSISTING, BREED, MIGRATION, SILENCE;
    }
}
