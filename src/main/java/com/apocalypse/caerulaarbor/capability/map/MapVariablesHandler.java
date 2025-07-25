package com.apocalypse.caerulaarbor.capability.map;

import com.apocalypse.caerulaarbor.client.font.ModFontHelper;
import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
import com.apocalypse.caerulaarbor.init.ModSounds;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;

public class MapVariablesHandler {

    /**
     * 增加策略的进化点数
     *
     * @param strategy 进化策略类型
     * @param level    实体所在的世界
     * @param point    待增加的点数
     */
    public static void addEvoPoint(MapVariables.StrategyType strategy, Level level, double point) {
        var mapVariables = MapVariables.get(level);
        int strategyLevel = mapVariables.getStrategyLevel(strategy);
        if (strategyLevel >= 4) return;

        if (strategy == MapVariables.StrategyType.SILENCE) {
            if (!mapVariables.enabledStrategySilence) return;
            if (mapVariables.strategyGrow < 4 || mapVariables.strategySubsisting < 4 || mapVariables.strategyBreed < 4 || mapVariables.strategyMigration < 4) {
                return;
            }
        }

        double currentPoint = mapVariables.getEvoPoint(strategy) + point;
        double required = Math.pow(strategyLevel + 1, 3) * GameplayConfig.EVOLUTION_POINT_COEFFICIENT.get();
        if (currentPoint >= (strategy == MapVariables.StrategyType.SILENCE ? required * 4 : required)) {
            upgradeStrategy(strategy, level);
        } else {
            mapVariables.setEvoPoint(strategy, currentPoint);
            mapVariables.syncData(level);
        }
    }

    public static boolean upgradeStrategy(MapVariables.StrategyType strategy, Level level) {
        var mapVariables = MapVariables.get(level);
        if (strategy == MapVariables.StrategyType.SILENCE && !mapVariables.enabledStrategySilence) return false;
        var currentLevel = mapVariables.getStrategyLevel(strategy);
        if (currentLevel >= 4) return false;
        return setStrategyLevel(strategy, currentLevel + 1, level);
    }

    // TODO 在这里添加一个进度的trigger
    public static boolean setStrategyLevel(MapVariables.StrategyType strategy, int strategyLevel, Level level) {
        var mapVariables = MapVariables.get(level);
        var currentLevel = mapVariables.getStrategyLevel(strategy);
        if (currentLevel == strategyLevel) {
            return false;
        }
        if (strategy == MapVariables.StrategyType.SILENCE && !mapVariables.enabledStrategySilence) return false;
        if (strategyLevel < 4 && strategy != MapVariables.StrategyType.SILENCE) {
            mapVariables.strategySilence = 0;
            mapVariables.evoPointSilence = 0;
        }
        switch (strategy) {
            case GROW -> {
                mapVariables.evoPointGrow = 0;
                mapVariables.strategyGrow = strategyLevel;
            }
            case SUBSISTING -> {
                mapVariables.evoPointSubsisting = 0;
                mapVariables.strategySubsisting = strategyLevel;
            }
            case BREED -> {
                mapVariables.evoPointBreed = 0;
                mapVariables.strategyBreed = strategyLevel;
            }
            case MIGRATION -> {
                mapVariables.evoPointMigration = 0;
                mapVariables.strategyMigration = strategyLevel;
            }
            case SILENCE -> {
                mapVariables.evoPointSilence = 0;
                mapVariables.strategySilence = strategyLevel;
            }
        }

        mapVariables.syncData(level);

        if (strategyLevel > 0) {
            displayUpgradeEffects(strategy, strategyLevel, level);
        }

        return true;
    }

    // TODO 补齐每种策略进化时的效果提示，静谧添加单独判定
    public static void displayUpgradeEffects(MapVariables.StrategyType strategy, int strategyLevel, Level level) {
        SoundEvent lowLevelSound = switch (strategy) {
            case GROW -> ModSounds.GROW1.get();
            case SUBSISTING -> ModSounds.SUBSISTING1.get();
            case BREED -> ModSounds.BREED1.get();
            case MIGRATION -> ModSounds.MIGRATION1.get();
            case SILENCE -> strategyLevel == 1 ? ModSounds.SILENCE1.get() : ModSounds.SILENCE2.get();
        };

        SoundEvent highLevelSound = switch (strategy) {
            case GROW -> ModSounds.GROW2.get();
            case SUBSISTING -> ModSounds.SUBSISTING2.get();
            case BREED -> ModSounds.BREED2.get();
            case MIGRATION -> ModSounds.MIGRATION2.get();
            case SILENCE -> strategyLevel == 3 ? ModSounds.SILENCE3.get() : ModSounds.SILENCE4.get();
        };

        for (var player : level.players()) {
            if (GameplayConfig.ENABLE_EVOLUTION_SOUND.get()) {
                if (!level.isClientSide()) {
                    level.playSound(null, player.getOnPos(), strategyLevel > 2 ? highLevelSound : lowLevelSound, SoundSource.NEUTRAL, 1, 1);
                } else {
                    level.playLocalSound(player.getX(), player.getY(), player.getZ(), strategyLevel > 2 ? highLevelSound : lowLevelSound, SoundSource.NEUTRAL, 1, 1, false);
                }
            }
            if (!level.isClientSide) {
                player.displayClientMessage(getEvoInfo(strategy, strategyLevel), false);
//                player.displayClientMessage(Component.translatable("des." + strategy.name + "_" + strategyLevel), false);
            }
        }
    }

    private static Component getEvoInfo(MapVariables.StrategyType type, int level) {
        int color = switch (level) {
            default -> 0xFFFFFF;
            case 2 -> 0x54CFCF;
            case 3 -> 0x50C2ED;
            case 4 -> 0x484CCE;
        };
        String levelText = switch (level) {
            default -> "0";
            case 1 -> "I";
            case 2 -> "II";
            case 3 -> "III";
            case 4 -> "IV";
        };
        return ModFontHelper.seabornText(getStrategyEvoInfo(type, level), level % 2 == 0,
                        Component.translatable("des.caerula_arbor.strategy.upgrade", Component.translatable(type.name), levelText))
                .withStyle(Style.EMPTY.withColor(color));
    }

    public static String getStrategyEvoInfo(MapVariables.StrategyType type, int level) {
        return switch (type) {
            case GROW -> switch (level) {
                default -> "Grow Embrace More";
                case 3, 4 -> "Grow Assimilate More";
            };
            case SUBSISTING -> switch (level) {
                default -> "Subsisting We the Anima";
                case 3, 4 -> "Subsisting We the Vicissitudes";
            };
            case BREED -> switch (level) {
                default -> "Breed Creed beyond our death";
                case 3, 4 -> "Breed Arise among the Colony";
            };
            case MIGRATION -> switch (level) {
                default -> "Migration Every trench we fill in";
                case 3, 4 -> "Migration In predation we proliferate";
            };
            case SILENCE -> switch (level) {
                default -> "Silence Now come with me";
                case 3, 4 -> "Silence Now come with me back to our eternal hometown";
            };
        };
    }
}