package com.apocalypse.caerulaarbor.init;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import com.apocalypse.caerulaarbor.command.LowerCamelCaseEnumArgument;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCommandArguments {

    public static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGUMENT_TYPES = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, CaerulaArborMod.MODID);

    public static final RegistryObject<LowerCamelCaseEnumArgument.Info> LOWER_CAMEL_CASE_ENUM =
            COMMAND_ARGUMENT_TYPES.register("lower_camel_case_enum", () -> ArgumentTypeInfos.registerByClass(LowerCamelCaseEnumArgument.class, new LowerCamelCaseEnumArgument.Info()));
}
