package ir.syphix.palladiumshop.listener;

import ir.syphix.palladiumshop.core.gui.CustomGui;
import ir.syphix.palladiumshop.core.gui.CustomGuiManager;
import ir.syphix.palladiumshop.core.shop.ShopCategories;
import ir.syphix.palladiumshop.core.shop.ShopCategory;
import ir.syphix.palladiumshop.core.shop.ShopItem;
import ir.syphix.palladiumshop.gui.MainGui;
import ir.syrent.origin.paper.Origin;
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
        Player player = (Player) event.getWhoClicked();

        if (event.getInventory() == CustomGuiManager.sellGuiByUuid(player.getUniqueId()).getInventory()) {
            event.setCancelled(true);
            if (clickedItem == null) return;
            if (clickedItem.hasItemMeta() && clickedItem.getItemMeta().getPersistentDataContainer().has(CustomGui.SHOP_GLASS)) {
                return;
            }
            return;
        }

        if (event.getInventory().getHolder() instanceof MainGui) {
            Origin.broadcast("1");
            event.setCancelled(true);
            if (clickedItem == null) return;
            Origin.broadcast("2");
            if (!clickedItem.hasItemMeta()) return;
            Origin.broadcast("3");
            if (clickedItem.getItemMeta().getPersistentDataContainer().has(CustomGui.SHOP_GLASS)) return;
            Origin.broadcast("4");
            if (clickedItem.getItemMeta().getPersistentDataContainer().has(CustomGui.SHOP_CATEGORY)) {
                Origin.broadcast("5");
                String categoryName = clickedItem.getItemMeta().getPersistentDataContainer().get(CustomGui.SHOP_CATEGORY, PersistentDataType.STRING);
                player.openInventory(CustomGuiManager.guiById(categoryName).inventory());
                return;
            }
        }

        boolean isCustomGui = false;
        CustomGui gui = null;
        for (CustomGui customGui : CustomGuiManager.guis()) {
            for (Inventory inventory : customGui.inventories.values()) {
                if (event.getWhoClicked().getOpenInventory().getTopInventory() == inventory) {
                    isCustomGui = true;
                    gui = customGui;
                    break;
                }
            }
        }

        if (!isCustomGui) return;
        event.setCancelled(true);

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
                player.openInventory(CustomGuiManager.sellGuiByUuid(player.getUniqueId()).getInventory());

            } else if (itemData.has(ShopItem.SHOP_ITEM)) {
                if (item == null) return;
                ClickType clickType = event.getClick();
                buyHandler(clickType, item, player);

            } else if (itemData.has(ShopItem.SHOP_CUSTOM_ITEM)) {
                if (item == null) return;
                String id = item.item().getItemMeta().getPersistentDataContainer().get(ShopItem.SHOP_CUSTOM_ITEM, PersistentDataType.STRING);
                ClickType clickType = event.getClick();
                sellHandler(clickType, item, player, id);
            }

        }

    }

    public void buyHandler(ClickType clickType, ShopItem shopItem, Player player) {
        if (clickType.isLeftClick() && clickType.isShiftClick()) {
            shopItem.buy(player, 32);
        } else if (clickType.isRightClick() && clickType.isShiftClick()) {
            shopItem.buy(player, 128);
        } else if (clickType.isLeftClick()) {
            shopItem.buy(player, 1);
        } else if (clickType.isRightClick()) {
            shopItem.buy(player, 64);
        }
    }

    public void sellHandler(ClickType clickType, ShopItem shopItem, Player player, String id) {
        if (clickType.isLeftClick() && clickType.isShiftClick()) {
            shopItem.buy(player, id, 32);
        } else if (clickType.isRightClick() && clickType.isShiftClick()) {
            shopItem.buy(player, id, 128);
        } else if (clickType.isLeftClick()) {
            shopItem.buy(player, id, 1);
        } else if (clickType.isRightClick()) {
            shopItem.buy(player, id, 64);
        }
    }

}
