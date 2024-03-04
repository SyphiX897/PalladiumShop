package ir.syphix.palladiumshop.listener;

import ir.syphix.palladiumshop.PalladiumShop;
import ir.syphix.palladiumshop.core.CustomGui;
import ir.syphix.palladiumshop.core.CustomGuiManager;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
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
        if (clickedItem.getType() == Material.LIME_STAINED_GLASS_PANE || clickedItem.getType() == Material.LIGHT_BLUE_STAINED_GLASS_PANE) return;
        if (!clickedItem.hasItemMeta()) return;

        PersistentDataContainer itemData = clickedItem.getItemMeta().getPersistentDataContainer();
        if (itemData.has(gui.SHOP_PAGE)) {

            String page = itemData.get(gui.SHOP_PAGE, PersistentDataType.STRING);
            if (page == null) return;
            player.openInventory(gui.inventories.get(Integer.valueOf(page)));

        } else if (itemData.has(gui.SHOP_ITEM)) {

            String configId = itemData.get(gui.SHOP_ITEM, PersistentDataType.STRING);
            FileConfiguration itemConfig = PalladiumShop.configList.get(configId);
        }


    }
}
