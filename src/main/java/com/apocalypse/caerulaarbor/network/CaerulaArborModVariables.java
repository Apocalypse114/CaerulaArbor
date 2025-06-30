package com.apocalypse.caerulaarbor.network;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.Relic;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.HashMap;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CaerulaArborModVariables {

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        CaerulaArborMod.addNetworkMessage(SavedDataSyncMessage.class, SavedDataSyncMessage::buffer, SavedDataSyncMessage::new, SavedDataSyncMessage::handler);
        CaerulaArborMod.addNetworkMessage(PlayerVariablesSyncMessage.class, PlayerVariablesSyncMessage::buffer, PlayerVariablesSyncMessage::new, PlayerVariablesSyncMessage::handler);
    }

    @SubscribeEvent
    public static void init(RegisterCapabilitiesEvent event) {
        event.register(PlayerVariables.class);
    }

    @Mod.EventBusSubscriber
    public static class EventBusVariableHandlers {
        @SubscribeEvent
        public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
            if (!event.getEntity().level().isClientSide())
                event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()).syncPlayerVariables(event.getEntity());
        }

        @SubscribeEvent
        public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
            if (!event.getEntity().level().isClientSide())
                event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()).syncPlayerVariables(event.getEntity());
        }

        @SubscribeEvent
        public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (!event.getEntity().level().isClientSide())
                event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables()).syncPlayerVariables(event.getEntity());
        }

        @SubscribeEvent
        public static void clonePlayer(PlayerEvent.Clone event) {
            event.getOriginal().revive();
            PlayerVariables original = event.getOriginal().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
            PlayerVariables clone = event.getEntity().getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
            clone.light = original.light;
            clone.lives = original.lives;
            clone.maxLive = original.maxLive;
            clone.shield = original.shield;
            clone.disoclusion = original.disoclusion;
            clone.show_stats = original.show_stats;
            clone.kingShowPtc = original.kingShowPtc;

            for (var relic : Relic.values()) {
                if (relic.gained(original)) {
                    relic.set(clone, relic.get(original));
                }
            }

            clone.player_king_suit = original.player_king_suit;
            clone.player_demon_suit = original.player_demon_suit;
            clone.player_oceanization = original.player_oceanization;
            if (!event.isWasDeath()) {
                clone.chitin_knife_selected = original.chitin_knife_selected;
            }
        }

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
        public double strategy_grow = 0;
        public double strategy_subsisting = 0;
        public double strategy_breed = 0;
        public double strategy_migration = 0;
        public double strategy_silence = 0;
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
            strategy_grow = nbt.getDouble("strategy_grow");
            strategy_subsisting = nbt.getDouble("strategy_subsisting");
            strategy_breed = nbt.getDouble("strategy_breed");
            strategy_migration = nbt.getDouble("strategy_migration");
            strategy_silence = nbt.getDouble("strategy_silence");
            evo_point_silence = nbt.getDouble("evo_point_silence");
            silence_enabled = nbt.getBoolean("silence_enabled");
        }

        @Override
        public @NotNull CompoundTag save(CompoundTag nbt) {
            nbt.putDouble("evo_point_grow", evo_point_grow);
            nbt.putDouble("evo_point_subsisting", evo_point_subsisting);
            nbt.putDouble("evo_point_breed", evo_point_breed);
            nbt.putDouble("evo_point_migration", evo_point_migration);
            nbt.putDouble("strategy_grow", strategy_grow);
            nbt.putDouble("strategy_subsisting", strategy_subsisting);
            nbt.putDouble("strategy_breed", strategy_breed);
            nbt.putDouble("strategy_migration", strategy_migration);
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

    public static final Capability<PlayerVariables> PLAYER_VARIABLES_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
    });

    @Mod.EventBusSubscriber
    private static class PlayerVariablesProvider implements ICapabilitySerializable<Tag> {
        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer))
                event.addCapability(new ResourceLocation("caerula_arbor", "player_variables"), new PlayerVariablesProvider());
        }

        private final PlayerVariables playerVariables = new PlayerVariables();
        private final LazyOptional<PlayerVariables> instance = LazyOptional.of(() -> playerVariables);

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return cap == PLAYER_VARIABLES_CAPABILITY ? instance.cast() : LazyOptional.empty();
        }

        @Override
        public Tag serializeNBT() {
            return playerVariables.writeNBT();
        }

        @Override
        public void deserializeNBT(Tag nbt) {
            playerVariables.readNBT(nbt);
        }
    }

    // TODO 把这坨清理了
    public static class PlayerVariables {
        public double light = 100.0;
        public double lives = 6.0;
        public double maxLive = 6.0;
        public double shield = 0;
        public int disoclusion = 0;
        public boolean show_stats = false;
        public boolean kingShowPtc = true;
        public ItemStack chitin_knife_selected = ItemStack.EMPTY;
        public double player_king_suit = 0;
        public double player_demon_suit = 0;
        public double player_oceanization = 0;

        public HashMap<Relic, Integer> relics = new HashMap<>();

        public void syncPlayerVariables(Entity entity) {
            if (entity instanceof ServerPlayer serverPlayer)
                CaerulaArborMod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PlayerVariablesSyncMessage(this));
        }

        public Tag writeNBT() {
            CompoundTag nbt = new CompoundTag();
            nbt.putDouble("player_light", light);
            nbt.putDouble("player_lives", lives);
            nbt.putDouble("player_maxlive", maxLive);
            nbt.putDouble("player_shield", shield);
            nbt.putInt("disoclusion", disoclusion);
            nbt.putBoolean("show_stats", show_stats);
            nbt.putBoolean("kingShowPtc", kingShowPtc);

            for (var relic : Relic.values()) {
                if (relic.gained(this)) {
                    nbt.putInt(relic.name(), relic.get(this));
                }
            }

            nbt.put("chitin_knife_selected", chitin_knife_selected.save(new CompoundTag()));
            nbt.putDouble("player_king_suit", player_king_suit);
            nbt.putDouble("player_demon_suit", player_demon_suit);
            nbt.putDouble("player_oceanization", player_oceanization);
            return nbt;
        }

        public void readNBT(Tag tag) {
            CompoundTag nbt = (CompoundTag) tag;
            light = nbt.getDouble("player_light");
            lives = nbt.getDouble("player_lives");
            maxLive = nbt.getDouble("player_maxlive");
            shield = nbt.getDouble("player_shield");
            disoclusion = nbt.getInt("disoclusion");
            show_stats = nbt.getBoolean("show_stats");
            kingShowPtc = nbt.getBoolean("kingShowPtc");

            for (var relic : Relic.values()) {
                if (nbt.contains(relic.name())) {
                    relic.set(this, nbt.getInt(relic.name()));
                }
            }

            chitin_knife_selected = ItemStack.of(nbt.getCompound("chitin_knife_selected"));
            player_king_suit = nbt.getDouble("player_king_suit");
            player_demon_suit = nbt.getDouble("player_demon_suit");
            player_oceanization = nbt.getDouble("player_oceanization");
        }
    }

    public static class PlayerVariablesSyncMessage {
        private final PlayerVariables data;

        public PlayerVariablesSyncMessage(FriendlyByteBuf buffer) {
            this.data = new PlayerVariables();
            this.data.readNBT(buffer.readNbt());
        }

        public PlayerVariablesSyncMessage(PlayerVariables data) {
            this.data = data;
        }

        public static void buffer(PlayerVariablesSyncMessage message, FriendlyByteBuf buffer) {
            buffer.writeNbt((CompoundTag) message.data.writeNBT());
        }

        public static void handler(PlayerVariablesSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (!context.getDirection().getReceptionSide().isServer()) {
                    PlayerVariables variables = Minecraft.getInstance().player.getCapability(PLAYER_VARIABLES_CAPABILITY, null).orElse(new PlayerVariables());
                    variables.light = message.data.light;
                    variables.lives = message.data.lives;
                    variables.maxLive = message.data.maxLive;
                    variables.shield = message.data.shield;
                    variables.disoclusion = message.data.disoclusion;
                    variables.show_stats = message.data.show_stats;
                    variables.kingShowPtc = message.data.kingShowPtc;

                    for (var relic : Relic.values()) {
                        relic.set(variables, message.data.relics.getOrDefault(relic, relic.defaultLevel));
                    }

                    variables.chitin_knife_selected = message.data.chitin_knife_selected;
                    variables.player_king_suit = message.data.player_king_suit;
                    variables.player_demon_suit = message.data.player_demon_suit;
                    variables.player_oceanization = message.data.player_oceanization;
                }
            });
            context.setPacketHandled(true);
        }
    }
}
