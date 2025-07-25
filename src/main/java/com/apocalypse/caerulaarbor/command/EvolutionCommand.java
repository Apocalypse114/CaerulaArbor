package com.apocalypse.caerulaarbor.command;

import com.apocalypse.caerulaarbor.capability.map.MapVariables;
import com.apocalypse.caerulaarbor.capability.map.MapVariablesHandler;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.Component;

public class EvolutionCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal("evolution").requires(s -> s.hasPermission(2))
                .then(Commands.literal("upgrade").then(Commands.argument("strategy", LowerCamelCaseEnumArgument.enumArgument(MapVariables.StrategyType.class)).executes(arguments -> {
                    var strategy = arguments.getArgument("strategy", MapVariables.StrategyType.class);
                    var level = arguments.getSource().getUnsidedLevel();
                    if (MapVariablesHandler.upgradeStrategy(strategy, level)) {
                        arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.evolution.upgrade.success", Component.translatable(strategy.name)), true);
                        return 0;
                    }
                    arguments.getSource().sendFailure(Component.translatable("command.caerula_arbor.evolution.upgrade.failure", Component.translatable(strategy.name)));
                    return 0;
                })))
                .then(Commands.literal("set").then(Commands.argument("strategy", LowerCamelCaseEnumArgument.enumArgument(MapVariables.StrategyType.class)).then(Commands.argument("level", IntegerArgumentType.integer(0, 4)).executes(arguments -> {
                    var strategy = arguments.getArgument("strategy", MapVariables.StrategyType.class);
                    int strategyLevel = IntegerArgumentType.getInteger(arguments, "level");
                    var level = arguments.getSource().getUnsidedLevel();
                    String levelText = switch (strategyLevel) {
                        default -> "0";
                        case 1 -> "I";
                        case 2 -> "II";
                        case 3 -> "III";
                        case 4 -> "IV";
                    };
                    if (MapVariablesHandler.setStrategyLevel(strategy, strategyLevel, level)) {
                        arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.evolution.set.success", Component.translatable(strategy.name), levelText), true);
                        return 0;
                    }
                    arguments.getSource().sendFailure(Component.translatable("command.caerula_arbor.evolution.set.failure", Component.translatable(strategy.name)));
                    return 0;
                }))));
    }
}
