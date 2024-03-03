package ir.syphix.palladiumshop.core;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

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

    private final HashMap<Integer, Inventory> inventories = new HashMap<>();

    public CustomGui(String id, FileConfiguration config, int size, String title) {
        this.config = config;
        this.id = id;
        this.size = size;
        this.page = config.getConfigurationSection("items").getKeys(false).size() / 28;
        for (int i = 0; i < (page + 1); i++) {
            inventories.put(i, Bukkit.createInventory(null, size, toComponent(title + " <gray>(Page: " + (i + 1) + ")")));
        }
        inventory = inventories.get(0);
    }

    public void addIngredient(Character character, ItemStack itemStack, String displayName) {
        itemStack.editMeta(itemMeta -> itemMeta.displayName(toComponent(displayName)));
        ingredient.put(character, itemStack);
    }
    public void addShape(String... shape) {
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
            inventory.setItem(count, ingredient.get(character));
            count++;
        }

    }

    public void completeGui() {
        List<ItemStack> itemStackList = getItemStackList();
        int min = (page * 28) - 28;
        int max = page * 28;

        if (page == 0) {
            int count = min;
            for (int i = 0; i < size; i++) {
                if (inventory.getItem(i) != null) continue;
                if (count >= itemStackList.size()) continue;
                inventory.setItem(i, itemStackList.get(count));
                count++;
            }
        }


    }

    public Inventory getInventory() {
        return inventory;
    }
    public void registerGui() {
        CustomGuiManager.guis.put(id, this);
    }

    public Component toComponent(String content) {
        return MiniMessage.miniMessage().deserialize(content).decoration(TextDecoration.ITALIC, false);
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
                });
            }

            itemStackList.add(itemStack);
        }

        return itemStackList;
    }

    private ItemStack nullItemStack(String name) {
        ItemStack itemStack = new ItemStack(Material.BARRIER);
        itemStack.editMeta(itemMeta -> itemMeta.displayName(toComponent("<gradient:dark_red:red>Wrong material: " + name)));

        return itemStack;
    }

}
