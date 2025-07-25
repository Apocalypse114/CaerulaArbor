package com.apocalypse.caerulaarbor.config;

import com.apocalypse.caerulaarbor.config.server.MiscConfig;
import net.minecraftforge.common.ForgeConfigSpec;

public class ServerConfig {

    public static ForgeConfigSpec init() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();

        MiscConfig.init(builder);

        return builder.build();
    }
}
