package net.ruusika.brewerywinery;

import net.fabricmc.api.ModInitializer;

import net.minecraft.util.Identifier;
import net.ruusika.brewerywinery.init.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BreweryWinery implements ModInitializer {
	public static final String MOD_ID = "brewery-winery";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		BreweryWineryBlocks.initialize();
		BreweryWineryBlockEntities.initialize();
		BreweryWineryItems.initialize();
		BreweryWineryItemGroups.initialize();
		BreweryWineryProperties.initialize();
		LOGGER.info("DINONUGGETS!");
	}

	public static Identifier getId(String path) {
		return Identifier.of(MOD_ID, path);
	}
}