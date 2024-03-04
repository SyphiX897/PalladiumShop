package ir.syphix.palladiumshop;

import ir.syphix.palladiumshop.annotation.GuiHandlerProcessor;
import ir.syphix.palladiumshop.command.OpenGuiCommand;
import ir.syphix.palladiumshop.utils.YamlConfig;
import ir.syrent.origin.paper.Origin;
import ir.syrent.origin.paper.OriginPlugin;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public final class PalladiumShop extends OriginPlugin {

    public static HashMap<String, FileConfiguration> categoriesList = new HashMap<>();

    private static Economy econ = null;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        addConfigFiles();
        new OpenGuiCommand();
        GuiHandlerProcessor.process();

        for (String key : categoriesList.keySet()) {
            getLogger().warning(key);
            for (String string : categoriesList.get(key).getConfigurationSection("items").getKeys(false)) {
                getLogger().warning(string);
            }
        }
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    private void addConfigFiles() {
        List<String> filesList = getConfig().getStringList("categories");
        File rootDirectory = new File(Origin.getPlugin().getDataFolder(), "categories");
        if (!rootDirectory.exists()) {
            try {
                rootDirectory.mkdir();
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
        for (String fileName : filesList) {
            if (Arrays.stream(rootDirectory.listFiles()).map(File::getName).toList().contains(fileName + ".yml")) {
                YamlConfig itemConfig = new YamlConfig(rootDirectory, (fileName + ".yml"), false);
                FileConfiguration configuration = itemConfig.getConfig();
                categoriesList.put(fileName, configuration);
                continue;
            }
            YamlConfig itemConfig = new YamlConfig(rootDirectory, (fileName + ".yml"), false);
            saveResource("categories" + File.separator + fileName + ".yml", false);
            FileConfiguration configuration = itemConfig.getConfig();
            categoriesList.put(fileName, configuration);
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    public static Economy getEconomy() {
        return econ;
    }
}
