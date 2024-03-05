package ir.syphix.palladiumshop.core.shop;

import ir.syphix.palladiumshop.PalladiumShop;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ShopCategories {

        private static final HashMap<String, ShopCategory> categories = new HashMap<>();

        private static final List<File> categoryFiles = new ArrayList<>();

        public ShopCategories(List<FileConfiguration> configurations) {
            for (FileConfiguration configuration : configurations) {
                ShopCategory category = ShopCategory.fromConfig(configuration);
                addCategory(category);
            }
        }

        public static void addCategory(ShopCategory category) {
            categories.put(category.id, category);
        }

        public static ShopCategory getCategory(String id) {
            return categories.get(id);
        }

        public static List<ShopCategory> getCategories() {
            return categories.values().stream().toList();
        }

}
