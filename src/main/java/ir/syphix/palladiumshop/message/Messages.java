package ir.syphix.palladiumshop.message;

import ir.syphix.palladiumshop.utils.FileManager;
import org.bukkit.configuration.file.FileConfiguration;

public class Messages {

    FileConfiguration config = FileManager.messagesFile();

    public static String PREFIX;
    public static String NEED_PERMISSION;
    public static String PLAYER_NOT_FOUND;
    public static String MENU_NOT_FOUND;
    public static String SELL;
    public static String BUY;
    public static String NOT_ENOUGH_MONEY;
    public static String INVENTORY_IS_FULL;
    public static String RELOAD;


    public Messages() {
        PREFIX = getMessage("prefix");
        NEED_PERMISSION = getMessage("need_permission");
        PLAYER_NOT_FOUND = getMessage("player_not_found");
        MENU_NOT_FOUND = getMessage("menu_not_found");
        SELL = getMessage("sell");
        BUY = getMessage("buy");
        NOT_ENOUGH_MONEY = getMessage("not_enough_money");
        INVENTORY_IS_FULL = getMessage("inventory_is_full");
        RELOAD = getMessage("reload");
    }

    private String getMessage(String path) {
        return config.getString(path);
    }
}
