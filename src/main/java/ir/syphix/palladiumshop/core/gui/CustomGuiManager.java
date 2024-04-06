package ir.syphix.palladiumshop.core.gui;

import ir.syphix.palladiumshop.gui.MainGui;
import ir.syphix.palladiumshop.gui.SellGui;
import ir.syrent.origin.paper.Origin;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class CustomGuiManager {

    private static final FileConfiguration config = Origin.getPlugin().getConfig();

    private static final HashMap<String, CustomGui> GUI = new HashMap<>();
    private static final HashMap<UUID, SellGui> SELL_GUI = new HashMap<>();
    private static final HashMap<UUID, MainGui> MAIN_GUI = new HashMap<>();

    public static CustomGui guiById(String id) {
        return GUI.get(id);
    }
    public static List<CustomGui> guis() {
        return GUI.values().stream().toList();
    }
    public static HashMap<String, CustomGui> guiHashMap() {
        return GUI;
    }

    public static SellGui sellGuiByUuid(UUID uuid) { return SELL_GUI.get(uuid); }
    public static HashMap<UUID, SellGui> sellGuiHashMap() {
        return SELL_GUI;
    }

    public static MainGui mainGuiByUuid(UUID uuid) { return MAIN_GUI.get(uuid); }
    public static HashMap<UUID, MainGui> mainGuiHashMap() {
        return MAIN_GUI;
    }

    public static int mainGuiSlot() {
        return config.getInt("main_gui.profile.slot");
    }

    private CustomGuiManager() {
    }

}
