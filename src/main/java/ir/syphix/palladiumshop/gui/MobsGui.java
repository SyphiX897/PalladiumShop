package ir.syphix.palladiumshop.gui;

import ir.syphix.palladiumshop.annotation.GuiHandler;
import ir.syphix.palladiumshop.core.gui.CustomGui;
import ir.syphix.palladiumshop.core.shop.ShopCategories;
import ir.syphix.palladiumshop.core.shop.ShopItem;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

@GuiHandler
public class MobsGui extends CustomGui {

    public MobsGui() {
        super("mobs", ShopCategories.getCategory("mobs"), 54, "<#00aeff>Mobs Drop");

        glassShapeIngredient('B', new ItemStack(Material.LIGHT_BLUE_STAINED_GLASS_PANE), " ");
        glassShapeIngredient('G', new ItemStack(Material.LIME_STAINED_GLASS_PANE), " ");
        glassShape("GGBBBBBGG", "G       G", "B       B", "B       B", "G       G", "GGBBBBBGG");
        completeGui();
        registerGui();
    }

}