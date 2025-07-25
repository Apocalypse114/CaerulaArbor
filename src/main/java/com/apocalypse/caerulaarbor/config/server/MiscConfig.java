package com.apocalypse.caerulaarbor.config.server;

import net.minecraftforge.common.ForgeConfigSpec;

public class MiscConfig {

    public static ForgeConfigSpec.BooleanValue USE_SEABORN_LANGUAGE;

    public static void init(ForgeConfigSpec.Builder builder) {
        builder.push("misc");

        builder.comment("Set true to use the seaborn language");
        USE_SEABORN_LANGUAGE = builder.define("use_seaborn_language", true);

        builder.pop();
    }
}
