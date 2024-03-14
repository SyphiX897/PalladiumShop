package ir.syphix.palladiumshop.utils;

import ir.syphix.palladiumshop.PalladiumShop;
import ir.syrent.origin.paper.Origin;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class FileManager {

    private static final HashMap<String, FileConfiguration> categoriesFile = new HashMap<>();

    public static void addCategories() {
        List<String> filesList = PalladiumShop.getInstance().getConfig().getStringList("categories");
        File rootDirectory = new File(Origin.getPlugin().getDataFolder(), "categories");
        if (!rootDirectory.exists()) {
            try {
                rootDirectory.mkdir();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        for (String fileName : filesList) {
            if (Arrays.stream(Objects.requireNonNull(rootDirectory.listFiles())).map(File::getName).toList().contains(fileName + ".yml")) {
                YamlConfig itemConfig = new YamlConfig(rootDirectory, (fileName + ".yml"), false);
                FileConfiguration configuration = itemConfig.getConfig();
                categoriesFile.put(fileName, configuration);
                continue;
            }
            YamlConfig itemConfig = new YamlConfig(rootDirectory, (fileName + ".yml"), false);
            PalladiumShop.getInstance().saveResource("categories" + File.separator + fileName + ".yml", false);
            FileConfiguration configuration = itemConfig.getConfig();
            categoriesFile.put(fileName, configuration);
        }

    }

    public static List<FileConfiguration> categories() {
        return categoriesFile.values().stream().toList();
    }

    public static FileConfiguration messagesFile() {
        YamlConfig messageConfig = new YamlConfig(Origin.getPlugin().getDataFolder(), "messages.yml");
        messageConfig.saveDefaultConfig();
        return messageConfig.getConfig();
    }
}
