package ir.syphix.palladiumshop.listener;

import ir.syphix.palladiumshop.PalladiumShop;
import ir.syphix.palladiumshop.core.gui.CustomGui;
import ir.syphix.palladiumshop.core.gui.CustomGuiManager;
import ir.syrent.origin.paper.utils.ComponentUtils;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
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

        if (!gui.getId().equals("sell_gui")) {
            if (!clickedItem.hasItemMeta()) return;
            PersistentDataContainer itemData = clickedItem.getItemMeta().getPersistentDataContainer();
            if (itemData.has(gui.SHOP_PAGE)) {
                String page = itemData.get(gui.SHOP_PAGE, PersistentDataType.STRING);
                if (page == null) return;
                player.openInventory(gui.inventories.get(Integer.valueOf(page)));

            } else if (itemData.has(gui.SHOP_ITEM)) {
                String configId = itemData.get(gui.SHOP_ITEM, PersistentDataType.STRING);
                FileConfiguration itemConfig = PalladiumShop.configList.get(configId);
                ConfigurationSection clickedItemSection = itemConfig.getConfigurationSection("items." + clickedItem.getType().name());
                if (clickedItemSection == null) return;
                double playerBalance = getPlayerBalance(player);
                double buyPrice = clickedItemSection.getDouble("buy");

                if (event.getClick().isLeftClick()) {
                    if (playerBalance >= buyPrice) {
                        withdrawMoney(player, buyPrice);
                        if (player.getInventory().firstEmpty() == -1) {
                            player.getLocation().getWorld().dropItemNaturally(player.getLocation(), new ItemStack(clickedItem.getType()));
                        } else {
                            player.getInventory().addItem(new ItemStack(clickedItem.getType()));
                        }
                    } else {
                        player.sendMessage(ComponentUtils.component("<gradient:dark_red:red>You don't have enough money to buy this item!"));
                    }
                }

            } else if (itemData.has(gui.SHOP_SELL)) {
                player.openInventory(CustomGuiManager.getCustomGuiById("sell_gui").getInventory());
            }
            return;
        }
        //TODO: sell gui



    }

    private double getPlayerBalance(Player player) {
        return PalladiumShop.getEconomy().getBalance(player);
    }
    private void withdrawMoney(Player player, double amount) {
        PalladiumShop.getEconomy().withdrawPlayer(player, amount);
    }

}
