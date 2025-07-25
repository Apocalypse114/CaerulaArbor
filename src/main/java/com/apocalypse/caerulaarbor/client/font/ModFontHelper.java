package com.apocalypse.caerulaarbor.client.font;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.config.server.MiscConfig;
import net.minecraft.client.resources.language.ClientLanguage;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimplePreparableReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.NotNull;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.zip.CRC32;

@Mod.EventBusSubscriber(modid = CaerulaArborMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModFontHelper {
    public static ClientLanguage EN_US;

    public static final Style SEABORN_LANGUAGE = Style.EMPTY.withFont(CaerulaArborMod.loc("seaborn_language"));
    public static final Style SEABORN_LANGUAGE_INVERTED = Style.EMPTY.withFont(CaerulaArborMod.loc("seaborn_language_inverted"));

    public static final HashMap<String, Boolean[]> INFO_CACHE = new HashMap<>();

    private static Random getRandom(String string) {
        CRC32 crc32 = new CRC32();
        crc32.update(string.getBytes());
        long seed = crc32.getValue();
        return new Random(seed);
    }

    @SubscribeEvent
    public static void onResourcePackReload(RegisterClientReloadListenersEvent event) {
        event.registerReloadListener(new SimplePreparableReloadListener<ClientLanguage>() {
            @Override
            @ParametersAreNonnullByDefault
            protected @NotNull ClientLanguage prepare(ResourceManager pResourceManager, ProfilerFiller pProfiler) {
                return ClientLanguage.loadFrom(pResourceManager, List.of("en_us"), false);
            }

            @Override
            @ParametersAreNonnullByDefault
            protected void apply(ClientLanguage pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
                EN_US = pObject;
            }
        });
    }

    public static MutableComponent randomInvert(String text) {
        return randomInvert(text, false);
    }

    public static MutableComponent randomInvert(String text, boolean invert) {
        Random random = getRandom(text);
        MutableComponent component = Component.literal("");
        if (INFO_CACHE.containsKey(text)) {
            for (int i = 0; i < text.length(); i++) {
                component = component.append(Component.literal(text.charAt(i) + "").withStyle(INFO_CACHE.get(text)[i] ^ invert ? SEABORN_LANGUAGE_INVERTED : SEABORN_LANGUAGE));
            }
        } else {
            Boolean[] booleans = new Boolean[text.length()];
            for (int i = 0; i < text.length(); i++) {
                booleans[i] = random.nextDouble() > 0.5;
            }
            INFO_CACHE.put(text, booleans);
            for (int i = 0; i < text.length(); i++) {
                component = component.append(Component.literal(text.charAt(i) + "").withStyle(booleans[i] ^ invert ? SEABORN_LANGUAGE_INVERTED : SEABORN_LANGUAGE));
            }
        }
        return component;
    }

    public static MutableComponent seabornText(String text, Component realText) {
        return seabornText(text, false, realText);
    }

    public static MutableComponent seabornText(String text, boolean invert, Component realText) {
        if (MiscConfig.USE_SEABORN_LANGUAGE.get()) {
            return randomInvert(text, invert).withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, realText)));
        } else {
            return Component.literal("").append(realText);
        }
    }
}
