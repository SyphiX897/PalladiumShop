package ir.syphix.palladiumshop.gui;

import ir.syphix.palladiumshop.annotation.AutoInitializer;
import ir.syphix.palladiumshop.core.gui.CustomGui;
import ir.syphix.palladiumshop.core.shop.ShopCategories;
import ir.syphix.palladiumshop.core.shop.ShopCategory;
import ir.syrent.origin.paper.Origin;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
@AutoInitializer
public class ShopGuis {

    public ShopGuis() {
        FileConfiguration mainConfig = Origin.getPlugin().getConfig();
        for (ShopCategory shopCategory : ShopCategories.getCategories()) {
            String id = shopCategory.id();
            List<String> ingredients = mainConfig.getStringList("shop.ingredients");
            List<String> shape = mainConfig.getStringList("shop.shape");

            var customGui = new CustomGui(id, shopCategory, 54, shopCategory.title());

            for (String ingredient : ingredients) {
                List<String> ingredientList = Arrays.stream(ingredient.split(";")).toList();
                char ch = ingredientList.get(0).charAt(0);
                ItemStack itemStack = new ItemStack(Material.valueOf(ingredientList.get(1)));
                customGui.glassShapeIngredient(ch, itemStack, " ");
            }
            customGui.glassShape(shape);
            customGui.completeGui();
            customGui.registerGui();
        }
    }
}
