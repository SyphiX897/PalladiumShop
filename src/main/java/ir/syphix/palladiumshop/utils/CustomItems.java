package ir.syphix.palladiumshop.utils;

import ir.syphix.palladiumitems.Core.CustomItem;
import ir.syphix.palladiumitems.Core.CustomItemManager;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CustomItems {

    public static HashMap<String, ItemStack> customItemList = new HashMap<>();

    public static void addItems() {
        for (CustomItem customItem : CustomItemManager.getItems()) {
            customItemList.put(customItem.getId(), customItem.getItemStack());
        }
    }

}
