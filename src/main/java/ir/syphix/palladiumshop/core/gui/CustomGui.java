package ir.syphix.palladiumshop.core.gui;

import ir.syrent.origin.paper.Origin;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CustomGui {

    private final FileConfiguration config;
    private final Inventory inventory;
    private final String id;
    private final int size;
    private final int page;

    private final HashMap<Character, ItemStack> ingredient = new HashMap<>();

    public final HashMap<Integer, Inventory> inventories = new HashMap<>();
    public NamespacedKey SHOP_PAGE = new NamespacedKey(Origin.getPlugin(), "shop_page");
    public NamespacedKey SHOP_ITEM = new NamespacedKey(Origin.getPlugin(), "shop_item");
    public NamespacedKey SHOP_SELL = new NamespacedKey(Origin.getPlugin(), "shop_sell");

    public CustomGui(String id, FileConfiguration config, int size, String title) {
        this.id = id;
        this.config = config;
        this.size = size;
        int itemAmount = config.getConfigurationSection("items").getKeys(false).size();

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

    public void glassShapeIngredient(Character character, ItemStack itemStack, String displayName) {
        itemStack.editMeta(itemMeta -> itemMeta.displayName(toComponent(displayName)));
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
        List<ItemStack> itemStackList = getItemStackList();


        if (page == 0) {
            int count = 0;
            for (int i = 0; i < size; i++) {
                if (inventory.getItem(i) != null) continue;
                if (count >= itemStackList.size()) continue;
                inventory.setItem(i, itemStackList.get(count));
                count++;
            }
            return;
        }

        int count = 0;
        for (int key : inventories.keySet()) {
            for (int i = 0; i < 28; i++) {
                if (count >= itemStackList.size()) continue;
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

            ItemStack sellItemStack = new ItemStack(Material.MINECART);
            sellItemStack.editMeta(itemMeta -> {
                itemMeta.displayName(toComponent("<#00aeff>Sell Menu"));
                itemMeta.addEnchant(Enchantment.MENDING, 1, false);
                itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
                itemMeta.getPersistentDataContainer().set(SHOP_SELL, PersistentDataType.STRING, "sell_shop");
            });
            inventories.get(key).setItem(49, sellItemStack);
        }
    }

    private List<ItemStack> getItemStackList() {
        ConfigurationSection rootSection = config.getConfigurationSection("items");
        if (rootSection == null) {
            throw new NullPointerException("Can't find items section in yaml file");
        }
        List<ItemStack> itemStackList = new ArrayList<>();
        for (String items : rootSection.getKeys(false)) {
            ConfigurationSection itemSection = rootSection.getConfigurationSection(items);
            if (itemSection == null) {
                throw new NullPointerException("Can't find " + items + "section in yaml file");
            }
            ItemStack itemStack = nullItemStack(items);

            List<String> materials = new ArrayList<>();
            for (Material material : Material.class.getEnumConstants()) {
                materials.add(material.name());
            }
            if (materials.contains(items)) {
                itemStack.setType(Material.valueOf(items));
                List<Component> itemLore = new ArrayList<>();
                itemLore.add(toComponent("<gradient:dark_green:green>Buy: " + itemSection.getDouble("buy") + "$"));
                itemLore.add(toComponent("<gradient:dark_red:red>Sell: " + itemSection.getDouble("sell") + "$"));

                itemStack.editMeta(itemMeta -> {
                    itemMeta.lore(itemLore);
                    itemMeta.displayName(toComponent("<gray>" + items.replace("_", " ")));
                    itemMeta.getPersistentDataContainer().set(SHOP_ITEM, PersistentDataType.STRING, id);
                });
            }

            itemStackList.add(itemStack);
        }

        return itemStackList;
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

    private ItemStack nullItemStack(String name) {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        itemStack.editMeta(itemMeta -> itemMeta.displayName(toComponent("<gradient:dark_red:red>Wrong material: " + name)));

        return itemStack;
    }
    public Component toComponent(String content) {
        return MiniMessage.miniMessage().deserialize(content).decoration(TextDecoration.ITALIC, false);
    }

}
