package ir.syphix.palladiumshop.core.gui;

import ir.syphix.palladiumshop.core.shop.ShopCategory;
import ir.syphix.palladiumshop.core.shop.ShopItem;
import ir.syphix.palladiumshop.utils.TextUtils;
import ir.syrent.origin.paper.Origin;
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
    private int page = 0;


    private final int freeSpace = freeSpace();
    private final HashMap<Character, ItemStack> ingredient = new HashMap<>();

    public final HashMap<Integer, Inventory> inventories = new HashMap<>();
    public static NamespacedKey SHOP_PAGE = new NamespacedKey(Origin.getPlugin(), "shop_page");
    public static NamespacedKey SHOP_GLASS = new NamespacedKey(Origin.getPlugin(), "shop_glass");
    public static NamespacedKey SHOP_SELL = new NamespacedKey(Origin.getPlugin(), "shop_sell");
    public static NamespacedKey SHOP_CATEGORY = new NamespacedKey(Origin.getPlugin(), "shop_category");

    public CustomGui(String id, ShopCategory shopCategory, int size, String title) {
        this.id = id;
        this.size = size;
        this.shopCategory = shopCategory;

        if (shopCategory != null) {
            int itemAmount = shopCategory.items().size();

            int page;
            if (itemAmount % freeSpace == 0 && itemAmount != 0) {
                page = (itemAmount / freeSpace) - 1;
            } else {
                page = itemAmount / freeSpace;
            }
            this.page = (page + 1);

            for (int i = 0; i < this.page; i++) {
                inventories.put((i + 1), Bukkit.createInventory(null, size, TextUtils.toComponent(title + " <dark_gray>(Page: " + (i + 1) + "/" + this.page + ")")));
            }
        } else {
            inventories.put(1, Bukkit.createInventory(null, size, TextUtils.toComponent(title)));
        }

        inventory = inventories.get(1);
    }

    public void glassShapeIngredient(Character character, ItemStack itemStack, String displayName) {
        itemStack.editMeta(itemMeta -> {
            itemMeta.displayName(TextUtils.toComponent(displayName));
            itemMeta.getPersistentDataContainer().set(SHOP_GLASS, PersistentDataType.STRING, "glass");
        });
        ingredient.put(character, itemStack);
    }
    public void glassShape(List<String> shape) {
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
            inventory.setItem(49, sellGuiItemStack());
            return;
        }

        int count = 0;
        for (int key : inventories.keySet()) {
            for (int i = 0; i < freeSpace; i++) {
                if (count >= itemStackList.size()) break;
                int firstEmpty = inventories.get(key).firstEmpty();
                inventories.get(key).setItem(firstEmpty, itemStackList.get(count));
                count++;
            }
            if (inventories.size() != key) {
                ItemStack nextPage = new ItemStack(Material.ARROW);
                nextPage.editMeta(itemMeta -> {
                    itemMeta.displayName(TextUtils.toComponent("<gradient:dark_green:green>Next Page"));
                    itemMeta.getPersistentDataContainer().set(SHOP_PAGE, PersistentDataType.STRING, String.valueOf((key + 1)));
                });
                inventories.get(key).setItem(53, nextPage);
            }
            if (key > 1) {
                ItemStack previousPage = new ItemStack(Material.ARROW);
                previousPage.editMeta(itemMeta -> {
                    itemMeta.displayName(TextUtils.toComponent("<gradient:gold:yellow>Previous Page"));
                    itemMeta.getPersistentDataContainer().set(SHOP_PAGE, PersistentDataType.STRING, String.valueOf((key - 1)));
                });
                inventories.get(key).setItem(45, previousPage);
            }


            inventories.get(key).setItem(49, sellGuiItemStack());
        }
    }

    public void addCategory(String category, String displayName, String material, int slot) {
        ItemStack categoryItemStack = new ItemStack(Material.valueOf(material));
        categoryItemStack.editMeta(itemMeta -> {
            itemMeta.displayName(TextUtils.toComponent(displayName));
            itemMeta.getPersistentDataContainer().set(SHOP_CATEGORY, PersistentDataType.STRING, category);
        });

        inventory.setItem(slot, categoryItemStack);
    }

    public void registerGui() {
        CustomGuiManager.guiHashMap().put(id, this);
    }

    private ItemStack sellGuiItemStack() {
        ItemStack sellItemStack = new ItemStack(Material.MINECART);
        sellItemStack.editMeta(itemMeta -> {
            itemMeta.displayName(TextUtils.toComponent("<#00aeff>Sell Menu"));
            itemMeta.addEnchant(Enchantment.MENDING, 1, false);
            itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            itemMeta.getPersistentDataContainer().set(SHOP_SELL, PersistentDataType.STRING, "sell_shop");
        });

        return sellItemStack;
    }

    public Inventory inventory() {
        return inventory;
    }

    public String id() {
        return id;
    }
    public static int freeSpace() {
        int count = 0;
        for (String shape : Origin.getPlugin().getConfig().getStringList("shop.shape")) {
            for (Character character : shape.toCharArray()) {
                if (character != ' ') {
                    count++;
                }
            }
        }

        return (54 - count);
    }


}
