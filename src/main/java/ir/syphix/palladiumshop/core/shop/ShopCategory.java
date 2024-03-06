package ir.syphix.palladiumshop.core.shop;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ShopCategory {
        String id;

        private final List<ShopItem> shopItemList = new ArrayList<>();
        public HashMap<String, ShopItem> shopItemsHashMap = new HashMap<>();

        public ShopCategory(String id, List<ShopItem> shopItems) {
            this.id = id;
            for (ShopItem shopItem : shopItems) {
                addItem(shopItem);
            }
        }

        public void addItem(ShopItem shopItem) {
            shopItemList.add(shopItem);
            shopItemsHashMap.put(shopItem.displayName(), shopItem);
        }

        public List<ShopItem> items() {
            return shopItemList;
        }

        public static ShopCategory fromConfig(FileConfiguration config) {
            ConfigurationSection itemsSection = config.getConfigurationSection("items");
            if (itemsSection == null) return null;
            return new ShopCategory(
                    config.getString("id"),
                    itemsSection
                            .getKeys(false)
                            .stream()
                            .map(key -> ShopItem.fromConfig(Objects.requireNonNull(itemsSection.getConfigurationSection(key))))
                            .toList()
            );
        }

}
