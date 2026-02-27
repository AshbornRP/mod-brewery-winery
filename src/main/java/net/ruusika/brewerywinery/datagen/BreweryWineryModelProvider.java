package net.ruusika.brewerywinery.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.block.Block;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;
import net.ruusika.brewerywinery.BreweryWinery;
import net.ruusika.brewerywinery.blocks.BeverageBlock;
import net.ruusika.brewerywinery.blocks.HopsBlock;
import net.ruusika.brewerywinery.blocks.KegBlock;
import net.ruusika.brewerywinery.datagen.util.ParentModelSupplier;
import net.ruusika.brewerywinery.init.BreweryWineryBlocks;

public class BreweryWineryModelProvider extends FabricModelProvider {

    public BreweryWineryModelProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {

        //BEVERAGES
        generateDefaultNorthFacingBlock(blockStateModelGenerator, BreweryWineryBlocks.LAGER_BEER, "beer_lager_block");
        generateDefaultNorthFacingBlock(blockStateModelGenerator, BreweryWineryBlocks.CRAFT_BEER, "beer_craft_block");
        generateDefaultNorthFacingBlock(blockStateModelGenerator, BreweryWineryBlocks.WEIRD_BEER, "beer_weird_block");

        generateDefaultNorthFacingBlock(blockStateModelGenerator, BreweryWineryBlocks.LAGER_BEER_LARGE, "beer_lager_large_block");
        generateDefaultNorthFacingBlock(blockStateModelGenerator, BreweryWineryBlocks.CRAFT_BEER_LARGE, "beer_craft_large_block");
        generateDefaultNorthFacingBlock(blockStateModelGenerator, BreweryWineryBlocks.WEIRD_BEER_LARGE, "beer_weird_large_block");

        generateDefaultNorthFacingBlock(blockStateModelGenerator, BreweryWineryBlocks.RED_WINE, "wine_red_block");
        generateDefaultNorthFacingBlock(blockStateModelGenerator, BreweryWineryBlocks.WHITE_WINE, "wine_white_block");
        generateDefaultNorthFacingBlock(blockStateModelGenerator, BreweryWineryBlocks.ROSE_WINE, "wine_rose_block");

        generateDefaultNorthFacingBlock(blockStateModelGenerator, BreweryWineryBlocks.MEAD, "mead_block");
        generateDefaultNorthFacingBlock(blockStateModelGenerator, BreweryWineryBlocks.CIDER, "cider_block");

        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier.create(BreweryWineryBlocks.KEG_BLOCK)
                .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates())
                .coordinate(createIsFinishedMap()));

        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator
                .createSingletonBlockState(BreweryWineryBlocks.SERVING_TRAY,
                        new Identifier(BreweryWinery.MOD_ID, "block/serving_tray_block")));


        for (BeverageBlock entry : BreweryWineryBlocks.BEERS) {

            String parentPath = "block/mug_" + entry.getSize().getName() + "_block";
            String texturePath = "block/mug_" + entry.getSize().getName() + "_" + entry.getColor().getName();
            generateCustomItemModel(blockStateModelGenerator, entry, parentPath, texturePath, "block/glass");
        }

        //SERVING TRAY
        generateCustomItemModel(blockStateModelGenerator, BreweryWineryBlocks.SERVING_TRAY,
                "block/serving_tray_block",
                "block/serving_tray_block",
                "block/stripped_spruce_log");

        //HOPS PLANT
        Identifier stageBase = new Identifier(BreweryWinery.MOD_ID, "block/hop_lvl1");
        Identifier stageOne = new Identifier(BreweryWinery.MOD_ID, "block/hop_lvl2_top");
        Identifier stageTwo = new Identifier(BreweryWinery.MOD_ID, "block/hop_lvl3_top");

        blockStateModelGenerator.blockStateCollector.accept(VariantsBlockStateSupplier
                .create(BreweryWineryBlocks.HOPS_PLANT).coordinate(BlockStateVariantMap.create(HopsBlock.HOP_AGE)
                        .register(0, BlockStateVariant.create().put(VariantSettings.MODEL, stageBase))
                        .register(1, BlockStateVariant.create().put(VariantSettings.MODEL, stageOne))
                        .register(2, BlockStateVariant.create().put(VariantSettings.MODEL, stageTwo))
                ));


    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {

    }

    private static void generateCustomItemModel(BlockStateModelGenerator generator, Block block, String parentPath, String texturePath, String particlePath) {
        generator.excludeFromSimpleItemModelGeneration(block);
        generator.modelCollector.accept(ModelIds.getItemModelId(block.asItem()),
                new ParentModelSupplier(
                        new Identifier(BreweryWinery.MOD_ID, parentPath),
                        new Identifier(BreweryWinery.MOD_ID, texturePath),
                        new Identifier(particlePath)
                ));
    }

    private static void generateDefaultNorthFacingBlock(BlockStateModelGenerator blockStateModelGenerator, Block block, String name) {
        blockStateModelGenerator.blockStateCollector.accept(
                VariantsBlockStateSupplier.create(block, BlockStateVariant.create()
                                .put(VariantSettings.MODEL, new Identifier(BreweryWinery.MOD_ID, "block/" + name)))
                        .coordinate(BlockStateModelGenerator.createNorthDefaultHorizontalRotationStates()));
    }

    private static BlockStateVariantMap createIsFinishedMap() {
        return BlockStateVariantMap.create(KegBlock.IS_FINISHED).register(aBoolean -> {
            String name = "block/keg_block";
            if (aBoolean) {
                name += "_finished";
            }
            return BlockStateVariant.create().put(VariantSettings.MODEL, new Identifier(BreweryWinery.MOD_ID, name));
        });
    }
}
