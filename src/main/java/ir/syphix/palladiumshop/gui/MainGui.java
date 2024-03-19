package ir.syphix.palladiumshop.gui;

import ir.syphix.palladiumshop.annotation.AutoInitializer;
import ir.syphix.palladiumshop.core.gui.CustomGui;
import ir.syphix.palladiumshop.core.shop.ShopCategory;

@SuppressWarnings("unused")
@AutoInitializer
public class MainGui extends CustomGui {
    public MainGui() {
        super("main_gui", null, 54, "<#00aeff>Shop");
    }
}
