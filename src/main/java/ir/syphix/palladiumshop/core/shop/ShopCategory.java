package ir.syphix.palladiumshop.core.shop;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ShopCategory {
        String id;

        HashMap<String, ShopItem> shopItems = new HashMap<>();
        public List<ShopItem> shopItemList = new ArrayList<>();

        public ShopCategory(String id, List<ShopItem> shopItems) {
            this.id = id;
            for (ShopItem shopItem : shopItems) {
                shopItemList.add(shopItem);
                addItem(shopItem);
            }
        }

        public void addItem(ShopItem shopItem) {
            shopItems.put(shopItem.displayName, shopItem);
        }

//        public ShopItem getItem(String id) {
//            return shopItems.get(id);
//        }

        public List<ShopItem> items() {
            return shopItems.values().stream().toList();
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
