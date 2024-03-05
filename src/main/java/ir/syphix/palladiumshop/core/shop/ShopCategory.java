package ir.syphix.palladiumshop.core.shop;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopCategory {
        String id;

        HashMap<String, ShopItem> shopItems;

        public ShopCategory(String id, List<ShopItem> shopItems) {
            this.id = id;
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
                    config.getConfigurationSection("items")
                            .getKeys(false)
                            .stream()
                            .map(key -> ShopItem.fromConfig(config.getConfigurationSection("items").getConfigurationSection(key)))
                            .toList()
            );
        }

}
