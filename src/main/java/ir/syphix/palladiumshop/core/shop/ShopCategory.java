package ir.syphix.palladiumshop.core.shop;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Map;

public class ShopCategory {
        String id;
        String displayName;

        Map<String, ShopItem> shopItems;

        public ShopCategory(String id, String displayName, List<ShopItem> shopItems) {
            this.id = id;
            this.displayName = displayName;
            for (ShopItem shopItem : shopItems) {
                addItem(shopItem);
            }
        }

        public void addItem(ShopItem shopItem) {
            shopItems.put(shopItem.material.name(), shopItem);
        }

        public ShopItem getItem(String id) {
            return shopItems.get(id);
        }

        public List<ShopItem> getItems() {
            return shopItems.values().stream().toList();
        }

        public static ShopCategory fromConfig(FileConfiguration config) {
            return new ShopCategory(
                    config.getString("id"),
                    config.getString("display-name"),
                    config.getConfigurationSection("shop-items")
                            .getKeys(false)
                            .stream()
                            .map(key -> ShopItem.fromConfig(config.getConfigurationSection("shop-items").getConfigurationSection(key)))
                            .toList()
            );
        }

}
