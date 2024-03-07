package ir.syphix.palladiumshop.listener;

import ir.syphix.palladiumshop.core.gui.CustomGui;
import ir.syphix.palladiumshop.core.gui.CustomGuiManager;
import ir.syphix.palladiumshop.core.shop.ShopCategories;
import ir.syphix.palladiumshop.core.shop.ShopCategory;
import ir.syphix.palladiumshop.core.shop.ShopItem;
import ir.syrent.origin.paper.Origin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        boolean isShop = false;
        CustomGui gui = null;
        for (CustomGui customGui : CustomGuiManager.getGuis()) {
            for (Inventory inventory : customGui.inventories.values()) {
                if (event.getWhoClicked().getOpenInventory().getTopInventory() == inventory) {
                    isShop = true;
                    gui = customGui;
                    break;
                }
            }
        }
        if (!isShop) return;

        event.setCancelled(true);
        Player player = (Player) event.getWhoClicked();
        ItemStack clickedItem = event.getCurrentItem();

        if (clickedItem == null) return;
        if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().getPersistentDataContainer().isEmpty()) return;
        if (clickedItem.getType() == Material.LIME_STAINED_GLASS_PANE || clickedItem.getType() == Material.LIGHT_BLUE_STAINED_GLASS_PANE) return;

        ShopItem item = null;

        for (ShopCategory shopCategory : ShopCategories.getCategories()) {
            for (ShopItem shopItem : shopCategory.items()) {
                if ((clickedItem.getItemMeta().getPersistentDataContainer().has(ShopItem.SHOP_ITEM) &&
                                shopItem.item().getItemMeta().getPersistentDataContainer().has(ShopItem.SHOP_ITEM) && clickedItem.getType() == shopItem.item().getType()) ||
                        (clickedItem.getItemMeta().getPersistentDataContainer().has(ShopItem.SHOP_CUSTOM_ITEM) &&
                                shopItem.item().getItemMeta().getPersistentDataContainer().has(ShopItem.SHOP_CUSTOM_ITEM) && clickedItem.getType() == shopItem.item().getType())) {
                    item = shopItem;
                    break;
                }
            }
        }

        if (!gui.getId().equals("sell_gui")) {

            if (clickedItem.hasItemMeta()) {

                PersistentDataContainer itemData = clickedItem.getItemMeta().getPersistentDataContainer();

                if (itemData.has(gui.SHOP_PAGE)) {
                    String page = itemData.get(gui.SHOP_PAGE, PersistentDataType.STRING);
                    if (page == null) return;
                    player.openInventory(gui.inventories.get(Integer.valueOf(page)));

                } else if (itemData.has(gui.SHOP_SELL)) {
                    Origin.broadcast("test");
//                    player.openInventory(CustomGuiManager.getCustomGuiById("sell_gui").getInventory());

                } else if (itemData.has(ShopItem.SHOP_ITEM)) {
                    if (item == null) return;
                    if (event.getClick().isLeftClick()) {
                        item.buy(player, 1);
                    } else if (event.getClick().isRightClick()) {
                        item.buy(player, 64);
                    }

                } else if (itemData.has(ShopItem.SHOP_CUSTOM_ITEM)) {
                    if (item == null) return;
                    String id = item.item().getItemMeta().getPersistentDataContainer().get(ShopItem.SHOP_CUSTOM_ITEM, PersistentDataType.STRING);

                    if (event.getClick().isLeftClick()) {
                        item.buy(player, id, 1);
                    } else if (event.getClick().isRightClick()) {
                        item.buy(player, id, 64);
                    }

                }

            }

        }
        //TODO: sell gui



    }

}
