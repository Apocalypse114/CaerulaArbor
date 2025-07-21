package com.apocalypse.caerulaarbor.network;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CaerulaArborModVariables {

    @Mod.EventBusSubscriber
    public static class EventBusVariableHandlers {

        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            if (!event.getEntity().level().isClientSide()) {
                SavedData mapdata = MapVariables.get(event.getEntity().level());
                SavedData worlddata = WorldVariables.get(event.getEntity().level());
                if (mapdata != null)
                    CaerulaArborMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SavedDataSyncMessage(0, mapdata));
                if (worlddata != null)
                    CaerulaArborMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SavedDataSyncMessage(1, worlddata));
            }
        }

        @SubscribeEvent
        public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (!event.getEntity().level().isClientSide()) {
                SavedData worlddata = WorldVariables.get(event.getEntity().level());
                if (worlddata != null)
                    CaerulaArborMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SavedDataSyncMessage(1, worlddata));
            }
        }
    }

    public static class WorldVariables extends SavedData {
        public static final String DATA_NAME = "caerula_arbor_worldvars";

        public static WorldVariables load() {
            WorldVariables data = new WorldVariables();
            data.read();
            return data;
        }

        public void read() {
        }

        @Override
        public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
            return nbt;
        }

        public void syncData(LevelAccessor world) {
            this.setDirty();
            if (world instanceof Level level && !level.isClientSide())
                CaerulaArborMod.PACKET_HANDLER.send(PacketDistributor.DIMENSION.with(level::dimension), new SavedDataSyncMessage(1, this));
        }

        static WorldVariables clientSide = new WorldVariables();

        public static WorldVariables get(LevelAccessor world) {
            if (world instanceof ServerLevel level) {
                return level.getDataStorage().computeIfAbsent((CompoundTag t) -> load(), WorldVariables::new, DATA_NAME);
            } else {
                return clientSide;
            }
        }
    }

    public static class MapVariables extends SavedData {
        public static final String DATA_NAME = "caerula_arbor_mapvars";
        public double evo_point_grow = 0;
        public double evo_point_subsisting = 0;
        public double evo_point_breed = 0;
        public double evo_point_migration = 0;
        public int strategyGrow = 0;
        public int strategySubsisting = 0;
        public int strategyBreed = 0;
        public int strategyMigration = 0;
        public int strategy_silence = 0;
        public double evo_point_silence = 0;
        public boolean silence_enabled = false;

        public static MapVariables load(CompoundTag tag) {
            MapVariables data = new MapVariables();
            data.read(tag);
            return data;
        }

        public void read(CompoundTag nbt) {
            evo_point_grow = nbt.getDouble("evo_point_grow");
            evo_point_subsisting = nbt.getDouble("evo_point_subsisting");
            evo_point_breed = nbt.getDouble("evo_point_breed");
            evo_point_migration = nbt.getDouble("evo_point_migration");
            strategyGrow = nbt.getInt("StrategyGrow");
            strategySubsisting = nbt.getInt("StrategySubsisting");
            strategyBreed = nbt.getInt("StrategyBreed");
            strategyMigration = nbt.getInt("StrategyMigration");
            strategy_silence = nbt.getInt("strategy_silence");
            evo_point_silence = nbt.getDouble("evo_point_silence");
            silence_enabled = nbt.getBoolean("silence_enabled");
        }

        @Override
        public @NotNull CompoundTag save(CompoundTag nbt) {
            nbt.putDouble("evo_point_grow", evo_point_grow);
            nbt.putDouble("evo_point_subsisting", evo_point_subsisting);
            nbt.putDouble("evo_point_breed", evo_point_breed);
            nbt.putDouble("evo_point_migration", evo_point_migration);
            nbt.putDouble("StrategyGrow", strategyGrow);
            nbt.putDouble("StrategySubsisting", strategySubsisting);
            nbt.putDouble("StrategyBreed", strategyBreed);
            nbt.putDouble("StrategyMigration", strategyMigration);
            nbt.putDouble("strategy_silence", strategy_silence);
            nbt.putDouble("evo_point_silence", evo_point_silence);
            nbt.putBoolean("silence_enabled", silence_enabled);
            return nbt;
        }

        public void syncData(LevelAccessor world) {
            this.setDirty();
            if (world instanceof Level && !world.isClientSide())
                CaerulaArborMod.PACKET_HANDLER.send(PacketDistributor.ALL.noArg(), new SavedDataSyncMessage(0, this));
        }

        static MapVariables clientSide = new MapVariables();

        public static MapVariables get(LevelAccessor world) {
            if (world instanceof ServerLevelAccessor serverLevelAcc) {
                return serverLevelAcc.getLevel().getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(MapVariables::load, MapVariables::new, DATA_NAME);
            } else {
                return clientSide;
            }
        }
    }

    public static class SavedDataSyncMessage {
        private final int type;
        private SavedData data;

        public SavedDataSyncMessage(FriendlyByteBuf buffer) {
            this.type = buffer.readInt();
            CompoundTag nbt = buffer.readNbt();
            if (nbt != null) {
                this.data = this.type == 0 ? new MapVariables() : new WorldVariables();
                if (this.data instanceof MapVariables mapVariables)
                    mapVariables.read(nbt);
                else if (this.data instanceof WorldVariables worldVariables)
                    worldVariables.read();
            }
        }

        public SavedDataSyncMessage(int type, SavedData data) {
            this.type = type;
            this.data = data;
        }

        public static void buffer(SavedDataSyncMessage message, FriendlyByteBuf buffer) {
            buffer.writeInt(message.type);
            if (message.data != null)
                buffer.writeNbt(message.data.save(new CompoundTag()));
        }

        public static void handler(SavedDataSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (!context.getDirection().getReceptionSide().isServer() && message.data != null) {
                    if (message.type == 0)
                        MapVariables.clientSide = (MapVariables) message.data;
                    else
                        WorldVariables.clientSide = (WorldVariables) message.data;
                }
            });
            context.setPacketHandled(true);
        }
    }

}
