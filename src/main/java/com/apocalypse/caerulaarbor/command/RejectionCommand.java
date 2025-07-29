package com.apocalypse.caerulaarbor.command;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.capability.player.PlayerVariable;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;

public class RejectionCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal("rejection").requires(s -> s.hasPermission(2)).then(Commands.argument("player", EntityArgument.players())
                .then(Commands.argument("rejection", LowerCamelCaseEnumArgument.enumArgument(PlayerVariable.Rejection.class)).then(Commands.argument("invoke", BoolArgumentType.bool())
                        .executes(arguments -> {
                            var players = EntityArgument.getPlayers(arguments, "player");
                            var rejection = arguments.getArgument("rejection", PlayerVariable.Rejection.class);
                            var invoke = BoolArgumentType.getBool(arguments, "invoke");

                            for (var player : players) {
                                var variables = ModCapabilities.getPlayerVariables(player);
                                variables.setRejection(rejection, invoke);
                                variables.syncPlayerVariables(player);
                            }

                            if (players.size() == 1) {
                                arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.rejection.single." + (invoke ? "enabled" : "disabled"),
                                        players.iterator().next().getDisplayName(), Component.translatable(rejection.name)), true);
                            } else {
                                arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.rejection.multiple." + (invoke ? "enabled" : "disabled"),
                                        players.size(), Component.translatable(rejection.name)), true);
                            }

                            return 0;
                        }))
                )
        );
    }
}
