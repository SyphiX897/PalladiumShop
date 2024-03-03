package ir.syphix.palladiumshop.gui;

import ir.syphix.palladiumshop.PalladiumShop;
import ir.syphix.palladiumshop.annotation.GuiHandler;
import ir.syphix.palladiumshop.core.CustomGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@GuiHandler
public class Ores extends CustomGui {

    public Ores() {
        super("ores", PalladiumShop.categoriesList.get("ores"), 54, "<aqua>Ores");

        addIngredient('B', new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), " ");
        addIngredient('G', new ItemStack(Material.LIME_STAINED_GLASS_PANE), " ");
        addShape("GGBBBBBGG", "G       G", "B       B", "B       B", "G       G", "GGBBBBBGG");
        completeGui();
        registerGui();
    }
}
