package ir.syphix.palladiumshop.gui;

import ir.syphix.palladiumshop.annotation.AutoInitializer;
import ir.syphix.palladiumshop.core.gui.CustomGui;
import ir.syrent.origin.paper.Origin;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
@AutoInitializer
public class SellGui extends CustomGui implements InventoryHolder {

    private final Inventory inventory;

    public SellGui() {
        super("sell_gui", null, 54, "<#00aeff>Sell Menu");

        glassShapeIngredient('B', new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), " ");
        glassShapeIngredient('G', new ItemStack(Material.LIME_STAINED_GLASS_PANE), " ");
        glassShape(Origin.getPlugin().getConfig().getStringList("shop.shape"));

        this.inventory = inventory();
    }

    @Override
    public @NotNull Inventory getInventory() {
        return inventory;
    }
}
