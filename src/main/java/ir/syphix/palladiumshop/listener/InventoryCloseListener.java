package ir.syphix.palladiumshop.listener;

import ir.syphix.palladiumshop.core.gui.CustomGui;
import ir.syphix.palladiumshop.core.gui.CustomGuiManager;
import ir.syphix.palladiumshop.core.shop.ShopCategories;
import ir.syphix.palladiumshop.core.shop.ShopCategory;
import ir.syphix.palladiumshop.core.shop.ShopItem;
import ir.syrent.origin.paper.Origin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryCloseListener implements Listener {


    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory() != CustomGuiManager.getCustomGuiById("sell_gui").getInventory()) return;
        if (!(event.getPlayer() instanceof Player player)) return;
        Inventory inventory = event.getInventory();
        for (ItemStack itemStack : inventory.getContents()) {
            boolean isSimilar = false;
            ShopItem item = null;
            if (itemStack == null) continue;
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().getPersistentDataContainer().has(CustomGui.SHOP_GLASS)) continue;
            for (ShopCategory shopCategory : ShopCategories.getCategories()) {
                for (ShopItem shopItem : shopCategory.items()) {
                    if (itemStack.isSimilar(shopItem.originalItemStack())) {
                        isSimilar = true;
                        item = shopItem;
                    }
                }
            }
            
            if (!isSimilar) {
                inventory.remove(itemStack);
                if (player.getInventory().firstEmpty() != -1) {
                    player.getInventory().addItem(itemStack);
                } else {
                    player.getLocation().getWorld().dropItemNaturally(player.getLocation(), itemStack);
                }
                continue;
            }
            inventory.remove(itemStack);
            item.sell(player, itemStack.getAmount());
            
        }
    }
}
