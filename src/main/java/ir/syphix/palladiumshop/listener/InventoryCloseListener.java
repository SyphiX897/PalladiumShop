package ir.syphix.palladiumshop.listener;

import ir.syphix.palladiumshop.PalladiumShop;
import ir.syphix.palladiumshop.core.gui.CustomGui;
import ir.syphix.palladiumshop.core.gui.CustomGuiManager;
import ir.syphix.palladiumshop.core.shop.ShopCategories;
import ir.syphix.palladiumshop.core.shop.ShopCategory;
import ir.syphix.palladiumshop.core.shop.ShopItem;
import ir.syrent.origin.paper.utils.ComponentUtils;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryCloseListener implements Listener {


    String prefix = PalladiumShop.prefix();

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if (event.getInventory() != CustomGuiManager.getCustomGuiById("sell_gui").getInventory()) return;
        if (!(event.getPlayer() instanceof Player player)) return;
        Inventory inventory = event.getInventory();
        double totalPrice = 0;
        int itemAmount = 0;
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
            
            if (!isSimilar || item.shopPrice().sellPrice() == -1) {
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
            itemAmount += itemStack.getAmount();
            totalPrice += (itemStack.getAmount() * item.shopPrice().sellPrice());
        }

        if (itemAmount <= 0) return;
        player.sendMessage(ComponentUtils.component(String.format("%s>> <gradient:dark_green:green>You have sold <yellow>%dx</yellow> items for <yellow>%s</yellow>$", prefix, itemAmount, totalPrice)));
    }
}
