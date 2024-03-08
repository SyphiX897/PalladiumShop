package ir.syphix.palladiumshop.core.gui;

import ir.syphix.palladiumshop.core.shop.ShopCategory;
import ir.syphix.palladiumshop.core.shop.ShopItem;
import ir.syrent.origin.paper.Origin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomGui {

    private final Inventory inventory;
    private final ShopCategory shopCategory;
    private final String id;
    private final int size;
    private final int page;


    private final HashMap<Character, ItemStack> ingredient = new HashMap<>();

    public final HashMap<Integer, Inventory> inventories = new HashMap<>();
    public static NamespacedKey SHOP_PAGE = new NamespacedKey(Origin.getPlugin(), "shop_page");
    public static NamespacedKey SHOP_GLASS = new NamespacedKey(Origin.getPlugin(), "shop_item");
    public static NamespacedKey SHOP_SELL = new NamespacedKey(Origin.getPlugin(), "shop_sell");

    public CustomGui(String id, ShopCategory shopCategory, int size, String title) {
        this.id = id;
        this.size = size;
        this.shopCategory = shopCategory;
        int itemAmount = shopCategory.items().size();

        int page;
        if (itemAmount % 28 == 0 && itemAmount != 0) {
            page = (itemAmount / 28) - 1;
        } else {
            page = itemAmount / 28;
        }
        this.page = page;

        for (int i = 0; i < (this.page + 1); i++) {
            inventories.put((i + 1), Bukkit.createInventory(null, size, toComponent(title + " <dark_gray>(Page: " + (i + 1) + ")")));
        }
        inventory = inventories.get(1);
    }

    public CustomGui(String id, int size, String title) {
        this.id = id;
        this.size = size;
        this.page = 0;
        this.shopCategory = null;
        inventories.put(1, Bukkit.createInventory(null, size, toComponent(title)));
        inventory = inventories.get(1);
    }

    public void glassShapeIngredient(Character character, ItemStack itemStack, String displayName) {
        itemStack.editMeta(itemMeta -> {
            itemMeta.displayName(toComponent(displayName));
            itemMeta.getPersistentDataContainer().set(SHOP_GLASS, PersistentDataType.STRING, "glass");
        });
        ingredient.put(character, itemStack);
    }
    public void glassShape(String... shape) {
        List<Character> shapeList = new ArrayList<>();
        for (String s : shape) {
            for (char c : s.toCharArray()) {
                if (c == ' ') {
                    shapeList.add('/');
                    continue;
                }
                shapeList.add(c);
            }
        }

        int count = 0;
        for (Character character : shapeList) {
            if (character == '/') {
                count++;
                continue;
            }
            for (Inventory inv : inventories.values()) {
                inv.setItem(count, ingredient.get(character));
            }

            count++;
        }

    }

    public void completeGui() {
        List<ItemStack> itemStackList = shopCategory.items().stream().map(ShopItem::guiItemStack).toList();

        if (page == 0) {
            int count = 0;
            for (int i = 0; i < size; i++) {
                if (inventory.getItem(i) != null) continue;
                if (count >= itemStackList.size()) continue;
                inventory.setItem(i, itemStackList.get(count));
                count++;
            }
            inventory.setItem(49, getSellGuiItemStack());
            return;
        }

        int count = 0;
        for (int key : inventories.keySet()) {
            for (int i = 0; i < 28; i++) {
                if (count >= itemStackList.size()) break;
                int firstEmpty = inventories.get(key).firstEmpty();
                inventories.get(key).setItem(firstEmpty, itemStackList.get(count));
                count++;
            }
            if (inventories.size() != key) {
                ItemStack nextPage = new ItemStack(Material.ARROW);
                nextPage.editMeta(itemMeta -> {
                    itemMeta.displayName(toComponent("<gradient:dark_green:green>Next Page"));
                    itemMeta.getPersistentDataContainer().set(SHOP_PAGE, PersistentDataType.STRING, String.valueOf((key + 1)));
                });
                inventories.get(key).setItem(53, nextPage);
            }
            if (key > 1) {
                ItemStack previousPage = new ItemStack(Material.ARROW);
                previousPage.editMeta(itemMeta -> {
                    itemMeta.displayName(toComponent("<gradient:gold:yellow>Previous Page"));
                    itemMeta.getPersistentDataContainer().set(SHOP_PAGE, PersistentDataType.STRING, String.valueOf((key - 1)));
                });
                inventories.get(key).setItem(45, previousPage);
            }


            inventories.get(key).setItem(49, getSellGuiItemStack());
        }
    }

    public Inventory getInventory() {
        return inventory;
    }

    public String getId() {
        return id;
    }

    public void registerGui() {
        CustomGuiManager.guis.put(id, this);
    }

    private ItemStack getSellGuiItemStack() {
        ItemStack sellItemStack = new ItemStack(Material.MINECART);
        sellItemStack.editMeta(itemMeta -> {
            itemMeta.displayName(toComponent("<#00aeff>Sell Menu"));
            itemMeta.addEnchant(Enchantment.MENDING, 1, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.getPersistentDataContainer().set(SHOP_SELL, PersistentDataType.STRING, "sell_shop");
        });

        return sellItemStack;
    }

    public Component toComponent(String content) {
        return MiniMessage.miniMessage().deserialize(content).decoration(TextDecoration.ITALIC, false);
    }

}
