package com.apocalypse.caerulaarbor.command;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import net.minecraft.commands.Commands;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ModCommands {

    @SubscribeEvent
    public static void registerCommand(RegisterCommandsEvent event) {
        var command = Commands.literal(CaerulaArborMod.MODID);
        command.then(EvolutionCommand.get());
        command.then(AttributeCommand.get());
        command.then(RejectionCommand.get());

        event.getDispatcher().register(command);
    }
}
