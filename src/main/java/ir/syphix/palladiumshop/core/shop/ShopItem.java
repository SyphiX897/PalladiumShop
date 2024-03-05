package ir.syphix.palladiumshop.core.shop;

import ir.syphix.palladiumshop.PalladiumShop;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

public class ShopItem {

        Material material;
        ShopPrice shopPrice;

        public ShopItem(Material material, ShopPrice shopPrice) {
            this.material = material;
            this.shopPrice = shopPrice;
        }

        public void buy(Player player, int amount) {
            PalladiumShop.getEconomy().withdrawPlayer(player, shopPrice.buy * amount);
        }

        public void sell(Player player, int amount) {
            PalladiumShop.getEconomy().depositPlayer(player, shopPrice.sell * amount);
        }

        public static ShopItem fromConfig(ConfigurationSection section) {
            return new ShopItem(
                    Material.valueOf(section.getString("material")),
                    ShopPrice.fromConfig(section.getConfigurationSection("price"))
            );
        }

}
