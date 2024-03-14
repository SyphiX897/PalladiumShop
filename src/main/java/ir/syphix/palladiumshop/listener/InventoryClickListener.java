package ir.syphix.palladiumshop.listener;

import ir.syphix.palladiumshop.core.gui.CustomGui;
import ir.syphix.palladiumshop.core.gui.CustomGuiManager;
import ir.syphix.palladiumshop.core.shop.ShopCategories;
import ir.syphix.palladiumshop.core.shop.ShopCategory;
import ir.syphix.palladiumshop.core.shop.ShopItem;
import ir.syrent.origin.paper.Origin;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class InventoryClickListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        ItemStack clickedItem = event.getCurrentItem();

        if (event.getInventory() == CustomGuiManager.getCustomGuiById("sell_gui").inventory()) {
            if (clickedItem == null) return;
            if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().getPersistentDataContainer().has(CustomGui.SHOP_GLASS)) {
                event.setCancelled(true);
                return;
            }
            return;
        }

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

        if (clickedItem == null) return;
        if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().getPersistentDataContainer().isEmpty()) return;
        if (clickedItem.getItemMeta().getPersistentDataContainer().has(CustomGui.SHOP_GLASS)) return;

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


        if (clickedItem.hasItemMeta()) {
            PersistentDataContainer itemData = clickedItem.getItemMeta().getPersistentDataContainer();

            if (itemData.has(CustomGui.SHOP_PAGE)) {
                String page = itemData.get(CustomGui.SHOP_PAGE, PersistentDataType.STRING);
                if (page == null) return;
                player.openInventory(gui.inventories.get(Integer.valueOf(page)));

            } else if (itemData.has(CustomGui.SHOP_SELL)) {
                player.openInventory(CustomGuiManager.getCustomGuiById("sell_gui").inventory());

            } else if (itemData.has(ShopItem.SHOP_ITEM)) {
                if (item == null) return;
                ClickType clickType = event.getClick();
                if (clickType.isLeftClick() && clickType.isShiftClick()) {
                    item.buy(player, 32);
                } else if (clickType.isRightClick() && clickType.isShiftClick()) {
                    item.buy(player, 128);
                } else if (clickType.isLeftClick()) {
                    item.buy(player, 1);
                } else if (clickType.isRightClick()) {
                    item.buy(player, 64);
                }

            } else if (itemData.has(ShopItem.SHOP_CUSTOM_ITEM)) {
                if (item == null) return;
                String id = item.item().getItemMeta().getPersistentDataContainer().get(ShopItem.SHOP_CUSTOM_ITEM, PersistentDataType.STRING);

                ClickType clickType = event.getClick();
                if (clickType.isLeftClick() && clickType.isShiftClick()) {
                    item.buy(player, id, 32);
                } else if (clickType.isRightClick() && clickType.isShiftClick()) {
                    item.buy(player, id, 128);
                } else if (clickType.isLeftClick()) {
                    item.buy(player, id, 1);
                } else if (clickType.isRightClick()) {
                    item.buy(player, id, 64);
                }

            }

        }

    }

}
