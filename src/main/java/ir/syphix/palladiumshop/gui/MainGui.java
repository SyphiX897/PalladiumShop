package ir.syphix.palladiumshop.gui;

import ir.syphix.palladiumshop.core.gui.CustomGui;
import ir.syrent.origin.paper.Origin;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class MainGui extends CustomGui implements InventoryHolder {

    private Inventory inventory;
    public MainGui() {
        super("main_gui", null, 54, Origin.getPlugin().getConfig().getString("main_gui.title"));
        ConfigurationSection mainGuiSection = Origin.getPlugin().getConfig().getConfigurationSection("main_gui");
        if (mainGuiSection == null) return;

        for (String ingredient : mainGuiSection.getStringList("ingredients")) {
            List<String> ingredientList = Arrays.stream(ingredient.split(";")).toList();
            char ch = ingredientList.get(0).charAt(0);
            ItemStack itemStack = new ItemStack(Material.valueOf(ingredientList.get(1)));
            glassShapeIngredient(ch, itemStack, " ");
        }
        glassShape(mainGuiSection.getStringList("shape"));

        ConfigurationSection categoriesSection = mainGuiSection.getConfigurationSection("categories");
        for (String category : categoriesSection.getKeys(false)) {
            addCategory(
                    category,
                    categoriesSection.getString(category + ".display_name"),
                    categoriesSection.getString(category + ".material"),
                    categoriesSection.getInt(category + ".slot")
            );
        }

        this.inventory = inventory();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
