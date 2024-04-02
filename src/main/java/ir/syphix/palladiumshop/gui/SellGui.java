package ir.syphix.palladiumshop.gui;

import ir.syphix.palladiumshop.core.gui.CustomGui;
import ir.syrent.origin.paper.Origin;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

public class SellGui extends CustomGui implements InventoryHolder {

    private final Inventory inventory;

    public SellGui() {
        super("sell_gui", null, 54, Origin.getPlugin().getConfig().getString("sell_gui.title"));
        FileConfiguration config = Origin.getPlugin().getConfig();

        for (String ingredient : config.getStringList("sell_gui.ingredients")) {
            List<String> ingredientList = Arrays.stream(ingredient.split(";")).toList();
            char ch = ingredientList.get(0).charAt(0);
            ItemStack itemStack = new ItemStack(Material.valueOf(ingredientList.get(1)));
            glassShapeIngredient(ch, itemStack, " ");
        }

        glassShape(config.getStringList("sell_gui.shape"));

        this.inventory = inventory();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
