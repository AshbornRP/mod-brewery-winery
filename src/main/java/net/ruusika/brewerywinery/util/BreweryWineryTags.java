package net.ruusika.brewerywinery.util;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import net.ruusika.brewerywinery.BreweryWinery;

public class BreweryWineryTags {
    public static class Blocks {
       public static final TagKey<Block> HEAT_BLOCKS = createTag("heat_blocks");
       public static final TagKey<Block> HOPS_PILLAR = createTag("hops_pillar");

       private static TagKey<Block> createTag(String name) {
           return TagKey.of(RegistryKeys.BLOCK, new Identifier(BreweryWinery.MOD_ID, name));
        }

    }

    public static final class Items {
        public static final TagKey<Item> BEER_ADDITIVES_CROPS = createTag("beer_additives/crops");
        public static final TagKey<Item> BEER_ADDITIVES_EDIBLE_NON_MEAT = createTag("beer_additives/edible_non_meat");
        public static final TagKey<Item> BEER_ADDITIVES_MEAT = createTag("beer_additives/meat");
        public static final TagKey<Item> BEER_ADDITIVES_MONSTER_FOOD = createTag("beer_additives/monster_food");
        public static final TagKey<Item> BEER_ADDITIVES_WEIRD_MISC = createTag("beer_additives/weird_misc");

        private static TagKey<Item> createTag(String name) {
            return TagKey.of(RegistryKeys.ITEM, new Identifier(BreweryWinery.MOD_ID, name));
        }
    }
}
