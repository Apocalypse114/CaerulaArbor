package com.apocalypse.caerulaarbor.client.font;

import com.apocalypse.caerulaarbor.client.component.ClientLanguageGetter;
import com.google.common.collect.Lists;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import org.jetbrains.annotations.NotNull;

public class MutableComponentWrapper extends MutableComponent {

    public MutableComponentWrapper(ComponentContents components) {
        super(components, Lists.newArrayList(), Style.EMPTY);
    }

    @NotNull
    @Override
    public FormattedCharSequence getVisualOrderText() {
        Language language = ClientLanguageGetter.EN_US;
        if (this.decomposedWith != language) {
            this.visualOrderText = language.getVisualOrder(this);
            this.decomposedWith = language;
        }

        return this.visualOrderText;
    }
}
