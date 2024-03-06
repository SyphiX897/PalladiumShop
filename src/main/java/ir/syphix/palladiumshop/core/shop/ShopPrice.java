package ir.syphix.palladiumshop.core.shop;

import org.bukkit.configuration.ConfigurationSection;

public class ShopPrice {

    private final double buy;
    private final double sell;
    public ShopPrice(double buy, double sell) {
        this.buy = buy;
        this.sell = sell;
    }

    public double buyPrice() {
        return buy;
    }

    public double sellPrice() {
        return sell;
    }

        public static ShopPrice fromConfig(ConfigurationSection section) {
            return new ShopPrice(section.getDouble("buy"), section.getDouble("sell"));
        }
}
