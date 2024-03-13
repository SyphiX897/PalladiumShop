package ir.syphix.palladiumshop.item;

import com.bgsoftware.wildtools.api.WildToolsAPI;
import com.bgsoftware.wildtools.api.objects.tools.Tool;
import com.bgsoftware.wildtools.utils.items.ToolItemStack;
import dev.lone.itemsadder.api.CustomStack;
import dev.lone.itemsadder.api.ItemsAdder;
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
            for (SlimefunItem slimefunItem : Slimefun.getRegistry().getAllSlimefunItems()) {
                customItemList.put(slimefunItem.getId(), slimefunItem.getItem());
            }
        }

        if (Origin.hasPlugin("WildTools")) {
            for (Tool tool : WildToolsAPI.getWildTools().getToolsManager().getTools()) {
                ToolItemStack toolItemStack = ToolItemStack.of(tool.getFormattedItemStack(tool.getDefaultUses()));
                customItemList.put(tool.getName(), toolItemStack.getItem());
            }
        }

        if (Origin.hasPlugin("ItemsAdder")) {
            for (CustomStack customStack : ItemsAdder.getAllItems()) {
                customItemList.put(customStack.getId(), customStack.getItemStack());
            }
        }
    }

}
