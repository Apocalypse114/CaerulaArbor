package com.apocalypse.caerulaarbor.client.font;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.client.component.SeabornComponent;
import com.apocalypse.caerulaarbor.config.server.MiscConfig;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Objects;
import java.util.Random;
import java.util.zip.CRC32;

public class ModFontHelper {

    public static final Style SEABORN_LANGUAGE = Style.EMPTY.withFont(CaerulaArborMod.loc("seaborn_language"));
    public static final Style SEABORN_LANGUAGE_INVERTED = Style.EMPTY.withFont(CaerulaArborMod.loc("seaborn_language_inverted"));

    public static final HashMap<String, Boolean[]> INFO_CACHE = new HashMap<>();

    private static Random getRandom(String string) {
        CRC32 crc32 = new CRC32();
        crc32.update(string.getBytes());
        long seed = crc32.getValue();
        return new Random(seed);
    }

    public static MutableComponent randomInvert(String text) {
        return randomInvert(text, null, false);
    }

    public static MutableComponent randomInvert(String text, @Nullable Style defaultStyle, boolean invert) {
        Random random = getRandom(text);
        MutableComponent component = Component.literal("");
        if (INFO_CACHE.containsKey(text)) {
            for (int i = 0; i < text.length(); i++) {
                Component componentToAppend;
                if (defaultStyle != null) {
                    componentToAppend = Component.literal(text.charAt(i) + "")
                            .withStyle(defaultStyle)
                            .withStyle(INFO_CACHE.get(text)[i] ^ invert ? SEABORN_LANGUAGE_INVERTED : SEABORN_LANGUAGE);
                } else {
                    componentToAppend = Component.literal(text.charAt(i) + "")
                            .withStyle(INFO_CACHE.get(text)[i] ^ invert ? SEABORN_LANGUAGE_INVERTED : SEABORN_LANGUAGE);
                }

                component = component.append(componentToAppend);
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

    // 请在服务端发送消息给客户端玩家时使用该方法，客户端会自动替换为对应文本
    public static MutableComponent translatableSeaborn(String key, boolean obfuscated, boolean invert, Object... args) {
        return MutableComponent.create(new SeabornComponent(key, obfuscated, invert, null, args));
    }

    /**
     * 将指定字符串转换为海嗣文，应该在客户端进行调用
     *
     * @param text     目标字符串
     * @param realText 原文组件
     */
    public static MutableComponent seabornText(String text, @Nullable Style defaultStyle, Component realText) {
        return seabornText(text, defaultStyle, false, realText);
    }

    /**
     * 将指定字符串转换为海嗣文，应该在客户端进行调用
     */
    public static MutableComponent seabornText(String text, boolean invert, Component realText) {
        return seabornText(text, null, invert, realText);
    }

    public static MutableComponent seabornText(String text, @Nullable Style defaultStyle, boolean invert, Component realText) {
        if (MiscConfig.USE_SEABORN_LANGUAGE.get()) {
            defaultStyle = Objects.requireNonNullElse(defaultStyle, Style.EMPTY);
            return randomInvert(text, defaultStyle, invert).withStyle(defaultStyle.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, realText)));
        } else {
            return Component.literal("").append(realText);
        }
    }
}
