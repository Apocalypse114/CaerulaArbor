package com.apocalypse.caerulaarbor.mixin;

import com.apocalypse.caerulaarbor.client.component.ClientLanguageGetter;
import com.apocalypse.caerulaarbor.client.font.ModFontHelper;
import com.apocalypse.caerulaarbor.client.font.MutableComponentWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.LiteralContents;
import net.minecraftforge.fml.util.thread.SidedThreadGroups;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MutableComponent.class)
public class MutableComponentMixin {
    @Inject(method = "create", at = @At("RETURN"), cancellable = true)
    private static void modTranslatable(ComponentContents pContents, CallbackInfoReturnable<MutableComponent> cir) {
        // 可能不是很可靠，但是也没别的办法
        if (ClientLanguageGetter.EN_US == null || Thread.currentThread().getThreadGroup() == SidedThreadGroups.SERVER
                || Minecraft.getInstance().player == null) {
            return;
        }

        var mutableComponent = cir.getReturnValue();
        // literal的就不去换他了，不然问题太多了）
        if (mutableComponent.getContents() instanceof LiteralContents) {
            return;
        }
        try {
            var wrapper = new MutableComponentWrapper(mutableComponent.getContents());
            var raw = wrapper.getString();
            // 将非 ASCII 英文和非数字的每个字符映射为单个英文字母（基于 code point 的哈希）
            var mappedRaw = caerulaArbor$mapNonAlnum(raw);

            var replace = ModFontHelper.seabornText(mappedRaw, mutableComponent.getStyle(), true, mutableComponent);
            cir.setReturnValue(replace);
        } catch (Exception ignore) {}
    }

    @Unique
    private static String caerulaArbor$mapNonAlnum(String s) {
        if (s == null || s.isEmpty()) return s;
        StringBuilder sb = new StringBuilder(s.length());
        int i = 0;
        while (i < s.length()) {
            int cp = s.codePointAt(i);
            i += Character.charCount(cp);
            boolean isAsciiLetter = (cp >= 'A' && cp <= 'Z') || (cp >= 'a' && cp <= 'z');
            boolean isDigit = (cp >= '0' && cp <= '9');
            if (isAsciiLetter || isDigit) {
                sb.appendCodePoint(cp);
            } else {
                int h = Integer.hashCode(cp);
                char mapped = (char) ('a' + Math.floorMod(h, 26));
                sb.append(mapped);
            }
        }
        return sb.toString();
    }
}
