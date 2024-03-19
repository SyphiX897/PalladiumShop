package ir.syphix.palladiumshop.utils;

import ir.syphix.palladiumshop.PalladiumShop;
import ir.syrent.origin.paper.Origin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class FileManager {

    private static final HashMap<String, FileConfiguration> categoryFiles = new HashMap<>();

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
            if (Arrays.stream((rootDirectory.listFiles())).map(File::getName).toList().contains(toYamlName(fileName)) ||
                    Origin.getPlugin().getClass().getClassLoader().getResource("categories/" + toYamlName(fileName)) != null) {
                YamlConfig itemsConfig = new YamlConfig(Origin.getPlugin().getDataFolder(), "categories/" + toYamlName(fileName));
                FileConfiguration config = itemsConfig.getConfig();
                categoryFiles.put(fileName, config);
            } else {
                try {
                    YamlConfiguration configuration = YamlConfiguration.loadConfiguration(new InputStreamReader(Origin.getPlugin().getResource("example.yml")));
                    configuration.save(new File(rootDirectory, toYamlName(fileName)));
                } catch (Exception exception) {
                    exception.printStackTrace();
                }

                YamlConfig itemsConfig = new YamlConfig(rootDirectory, toYamlName(fileName), false);
                FileConfiguration config = itemsConfig.getConfig();
                config.set("id", fileName);
                config.set("title", "<#00aeff>" + TextUtils.toFormattedName(fileName));
                itemsConfig.saveConfig();
                categoryFiles.put(fileName, config);
            }
        }

    }

    public static List<FileConfiguration> categories() {
        return categoryFiles.values().stream().toList();
    }

    public static FileConfiguration messagesFile() {
        YamlConfig messageConfig = new YamlConfig(Origin.getPlugin().getDataFolder(), "messages.yml");
        messageConfig.saveDefaultConfig();
        return messageConfig.getConfig();
    }
    private static String toYamlName(String name) {
        return name + ".yml";
    }
}
