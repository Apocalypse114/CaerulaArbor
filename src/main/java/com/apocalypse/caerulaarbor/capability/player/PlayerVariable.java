package com.apocalypse.caerulaarbor.capability.player;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.capability.Relic;
import com.apocalypse.caerulaarbor.network.ModNetwork;
import com.apocalypse.caerulaarbor.network.message.receive.PlayerVariablesSyncMessage;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

// TODO 把这坨清理了
@AutoRegisterCapability
public class PlayerVariable implements INBTSerializable<CompoundTag> {

    public static ResourceLocation ID = CaerulaArborMod.loc("player_variables");

    // 灯火
    public double light = 100.0;
    // 目标生命值
    public int life = 6;
    // 最大目标生命值
    public int maxLive = 6;
    // 护盾值
    public int shield = 0;
    // 排异反应（0为无排异，1/2/4/8分别对应一个排异反应）
    public int rejection = 0;
    // 海嗣化程度
    public int seabornization = 0;

    public boolean show_stats = false;
    public boolean kingShowPtc = true;
    public ItemStack chitin_knife_selected = ItemStack.EMPTY;
    public int player_king_suit = 0;
    public int player_demon_suit = 0;

    public HashMap<Relic, Integer> relics = new HashMap<>();

    public void syncPlayerVariables(Entity entity) {
        if (entity instanceof ServerPlayer serverPlayer)
            ModNetwork.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> serverPlayer), new PlayerVariablesSyncMessage(this));
    }

    public CompoundTag writeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putDouble("Light", this.light);
        nbt.putInt("Life", this.life);
        nbt.putInt("MaxLife", this.maxLive);
        nbt.putInt("Shield", this.shield);
        nbt.putInt("Rejection", this.rejection);
        nbt.putInt("Seabornization", this.seabornization);

        nbt.putBoolean("show_stats", this.show_stats);
        nbt.putBoolean("kingShowPtc", this.kingShowPtc);

        for (var relic : Relic.values()) {
            if (relic.gained(this)) {
                nbt.putInt(relic.name(), relic.get(this));
            }
        }

        nbt.put("chitin_knife_selected", this.chitin_knife_selected.save(new CompoundTag()));
        nbt.putInt("player_king_suit", this.player_king_suit);
        nbt.putInt("player_demon_suit", this.player_demon_suit);
        return nbt;
    }

    public void readNBT(@Nullable CompoundTag tag) {
        if (tag == null) return;

        this.light = tag.getDouble("Light");
        this.life = tag.getInt("Life");
        this.maxLive = tag.getInt("MaxLife");
        this.shield = tag.getInt("Shield");
        this.rejection = tag.getInt("Rejection");
        this.seabornization = tag.getInt("Seabornization");

        this.show_stats = tag.getBoolean("show_stats");
        this.kingShowPtc = tag.getBoolean("kingShowPtc");

        for (var relic : Relic.values()) {
            if (tag.contains(relic.name())) {
                relic.set(this, tag.getInt(relic.name()));
            }
        }

        this.chitin_knife_selected = ItemStack.of(tag.getCompound("chitin_knife_selected"));
        this.player_king_suit = tag.getInt("player_king_suit");
        this.player_demon_suit = tag.getInt("player_demon_suit");
    }

    @Override
    public CompoundTag serializeNBT() {
        return writeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.readNBT(nbt);
    }

    public void setRejection(Rejection rejection, boolean invoke) {
        if (invoke) {
            this.rejection |= rejection.bitMask;
        } else {
            this.rejection &= ~rejection.bitMask;
        }
    }

    public boolean isRejectionInvoked(Rejection rejection) {
        return (this.rejection & rejection.bitMask) != 0;
    }

    public enum Rejection {
        HEMOPOIETIC_INHIBITION(0b0001, "caerula_arbor.rejection.hemopoietic_inhibition"),
        CONCENTRATION_DISORDER(0b0010, "caerula_arbor.rejection.concentration_disorder"),
        NEURODEGENERATION(0b0100, "caerula_arbor.rejection.neurodegeneration"),
        METASTATIC_ABERRATION(0b1000, "caerula_arbor.rejection.metastatic_aberration");

        public final int bitMask;
        public final String name;

        Rejection(int bitMask, String name) {
            this.bitMask = bitMask;
            this.name = name;
        }
    }
}
