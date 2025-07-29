package com.apocalypse.caerulaarbor.mixin;

import com.apocalypse.caerulaarbor.client.component.ClientLanguageGetter;
import com.apocalypse.caerulaarbor.client.component.SeabornComponent;
import com.apocalypse.caerulaarbor.client.font.ModFontHelper;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {

    @ModifyArg(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;ILnet/minecraft/client/GuiMessageTag;Z)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/components/ComponentRenderUtils;wrapComponents(Lnet/minecraft/network/chat/FormattedText;ILnet/minecraft/client/gui/Font;)Ljava/util/List;"), index = 0)
    public FormattedText modifyComponent(FormattedText pComponent) {
        if (pComponent instanceof MutableComponent component
                && component.getContents() instanceof SeabornComponent seabornComponent
                && seabornComponent.useObfuscatedText
        ) {
            var key = seabornComponent.getKey();
            var enTranslation = ClientLanguageGetter.EN_US.getOrDefault(key, seabornComponent.getFallback() != null ? seabornComponent.getFallback() : key);
            return ModFontHelper.seabornText(enTranslation, component.getStyle(), seabornComponent.invert, Component.translatableWithFallback(key, seabornComponent.getFallback(), seabornComponent.getArgs()));
        }

        return pComponent;
    }
}
