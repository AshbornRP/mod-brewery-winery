package net.ruusika.brewerywinery.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.ruusika.brewerywinery.util.BreweryWineryTags;

import java.util.concurrent.CompletableFuture;

public class BreweryWineryTagsProvider {
    public static class BWBlockTags extends FabricTagProvider.BlockTagProvider {

        public BWBlockTags(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(dataOutput, registriesFuture);
        }

        @Override
        protected void configure(RegistryWrapper.WrapperLookup wrapperLookup) {
            getOrCreateTagBuilder(BreweryWineryTags.Blocks.HOPS_PILLAR).addOptionalTag(BlockTags.FENCES);
            getOrCreateTagBuilder(BreweryWineryTags.Blocks.HEAT_BLOCKS).add(Blocks.MAGMA_BLOCK).add(Blocks.LAVA_CAULDRON);
        }
    }

    public static class BWItemTags extends FabricTagProvider.ItemTagProvider {
        public BWItemTags(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
            super(output, registriesFuture);
        }

        protected void configure(RegistryWrapper.WrapperLookup registries) {
            getOrCreateTagBuilder(BreweryWineryTags.Items.BEER_BASE_WATER).add(Items.WATER_BUCKET).add(Items.POTION);
            getOrCreateTagBuilder(BreweryWineryTags.Items.BEER_ADDITIVES_CROPS).add(Items.WHEAT).add(Items.BEETROOT)
                    .add(Items.CARROT).add(Items.POTATO);
            getOrCreateTagBuilder(BreweryWineryTags.Items.BEER_ADDITIVES_EDIBLE_NON_MEAT).add(Items.APPLE)
                    .add(Items.SWEET_BERRIES).add(Items.GLOW_BERRIES).add(Items.MELON_SLICE).add(Items.PUMPKIN_PIE)
                    .add(Items.COOKIE).add(Items.BREAD).add(Items.BAKED_POTATO).add(Items.BEETROOT_SOUP)
                    .add(Items.DRIED_KELP);
            getOrCreateTagBuilder(BreweryWineryTags.Items.BEER_ADDITIVES_MEAT).add(Items.BEEF).add(Items.COOKED_BEEF)
                    .add(Items.PORKCHOP).add(Items.COOKED_PORKCHOP).add(Items.CHICKEN).add(Items.COOKED_CHICKEN)
                    .add(Items.MUTTON).add(Items.COOKED_MUTTON).add(Items.RABBIT).add(Items.COOKED_RABBIT)
                    .addOptionalTag(ItemTags.FISHES);
            getOrCreateTagBuilder(BreweryWineryTags.Items.BEER_ADDITIVES_WEIRD_MISC).add(Items.BONE)
                    .add(Items.VINE).add(Items.BONE).add(Items.FERMENTED_SPIDER_EYE);
        }
    }
}
