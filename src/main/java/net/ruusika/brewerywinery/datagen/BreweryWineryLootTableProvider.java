package net.ruusika.brewerywinery.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.condition.BlockStatePropertyLootCondition;
import net.minecraft.loot.condition.InvertedLootCondition;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.SurvivesExplosionLootCondition;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.SetCountLootFunction;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.predicate.StatePredicate;
import net.ruusika.brewerywinery.blocks.HopsBlock;
import net.ruusika.brewerywinery.init.BreweryWineryBlocks;
import net.ruusika.brewerywinery.init.BreweryWineryItems;
import net.ruusika.brewerywinery.init.BreweryWineryProperties;

public class BreweryWineryLootTableProvider extends FabricBlockLootTableProvider {

    public BreweryWineryLootTableProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generate() {
        LootCondition.Builder hasPlant = BlockStatePropertyLootCondition.builder(BreweryWineryBlocks.HOPS_PLANT)
                .properties(StatePredicate.Builder.create().exactMatch(BreweryWineryProperties.HAS_PLANT, true));

        LootCondition.Builder isAge2 = BlockStatePropertyLootCondition.builder(BreweryWineryBlocks.HOPS_PLANT)
                .properties(StatePredicate.Builder.create().exactMatch(HopsBlock.HOP_AGE, 2));

        addDrop(BreweryWineryBlocks.HOPS_PLANT, block -> net.minecraft.loot.LootTable.builder()
                .pool(LootPool.builder()
                        .conditionally(SurvivesExplosionLootCondition.builder())
                        .conditionally(hasPlant)
                        .conditionally(InvertedLootCondition.builder(isAge2))
                        .with(ItemEntry.builder(BreweryWineryItems.HOPS)
                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0f)))))
                .pool(LootPool.builder()
                        .conditionally(SurvivesExplosionLootCondition.builder())
                        .conditionally(hasPlant)
                        .conditionally(isAge2)
                        .with(ItemEntry.builder(BreweryWineryItems.HOPS)
                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f)))))
        );
    }
}