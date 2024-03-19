package ir.syphix.palladiumshop.core.gui;

import ir.syphix.palladiumshop.gui.SellGui;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CustomGuiManager {

    public static HashMap<String, CustomGui> guis = new HashMap<>();
    public static HashMap<UUID, SellGui> sellGuis = new HashMap<>();

    public static CustomGui getCustomGuiById(String id) {
        return guis.get(id);
    }

    public static List<CustomGui> getGuis() {
        return guis.values().stream().toList();
    }

    private CustomGuiManager() {
    }

}
