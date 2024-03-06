package ir.syphix.palladiumshop.core.shop;

import ir.syphix.palladiumshop.PalladiumShop;
import ir.syrent.origin.paper.Origin;
import ir.syrent.origin.paper.utils.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ShopItem {

    ItemStack itemStack = null;
    Material material;
    ShopPrice shopPrice;
    String displayName;
    String shopItemsColor = Origin.getPlugin().getConfig().getString("shop.itemsColor");

    public ShopItem(String displayName, Material material, ShopPrice shopPrice) {
        this.displayName = displayName;
        this.material = material;
        this.shopPrice = shopPrice;
    }

    public ShopItem(String displayName, ItemStack itemStack, ShopPrice shopPrice) {
        this.displayName = displayName;
        this.itemStack = itemStack;
        this.shopPrice = shopPrice;
    }

    public ItemStack item() {
        if (this.itemStack == null) {
            ItemStack itemStack = new ItemStack(material);
            List<Component> itemLore = new ArrayList<>();
            itemLore.add(toComponent("<gradient:dark_green:green>Buy: " + shopPrice.buyPrice() + "$"));
            itemLore.add(toComponent("<gradient:dark_red:red>Buy: " + shopPrice.sellPrice() + "$"));
            itemStack.editMeta(itemMeta -> {
                itemMeta.displayName(toComponent(shopItemsColor + displayName));
                itemMeta.lore(itemLore);
            });
            return itemStack;
        }

        return itemStack;
    }

    public void buy(Player player, int amount) {
        double playerBalance = PalladiumShop.getEconomy().getBalance(player);
        double buyPrice = (shopPrice().buyPrice() * amount);

        if (playerBalance >= buyPrice) {
            if (player.getInventory().firstEmpty() == -1) {
                player.sendMessage(toComponent("<gradient:dark_red:red>Your inventory is full!"));
            } else {
                PalladiumShop.getEconomy().withdrawPlayer(player, buyPrice);
                if (this.itemStack == null) {
                    ItemStack itemStack = new ItemStack(material);
                    itemStack.setAmount(amount);
                    player.getInventory().addItem(itemStack);
                } else {
                    this.itemStack.setAmount(amount);
                    player.getInventory().addItem(this.itemStack);
                }
            }
        } else {
            player.sendMessage(ComponentUtils.component("<gradient:dark_red:red>You don't have enough money to buy this item!"));
        }

    }

    public void sell(Player player, int amount) {
        PalladiumShop.getEconomy().depositPlayer(player, shopPrice.sellPrice() * amount);
    }

    public ShopPrice shopPrice() {
        return shopPrice;
    }

    public static ShopItem fromConfig(ConfigurationSection section) {

        return new ShopItem(
                section.getName(),
                Material.valueOf(section.getString("material")),
                ShopPrice.fromConfig(section.getConfigurationSection("price"))
        );
    }

    private Component toComponent(String content) {
        return MiniMessage.miniMessage().deserialize(content).decoration(TextDecoration.ITALIC, false);
    }

}
