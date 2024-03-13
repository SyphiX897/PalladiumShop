package ir.syphix.palladiumshop.core.shop;

import ir.syphix.palladiumshop.PalladiumShop;
import ir.syphix.palladiumshop.item.CustomItems;
import ir.syphix.palladiumshop.message.Messages;
import ir.syphix.palladiumshop.utils.Utils;
import ir.syrent.origin.paper.Origin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ShopItem {

    private final ItemStack originalItemStack;
    private final ItemStack itemStack;
    private final ShopPrice shopPrice;
    private final String displayName;
    private final String shopItemsColor = Origin.getPlugin().getConfig().getString("shop.itemsColor");

    public static NamespacedKey SHOP_ITEM = new NamespacedKey(Origin.getPlugin(), "shop_item");
    public static NamespacedKey SHOP_CUSTOM_ITEM = new NamespacedKey(Origin.getPlugin(), "shop_custom_item");

    public ShopItem(String displayName, Material material, ShopPrice shopPrice) {
        this.displayName = displayName;
        this.shopPrice = shopPrice;
        this.originalItemStack = new ItemStack(material);

        ItemStack dummyItemStack = new ItemStack(material);
        dummyItemStack.editMeta(itemMeta -> itemMeta.getPersistentDataContainer().set(SHOP_ITEM, PersistentDataType.STRING, ""));
        this.itemStack = dummyItemStack;
    }

    public ShopItem(String displayName, String id, ItemStack itemStack, ShopPrice shopPrice) {
        this.originalItemStack = itemStack.clone();
        this.displayName = displayName;
        this.shopPrice = shopPrice;

        ItemStack dummyItemStack = itemStack.clone();
        dummyItemStack.editMeta(itemMeta -> {
            for (NamespacedKey namespacedKey : itemMeta.getPersistentDataContainer().getKeys()) {
                itemMeta.getPersistentDataContainer().remove(namespacedKey);
            }
            itemMeta.getPersistentDataContainer().set(SHOP_CUSTOM_ITEM, PersistentDataType.STRING, id);
        });

        this.itemStack = dummyItemStack;
    }

    public ItemStack item() {
        return itemStack;
    }

    public void buy(Player player, int amount) {
        double playerBalance = PalladiumShop.getEconomy().getBalance(player);
        double buyPrice = (shopPrice().buyPrice() * amount);

        if (playerBalance >= buyPrice) {
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(Utils.toFormattedComponent(Messages.INVENTORY_IS_FULL));
            } else {
                PalladiumShop.getEconomy().withdrawPlayer(player, buyPrice);
                ItemStack dummyItemStack = new ItemStack(itemStack.getType());
                dummyItemStack.setAmount(amount);
                player.getInventory().addItem(dummyItemStack);
                player.sendMessage(Utils.toFormattedComponent(Messages.BUY,
                        Placeholder.unparsed("item-amount", String.valueOf(amount)),
                        Placeholder.unparsed("item-name", Utils.toFormattedName(displayName)),
                        Placeholder.unparsed("total-price", String.valueOf(buyPrice))));
            }
        } else {
            player.sendMessage(Utils.toFormattedComponent(Messages.NOT_ENOUGH_MONEY));
        }

    }

    public void buy(Player player, String id, int amount) {
        double playerBalance = PalladiumShop.getEconomy().getBalance(player);
        double buyPrice = (shopPrice().buyPrice() * amount);

        if (playerBalance >= buyPrice) {
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(Utils.toFormattedComponent(Messages.INVENTORY_IS_FULL));
            } else {
                PalladiumShop.getEconomy().withdrawPlayer(player, buyPrice);
                ItemStack dummyItemStack = CustomItems.customItemList.get(id).clone();
                dummyItemStack.setAmount(amount);
                player.getInventory().addItem(dummyItemStack);
                player.sendMessage(Utils.toFormattedComponent(Messages.BUY,
                        Placeholder.unparsed("item-amount", String.valueOf(amount)),
                        Placeholder.unparsed("item-name", Utils.toFormattedName(displayName)),
                        Placeholder.unparsed("total-price", String.valueOf(buyPrice))));
            }
        } else {
            player.sendMessage(Utils.toFormattedComponent(Messages.NOT_ENOUGH_MONEY));
        }

    }

    public void sell(Player player, int amount) {
        PalladiumShop.getEconomy().depositPlayer(player, shopPrice.sellPrice() * amount);
    }

    public String displayName() {
        return displayName;
    }

    public ShopPrice shopPrice() {
        return shopPrice;
    }

    public ItemStack originalItemStack() {
        return originalItemStack;
    }

    public ItemStack guiItemStack() {
        ItemStack dummyItemStack = itemStack.clone();
        String displayName = (shopItemsColor + Utils.toFormattedName(displayName()));
        String customModelData;
        List<Component> itemStackLore = new ArrayList<>();
        if (dummyItemStack.getItemMeta().hasLore()) {
            itemStackLore = dummyItemStack.getItemMeta().lore();
        }
        if (itemStackLore == null) return dummyItemStack;

        String buy;
        String sell;

        if (shopPrice.sellPrice() == -1) {
            sell = "Unsellable";
        } else {
            sell = shopPrice.sellPrice() + "$";
        }
        if (shopPrice.buyPrice() == -1) {
            buy = "Unpurchasable";
        } else {
            buy = shopPrice.buyPrice() + "$";
        }


        itemStackLore.add(toComponent("<gradient:dark_green:green>Buy: " + buy));
        itemStackLore.add(toComponent("<gradient:dark_red:red>Sell: " + sell));

        if (itemStack.getItemMeta().hasCustomModelData()) {
            customModelData = String.valueOf(itemStack.getItemMeta().getCustomModelData());
        } else {
            customModelData = null;
        }

        List<Component> finalItemStackLore = itemStackLore;
        dummyItemStack.editMeta(itemMeta -> {
            itemMeta.displayName(toComponent(displayName));
            itemMeta.lore(finalItemStackLore);
            if (customModelData != null) {
                itemMeta.setCustomModelData(Integer.valueOf(customModelData));
            }
        });

        return dummyItemStack;
    }

    public static ShopItem fromConfig(ConfigurationSection section) {
        ConfigurationSection priceSection = section.getConfigurationSection("price");
        if (priceSection == null) return null;

        if (section.getString("material") == null && section.getString("item") != null) {
            String item = section.getString("item");
            if (item == null) return null;

            return new ShopItem(
                    section.getName(),
                    item,
                    CustomItems.customItemList.get(item),
                    ShopPrice.fromConfig(priceSection)
            );

        } else if (section.getString("material") != null && section.getString("item") == null) {
            return new ShopItem(
                    section.getName(),
                    Material.valueOf(section.getString("material")),
                    ShopPrice.fromConfig(priceSection)
            );
        }
        return null;
    }

    private Component toComponent(String content) {
        return MiniMessage.miniMessage().deserialize(content).decoration(TextDecoration.ITALIC, false);
    }

}
