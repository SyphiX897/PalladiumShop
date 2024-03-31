package ir.syphix.palladiumshop.core.shop;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

public class ShopCategory {
        private final String id;
        private final String title;

        public LinkedHashMap<String, ShopItem> shopItems = new LinkedHashMap<>();

        public ShopCategory(String id, String title, List<ShopItem> shopItems) {
            this.id = id;
            this.title = title;
            for (ShopItem shopItem : shopItems) {
                addItem(shopItem);
            }
        }

        public void addItem(ShopItem shopItem) {
            shopItems.put(shopItem.displayName(), shopItem);
        }

        public List<ShopItem> items() {
            return shopItems.values().stream().toList();
        }

        public String id() {
            return id;
        }

        public String title() { return title; }

        public static ShopCategory fromConfig(FileConfiguration config) {
            String id = config.getString("id");
            ConfigurationSection itemsSection = config.getConfigurationSection("items");
            if (itemsSection == null) return null;
            List<ShopItem> shopItemList = new ArrayList<>();
            for (String key : itemsSection.getKeys(false)) {
                ShopItem shopItem = ShopItem.fromConfig(id, itemsSection.getConfigurationSection(key));
                if (shopItem == null) continue;
                shopItemList.add(shopItem);
            }
            return new ShopCategory(
                    config.getString("id"),
                    config.getString("title", " "),
                    shopItemList
            );
        }

}
