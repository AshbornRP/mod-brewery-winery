package net.ruusika.brewerywinery.datagen;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.ruusika.brewerywinery.BreweryWinery;
import net.ruusika.brewerywinery.blocks.BeverageBlock;
import net.ruusika.brewerywinery.init.BreweryWineryBlocks;
import net.ruusika.brewerywinery.init.BreweryWineryItems;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BreweryWineryTranslationProvider extends FabricLanguageProvider {

    public BreweryWineryTranslationProvider(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {

        for (BeverageBlock entry : BreweryWineryBlocks.BEERS) {
            builder.add(entry, cleanString(Registries.BLOCK.getId(entry), true));
        }

        builder.add(BreweryWineryBlocks.SERVING_TRAY, cleanString(Registries.BLOCK.getId(BreweryWineryBlocks.SERVING_TRAY), false));
        builder.add(BreweryWineryBlocks.KEG_BLOCK, cleanString(Registries.BLOCK.getId(BreweryWineryBlocks.KEG_BLOCK), false));

        builder.add(BreweryWineryItems.HOPS, cleanString(Registries.ITEM.getId(BreweryWineryItems.HOPS), false));
        builder.add(BreweryWineryBlocks.HOPS_PLANT, cleanString(Registries.BLOCK.getId(BreweryWineryBlocks.HOPS_PLANT), false));
        builder.add(BreweryWineryItems.YEAST, cleanString(Registries.ITEM.getId(BreweryWineryItems.YEAST), false));

        builder.add(BreweryWineryItems.BROKEN_BOTTLE, cleanString(Registries.ITEM.getId(BreweryWineryItems.BROKEN_BOTTLE), false));

        builder.add("itemGroup.brewerywinery.brewery-winery", "Brewery & Winery");

        try {
            Path existingFilePath = this.dataOutput.getModContainer().findPath("assets/%s/lang/en_us_existing.json"
                    .formatted(BreweryWinery.MOD_ID)).orElse(null);

            if (existingFilePath != null) {
                builder.add(existingFilePath);
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to add existing language file!", e);
        }
    }

    private static String cleanString(Identifier name, boolean reverse) {
        List<String> input = List.of(name.getPath().split("/"));
        List<String> lastWords = Arrays.asList(input.get(input.size() - 1).split("_"));
        if (reverse) {
            Collections.reverse(lastWords);
        }

        StringBuilder output = new StringBuilder();

        for (int i = 0; i < lastWords.size(); i++) {
            String word = lastWords.get(i);
            char firstLetter = Character.toUpperCase(word.charAt(0));
            output.append(firstLetter).append(word.substring(1));
            if (i < lastWords.size() - 1) {
                output.append(" ");
            }
        }

        return output.toString();
    }
}
