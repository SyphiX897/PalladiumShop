package ir.syphix.palladiumshop.core.gui;

import ir.syphix.palladiumshop.core.gui.CustomGui;

import java.util.HashMap;
import java.util.List;

public class CustomGuiManager {

    public static HashMap<String, CustomGui> guis = new HashMap<>();

    public static CustomGui getCustomGuiById(String id) {
        return guis.get(id);
    }

    public static List<CustomGui> getGuis() {
        return guis.values().stream().toList();
    }
}
