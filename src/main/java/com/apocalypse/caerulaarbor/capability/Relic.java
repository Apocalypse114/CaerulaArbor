package com.apocalypse.caerulaarbor.capability;

import com.apocalypse.caerulaarbor.api.event.RelicEvent;
import com.apocalypse.caerulaarbor.init.ModItems;
import com.apocalypse.caerulaarbor.network.CaerulaArborModVariables;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;

import java.util.function.Consumer;

// TODO 需要在玩家获取、更新、失去藏品时，发送对应的事件
public enum Relic {
    FEATURED_CANNED_MEAT,
    SEAWEED_SALAD,
    ORANGE_STORM,
    COFFEE_PLAINS_COFFEE_CANDY,
    SCREAMING_CHERRY,
    PITTS_ASSORTED_FRUITS,
    EXTRA_PUNGENT_COFFEE_BEANS,

    CURSED_EMELIGHT,
    CURSED_GLOWBODY,
    CURSED_RESEARCH,
    KING_CROWN,
    KING_ARMOR,
    KING_SPEAR,
    KING_EXTENSION,
    KING_CRYSTAL,
    HAND_THORNS,
    HAND_STRANGLE,
    HAND_FERTILITY,
    HAND_SPEED,
    HAND_OF_PULVERIZATION(-1, 8, -1),
    HAND_SWIPE,
    SARKAZ_KING_ARTIFACT,
    HAND_FIREWORK,
    SARKAZ_KING_FLAG,
    HAND_ENGRAVE(-1, 99, -1),
    SARKAZ_KING_BED,
    SURVIVOR_CONTRACT(-1, 32, -1),
    TREATY,
    SARKAZ_KING_RYLFATE,
    UTIL_MUSICBOX,
    UTIL_IRIS,
    WEIRD_FLUTE,
    PURE_GOLD_EXPEDITION,
    DURIN_OVERGROUND_ODYSSEY,
    UTIL_TOPONYM,
    HOT_WATER_KETTLE,
    LEGEND_CHITIN,
    UTIL_ALLEY,
    VAMPIRES_BED,
    PROOF_OF_LONGEVITY,
    UTIL_OMNIKEY,
    UTIL_SCORE,
    UTIL_RESCISSION,
    UTIL_STARE,
    HAND_SWORD,
    CURSED_HEART;

    public final int minLevel;
    public final int maxLevel;
    public final int defaultLevel;
    public Item item;

    Relic() {
        this(0, 1, 0);
    }

    Relic(int minLevel, int maxLevel, int defaultLevel) {
        this.minLevel = minLevel;
        this.maxLevel = maxLevel;
        this.defaultLevel = defaultLevel;
    }

    public int get(Entity player) {
        return player.getCapability(CaerulaArborModVariables.PLAYER_VARIABLES_CAPABILITY)
                .map(this::get)
                .orElse(defaultLevel);
    }

    public int get(CaerulaArborModVariables.PlayerVariables variables) {
        return variables.relics.getOrDefault(this, defaultLevel);
    }

    public boolean gained(Entity player) {
        return player.getCapability(CaerulaArborModVariables.PLAYER_VARIABLES_CAPABILITY)
                .map(this::gained)
                .orElse(false);
    }

    public boolean gained(CaerulaArborModVariables.PlayerVariables variables) {
        return this.get(variables) != defaultLevel;
    }

    public void reset(Entity player) {
        player.getCapability(CaerulaArborModVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(this::reset);
    }

    public void reset(CaerulaArborModVariables.PlayerVariables variables) {
        variables.relics.remove(this);
    }

    public void set(Entity player, int level) {
        player.getCapability(CaerulaArborModVariables.PLAYER_VARIABLES_CAPABILITY)
                .ifPresent(c -> {
                    if (level == this.defaultLevel) {
                        remove(player);
                    } else {
                        set(c, level);
                        MinecraftForge.EVENT_BUS.post(new RelicEvent.Update(player, this));
                    }
                });
    }

    public void set(CaerulaArborModVariables.PlayerVariables variables, int level) {
        variables.relics.put(this, Mth.clamp(level, minLevel, maxLevel));
    }

    public void gain(Entity player) {
        set(player, 1);
        MinecraftForge.EVENT_BUS.post(new RelicEvent.Gain(player, this));
    }

    // TODO 尽量不用这个，需要发event
    public void gain(CaerulaArborModVariables.PlayerVariables variables) {
        set(variables, 1);
    }

    public void remove(Entity player) {
        player.getCapability(CaerulaArborModVariables.PLAYER_VARIABLES_CAPABILITY)
                .ifPresent(this::remove);
        MinecraftForge.EVENT_BUS.post(new RelicEvent.Remove(player, this));
    }

    public void remove(CaerulaArborModVariables.PlayerVariables variables) {
        variables.relics.remove(this);
    }

    public static void modify(Entity player, Consumer<CaerulaArborModVariables.PlayerVariables> operation) {
        player.getCapability(CaerulaArborModVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(cap -> modify(cap, player, operation));
    }

    public static void modify(CaerulaArborModVariables.PlayerVariables cap, Entity player, Consumer<CaerulaArborModVariables.PlayerVariables> operation) {
        operation.accept(cap);
        cap.syncPlayerVariables(player);
    }

    public static int getLevel(Entity player, Relic relic) {
        return player.getCapability(CaerulaArborModVariables.PLAYER_VARIABLES_CAPABILITY)
                .map(relic::get)
                .orElse(relic.defaultLevel);
    }

    public void modify(Entity player, int value) {
        player.getCapability(CaerulaArborModVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(cap -> modify(cap, player, value));
    }

    public void modify(CaerulaArborModVariables.PlayerVariables cap, Entity player, int value) {
        this.set(cap, value);
        cap.syncPlayerVariables(player);
    }

    public void gainAndSync(Entity player) {
        player.getCapability(CaerulaArborModVariables.PLAYER_VARIABLES_CAPABILITY).ifPresent(cap -> gainAndSync(cap, player));
    }

    public void gainAndSync(CaerulaArborModVariables.PlayerVariables cap, Entity player) {
        this.set(cap, 1);
        cap.syncPlayerVariables(player);
    }

    // TODO 这里的双向绑定item是不是可以优化？
    public static void onRegisterItem() {
        FEATURED_CANNED_MEAT.item = ModItems.FEATURED_CANNED_MEAT.get();
        SEAWEED_SALAD.item = ModItems.SEAWEED_SALAD.get();
        ORANGE_STORM.item = ModItems.ORANGE_STORM.get();
        COFFEE_PLAINS_COFFEE_CANDY.item = ModItems.COFFEE_PLAINS_COFFEE_CANDY.get();
        SCREAMING_CHERRY.item = ModItems.SCREAMING_CHERRY.get();
        PITTS_ASSORTED_FRUITS.item = ModItems.PITTS_ASSORTED_FRUITS.get();

        HAND_OF_PULVERIZATION.item = ModItems.HAND_OF_PULVERIZATION.get();
    }
}
