package ir.syphix.palladiumshop.gui;

import ir.syphix.palladiumshop.annotation.GuiHandler;
import ir.syphix.palladiumshop.core.gui.CustomGui;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@GuiHandler
public class SellGui extends CustomGui {

    public SellGui() {
        super("sell_gui", 54, "<#00aeff>Sell Gui");

        glassShapeIngredient('B', new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), " ");
        glassShapeIngredient('G', new ItemStack(Material.LIME_STAINED_GLASS_PANE), " ");
        glassShape("GGBBBBBGG", "G       G", "B       B", "B       B", "G       G", "GGBBBBBGG");
        registerGui();
    }
}
