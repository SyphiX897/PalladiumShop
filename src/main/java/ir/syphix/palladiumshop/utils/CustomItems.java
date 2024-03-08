package ir.syphix.palladiumshop.utils;

import io.github.thebusybiscuit.slimefun4.api.items.SlimefunItem;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import ir.syphix.palladiumitems.Core.CustomItem;
import ir.syphix.palladiumitems.Core.CustomItemManager;
import ir.syrent.origin.paper.Origin;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class CustomItems {

    public static HashMap<String, ItemStack> customItemList = new HashMap<>();

    public static void addItems() {
        if (Origin.hasPlugin("PalladiumItems")) {
            for (CustomItem customItem : CustomItemManager.getItems()) {
                customItemList.put(customItem.getId(), customItem.getItemStack());
            }
        }
        if (Origin.hasPlugin("Slimefun")) {
            for (ItemStack itemStack : Slimefun.getRegistry().getAllSlimefunItems().stream().map(SlimefunItem::getItem).toList()) {
                Origin.broadcast(String.valueOf(itemStack));
            }
        }

    }

}
