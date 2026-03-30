package net.ruusika.brewerywinery.init;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import net.ruusika.brewerywinery.BreweryWinery;

public class BreweryWineryItemGroups {
    public static final RegistryKey<ItemGroup> BREWERY_WINERY = register("brewery-winery",
            FabricItemGroup.builder().icon(() -> new ItemStack(BreweryWineryItems.LAGER_BEER))
                    .displayName(Text.translatable("itemGroup.brewerywinery.brewery-winery")).build());


    @SuppressWarnings("SameParameterValue")
    private static RegistryKey<ItemGroup> register(String name, ItemGroup group) {
        Registry.register(Registries.ITEM_GROUP, BreweryWinery.getId(name), group);
        return RegistryKey.of(Registries.ITEM_GROUP.getKey(), BreweryWinery.getId(name));
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(BREWERY_WINERY).register(entries -> {
            for (Item registeredItem : BreweryWineryItems.ALL_ITEMS) {
                entries.add(registeredItem.getDefaultStack());
            }
        });
    }
}
