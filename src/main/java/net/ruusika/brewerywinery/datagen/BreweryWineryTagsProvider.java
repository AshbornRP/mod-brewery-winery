package net.ruusika.brewerywinery.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Blocks;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.BlockTags;
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
}
