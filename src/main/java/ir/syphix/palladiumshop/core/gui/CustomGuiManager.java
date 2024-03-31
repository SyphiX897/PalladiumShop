package ir.syphix.palladiumshop.core.gui;

import ir.syphix.palladiumshop.gui.MainGui;
import ir.syphix.palladiumshop.gui.SellGui;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CustomGuiManager {

    private static final HashMap<String, CustomGui> GUI_HASH_MAP = new HashMap<>();
    private static final HashMap<UUID, SellGui> SELL_GUI_HASH_MAP = new HashMap<>();
    private static final HashMap<UUID, MainGui> MAIN_GUI_HASH_MAP = new HashMap<>();

    public static CustomGui guiById(String id) {
        return GUI_HASH_MAP.get(id);
    }
    public static List<CustomGui> guis() {
        return GUI_HASH_MAP.values().stream().toList();
    }
    public static HashMap<String, CustomGui> guiHashMap() {
        return GUI_HASH_MAP;
    }

    public static SellGui sellGuiByUuid(UUID uuid) { return SELL_GUI_HASH_MAP.get(uuid); }
    public static HashMap<UUID, SellGui> sellGuiHashMap() {
        return SELL_GUI_HASH_MAP;
    }

    public static MainGui mainGuiByUuid(UUID uuid) { return MAIN_GUI_HASH_MAP.get(uuid); }
    public static HashMap<UUID, MainGui> mainGuiHashMap() {
        return MAIN_GUI_HASH_MAP;
    }

    private CustomGuiManager() {
    }

}
