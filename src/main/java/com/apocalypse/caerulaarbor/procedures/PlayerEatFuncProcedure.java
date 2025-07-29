package com.apocalypse.caerulaarbor.procedures;

import com.apocalypse.caerulaarbor.capability.ModCapabilities;
import com.apocalypse.caerulaarbor.config.common.GameplayConfig;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;

@Mod.EventBusSubscriber
public class PlayerEatFuncProcedure {
    @SubscribeEvent
    public static void onUseItemFinish(LivingEntityUseItemEvent.Finish event) {
        if (event != null && event.getEntity() != null) {
            execute(event, event.getEntity(), event.getItem());
        }
    }

    public static void execute(Entity entity, ItemStack itemstack) {
        execute(null, entity, itemstack);
    }

    private static void execute(@Nullable Event event, Entity entity, ItemStack itemstack) {
        if (entity == null)
            return;
        String str;
        double least;
        double most;
        double index;
        for (String stringiterator : GameplayConfig.LIGHTS_RECOVERY_FOODS.get()) {
            index = stringiterator.indexOf(", ");
            str = stringiterator.substring(0, (int) index);
            if ((ForgeRegistries.ITEMS.getKey(itemstack.getItem()).toString()).equals(str)) {
                str = stringiterator.substring((int) (index + 2));
                index = str.indexOf("/");
                least = new Object() {
                    double convert(String s) {
                        try {
                            return Double.parseDouble(s.trim());
                        } catch (Exception e) {
                        }
                        return 0;
                    }
                }.convert(str.substring(0, (int) index));
                most = new Object() {
                    double convert(String s) {
                        try {
                            return Double.parseDouble(s.trim());
                        } catch (Exception ignored) {
                        }
                        return 0;
                    }
                }.convert(str.substring((int) (index + 1)));

                var cap = ModCapabilities.getPlayerVariables(entity);
                cap.light = Math.min(100, cap.light + Mth.nextInt(RandomSource.create(), (int) least, (int) most));
                cap.syncPlayerVariables(entity);
            }
        }
    }
}
