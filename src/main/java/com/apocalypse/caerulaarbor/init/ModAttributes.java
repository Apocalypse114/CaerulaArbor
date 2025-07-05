package com.apocalypse.caerulaarbor.init;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModAttributes {

    public static final DeferredRegister<Attribute> REGISTRY = DeferredRegister.create(ForgeRegistries.ATTRIBUTES, CaerulaArborMod.MODID);

    public static final RegistryObject<Attribute> SANITY_INJURY_RESISTANCE = REGISTRY.register("sanity_injury_resistance", () -> new RangedAttribute("attribute.caerula_arbor.sanity_injury_resistance", 0, 0, 100).setSyncable(true));
    public static final RegistryObject<Attribute> SANITY_REGENERATE = REGISTRY.register("sanity_regenerate", () -> new RangedAttribute("attribute.caerula_arbor.sanity_regenerate", 0, 0, 1000).setSyncable(true));
    public static final RegistryObject<Attribute> SANITY_RATE = REGISTRY.register("sanity_rate", () -> new RangedAttribute("attribute.caerula_arbor.sanity_rate", 0, 0, 999).setSyncable(true));
    public static final RegistryObject<Attribute> EVOLVED = REGISTRY.register("evolved", () -> new RangedAttribute("attribute.caerula_arbor.evolved", 0, 0, 1).setSyncable(true));
    public static final RegistryObject<Attribute> MISS_CHANCE = REGISTRY.register("miss_chance", () -> new RangedAttribute("attribute.caerula_arbor.miss_chance", 0, 0, 100).setSyncable(true));
    public static final RegistryObject<Attribute> SUMMONABLE = REGISTRY.register("summonable", () -> new RangedAttribute("attribute.caerula_arbor.summonable", 1, 0, 1).setSyncable(true));

    @SubscribeEvent
    public static void addAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entity -> {
            event.add(entity, SANITY_INJURY_RESISTANCE.get());
            event.add(entity, SANITY_REGENERATE.get());
            event.add(entity, SANITY_RATE.get());
            event.add(entity, EVOLVED.get());
            event.add(entity, MISS_CHANCE.get());
            event.add(entity, SUMMONABLE.get());
        });
    }

    @Mod.EventBusSubscriber
    public static class PlayerAttributesSync {
        @SubscribeEvent
        public static void playerClone(PlayerEvent.Clone event) {
            Player oldPlayer = event.getOriginal();
            Player newPlayer = event.getEntity();
            newPlayer.getAttribute(SANITY_INJURY_RESISTANCE.get()).setBaseValue(oldPlayer.getAttribute(SANITY_INJURY_RESISTANCE.get()).getBaseValue());
            newPlayer.getAttribute(SANITY_RATE.get()).setBaseValue(oldPlayer.getAttribute(SANITY_RATE.get()).getBaseValue());
            newPlayer.getAttribute(EVOLVED.get()).setBaseValue(oldPlayer.getAttribute(EVOLVED.get()).getBaseValue());
            newPlayer.getAttribute(MISS_CHANCE.get()).setBaseValue(oldPlayer.getAttribute(MISS_CHANCE.get()).getBaseValue());
            newPlayer.getAttribute(SUMMONABLE.get()).setBaseValue(oldPlayer.getAttribute(SUMMONABLE.get()).getBaseValue());
        }
    }
}
