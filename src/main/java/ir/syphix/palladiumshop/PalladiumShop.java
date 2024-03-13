package ir.syphix.palladiumshop;

import ir.syphix.palladiumshop.annotation.AutoInitializerProcessor;
import ir.syphix.palladiumshop.command.OpenGuiCommand;
import ir.syphix.palladiumshop.core.shop.ShopCategories;
import ir.syphix.palladiumshop.item.CustomItems;
import ir.syphix.palladiumshop.listener.InventoryClickListener;
import ir.syphix.palladiumshop.listener.InventoryCloseListener;
import ir.syphix.palladiumshop.message.Messages;
import ir.syphix.palladiumshop.utils.FileManager;
import ir.syrent.origin.paper.Origin;
import ir.syrent.origin.paper.OriginPlugin;
import net.milkbowl.vault.economy.Economy;
import org.apache.logging.log4j.message.Message;
import org.bukkit.plugin.RegisteredServiceProvider;

public final class PalladiumShop extends OriginPlugin {

    private static Economy econ = null;


    @Override
    public void onEnable() {
        saveDefaultConfig();
        new Messages();
        FileManager.addConfigFiles();
        setupEconomy();

        CustomItems.addItems();
        new ShopCategories(FileManager.categories());

        AutoInitializerProcessor.process();
        new OpenGuiCommand();

        Origin.registerListener(new InventoryClickListener());
        Origin.registerListener(new InventoryCloseListener());
    }

    private void setupEconomy() {
        if (!Origin.hasPlugin("Vault")) {
            return;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return;
        }
        econ = rsp.getProvider();
    }

    public static Economy getEconomy() {
        return econ;
    }

    public static String prefix() {
        return getInstance().getConfig().getString("prefix");
    }

}
