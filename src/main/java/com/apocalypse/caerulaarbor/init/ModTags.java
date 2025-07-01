package com.apocalypse.caerulaarbor.init;

import com.apocalypse.caerulaarbor.CaerulaArborMod;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {

    public static class Items {

        public static final TagKey<Item> RELICS = tag("relics");
        public static final TagKey<Item> HAND_RELICS = tag("relics/hand");
        public static final TagKey<Item> KING_RELICS = tag("relics/king");
        public static final TagKey<Item> CURSED_RELICS = tag("relics/cursed");
        public static final TagKey<Item> ARCHFIEND_RELICS = tag("relics/archfiend");

        public static final TagKey<Item> SELF_MENDABLE = tag("self_mendable");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(CaerulaArborMod.loc(name));
        }
    }

    public static class Blocks {
        public static final TagKey<Block> ERRODABLE = tag("errodable");
        private static TagKey<Block> tag(String name) {
            return BlockTags.create(CaerulaArborMod.loc(name));
        }
    }
}
