package ir.syphix.palladiumshop.core.shop;

import ir.syrent.origin.paper.Origin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ShopCategory {
        private final String id;
        private final String title;

        private final List<ShopItem> shopItemList = new ArrayList<>();
        public HashMap<String, ShopItem> shopItemHashMap = new HashMap<>();

        public ShopCategory(String id, String title, List<ShopItem> shopItems) {
            this.id = id;
            this.title = title;
            for (ShopItem shopItem : shopItems) {
                addItem(shopItem);
            }
        }

        public void addItem(ShopItem shopItem) {
            shopItemList.add(shopItem);
            shopItemHashMap.put(shopItem.displayName(), shopItem);
        }

        public List<ShopItem> items() {
            return shopItemList;
        }

        public String id() {
            return id;
        }

        public String title() { return title; }

        public static ShopCategory fromConfig(FileConfiguration config) {
            ConfigurationSection itemsSection = config.getConfigurationSection("items");
            if (itemsSection == null) return null;
            return new ShopCategory(
                    config.getString("id"),
                    config.getString("title"),
                    itemsSection
                            .getKeys(false)
                            .stream()
                            .map(key -> ShopItem.fromConfig(Objects.requireNonNull(itemsSection.getConfigurationSection(key))))
                            .toList()
            );
        }

}
