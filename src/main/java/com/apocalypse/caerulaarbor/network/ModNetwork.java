package com.apocalypse.caerulaarbor.network;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.network.message.receive.PlayerVariablesSyncMessage;
import com.apocalypse.caerulaarbor.network.message.receive.SavedDataSyncMessage;
import com.apocalypse.caerulaarbor.network.message.send.*;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModNetwork {

    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel PACKET_HANDLER = NetworkRegistry.newSimpleChannel(CaerulaArborMod.loc(CaerulaArborMod.MODID), () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals);
    public static int messageID = 0;

    public static void register() {
        playToClient(PlayerVariablesSyncMessage.class, PlayerVariablesSyncMessage::encode, PlayerVariablesSyncMessage::decode, PlayerVariablesSyncMessage::handler);
        playToClient(SavedDataSyncMessage.class, SavedDataSyncMessage::encode, SavedDataSyncMessage::decode, SavedDataSyncMessage::handler);

        playToServer(CaerulaRecordGUIButtonMessage.class, CaerulaRecordGUIButtonMessage::encode, CaerulaRecordGUIButtonMessage::decode, CaerulaRecordGUIButtonMessage::handler);
        playToServer(EvoTreeButtonMessage.class, EvoTreeButtonMessage::buffer, EvoTreeButtonMessage::new, EvoTreeButtonMessage::handler);
        playToServer(InfoStrategyAllButtonMessage.class, InfoStrategyAllButtonMessage::buffer, InfoStrategyAllButtonMessage::new, InfoStrategyAllButtonMessage::handler);
        playToServer(InfoStrategyBreedButtonMessage.class, InfoStrategyBreedButtonMessage::buffer, InfoStrategyBreedButtonMessage::new, InfoStrategyBreedButtonMessage::handler);
        playToServer(InfoStrategyGrowButtonMessage.class, InfoStrategyGrowButtonMessage::buffer, InfoStrategyGrowButtonMessage::new, InfoStrategyGrowButtonMessage::handler);
        playToServer(InfoStrategyMigrationButtonMessage.class, InfoStrategyMigrationButtonMessage::buffer, InfoStrategyMigrationButtonMessage::new, InfoStrategyMigrationButtonMessage::handler);
        playToServer(InfoStrategySubsisButtonMessage.class, InfoStrategySubsisButtonMessage::buffer, InfoStrategySubsisButtonMessage::new, InfoStrategySubsisButtonMessage::handler);
        playToServer(RelicShowcaseButtonMessage.class, RelicShowcaseButtonMessage::buffer, RelicShowcaseButtonMessage::new, RelicShowcaseButtonMessage::handler);
    }

    public static <T> void playToClient(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer, Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        messageID++;
    }

    /**
     * 注册无参数、向客户端发送的消息
     */
    @SuppressWarnings("unchecked")
    public static <T> void playToClient(T instance, Consumer<Supplier<NetworkEvent.Context>> messageConsumer) {
        var type = (Class<T>) instance.getClass();
        PACKET_HANDLER.registerMessage(messageID, type, (msg, buf) -> {
        }, (buf) -> instance, (msg, ctx) -> messageConsumer.accept(ctx), Optional.of(NetworkDirection.PLAY_TO_CLIENT));
        messageID++;
    }

    public static <T> void playToServer(Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkEvent.Context>> messageConsumer) {
        PACKET_HANDLER.registerMessage(messageID, messageType, encoder, decoder, messageConsumer, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        messageID++;
    }

    /**
     * 注册无参数、向服务器发送的消息
     */
    @SuppressWarnings("unchecked")
    public static <T> void playToServer(T instance, Consumer<Supplier<NetworkEvent.Context>> messageConsumer) {
        var type = (Class<T>) instance.getClass();
        PACKET_HANDLER.registerMessage(messageID, type, (msg, buf) -> {
        }, (buf) -> instance, (msg, ctx) -> messageConsumer.accept(ctx), Optional.of(NetworkDirection.PLAY_TO_SERVER));
        messageID++;
    }
}
