package com.apocalypse.caerulaarbor.command;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;

import java.util.Collection;

public class AttributeCommand {

    public static LiteralArgumentBuilder<CommandSourceStack> get() {
        return Commands.literal("attribute").requires(s -> s.hasPermission(2))
                .then(Commands.argument("player", EntityArgument.players())
                        .then(Commands.literal("life").then(Commands.argument("value", IntegerArgumentType.integer(0, 325))
                                .executes(arguments -> {
                                    var players = EntityArgument.getPlayers(arguments, "player");
                                    int value = IntegerArgumentType.getInteger(arguments, "value");

                                    for (var player : players) {
                                        var variables = ModCapabilities.getPlayerVariables(player);
                                        variables.life = Math.min(value, variables.maxLive);
                                        variables.syncPlayerVariables(player);
                                    }

                                    if (players.size() == 1) {
                                        arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.attribute.life.single", players.iterator().next().getDisplayName(), value), true);
                                    } else {
                                        arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.attribute.life.multiple", players.size(), value), true);
                                    }
                                    return 0;
                                })))
                        .then(Commands.literal("maxLife").then(Commands.argument("value", IntegerArgumentType.integer(0, 325))
                                .executes(arguments -> {
                                    var players = EntityArgument.getPlayers(arguments, "player");
                                    int value = IntegerArgumentType.getInteger(arguments, "value");

                                    for (var player : players) {
                                        var variables = ModCapabilities.getPlayerVariables(player);
                                        variables.maxLive = value;
                                        variables.syncPlayerVariables(player);
                                    }

                                    if (players.size() == 1) {
                                        arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.attribute.max_life.single", players.iterator().next().getDisplayName(), value), true);
                                    } else {
                                        arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.attribute.max_life.multiple", players.size(), value), true);
                                    }
                                    return 0;
                                })))
                        .then(Commands.literal("shield").then(Commands.argument("value", IntegerArgumentType.integer(0, Integer.MAX_VALUE))
                                .executes(arguments -> {
                                    var players = EntityArgument.getPlayers(arguments, "player");
                                    int value = IntegerArgumentType.getInteger(arguments, "value");

                                    for (var player : players) {
                                        var variables = ModCapabilities.getPlayerVariables(player);
                                        variables.shield = value;
                                        variables.syncPlayerVariables(player);
                                    }

                                    if (players.size() == 1) {
                                        arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.attribute.shield.single", players.iterator().next().getDisplayName(), value), true);
                                    } else {
                                        arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.attribute.shield.multiple", players.size(), value), true);
                                    }
                                    return 0;
                                })))
                        .then(Commands.literal("light")
                                .then(Commands.argument("value", DoubleArgumentType.doubleArg(0, 100.0D))
                                        .executes(arguments -> {
                                            var players = EntityArgument.getPlayers(arguments, "player");
                                            double value = DoubleArgumentType.getDouble(arguments, "value");
                                            setLight(players, value);
                                            if (players.size() == 1) {
                                                arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.attribute.light.single", players.iterator().next().getDisplayName(), value), true);
                                            } else {
                                                arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.attribute.light.multiple", players.size(), value), true);
                                            }
                                            return 0;
                                        }))
                                .then(Commands.argument("state", LowerCamelCaseEnumArgument.enumArgument(LightState.class))
                                        .executes(arguments -> {
                                            var players = EntityArgument.getPlayers(arguments, "player");
                                            var state = arguments.getArgument("state", LightState.class);
                                            setLight(players, state.value);
                                            if (players.size() == 1) {
                                                arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.attribute.light.single", players.iterator().next().getDisplayName(), Component.translatable(state.name)), true);
                                            } else {
                                                arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.attribute.light.multiple", players.size(), Component.translatable(state.name)), true);
                                            }
                                            return 0;
                                        })))
                        .then(Commands.literal("seabornization").then(Commands.argument("value", IntegerArgumentType.integer(0, 3))
                                .executes(arguments -> {
                                    var players = EntityArgument.getPlayers(arguments, "player");
                                    int value = IntegerArgumentType.getInteger(arguments, "value");

                                    for (var player : players) {
                                        var variables = ModCapabilities.getPlayerVariables(player);
                                        variables.seabornization = value;
                                        variables.syncPlayerVariables(player);
                                    }

                                    if (players.size() == 1) {
                                        arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.attribute.seabornization.single", players.iterator().next().getDisplayName(), value), true);
                                    } else {
                                        arguments.getSource().sendSuccess(() -> Component.translatable("command.caerula_arbor.attribute.seabornization.multiple", players.size(), value), true);
                                    }
                                    return 0;
                                })))
                );
    }

    private static void setLight(Collection<ServerPlayer> players, double value) {
        for (var player : players) {
            var variables = ModCapabilities.getPlayerVariables(player);
            variables.light = value;
            variables.syncPlayerVariables(player);
        }
    }

    public enum LightState {
        LUMINOUS(100, "caerula_arbor.light.luminous"),
        FLICKERING(85, "caerula_arbor.light.flickering"),
        DIM(50, "caerula_arbor.light.dim"),
        EXTINGUISHED(0, "caerula_arbor.light.extinguished");

        public final int value;
        public final String name;

        LightState(int value, String name) {
            this.value = value;
            this.name = name;
        }
    }
}
