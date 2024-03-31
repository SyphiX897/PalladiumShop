package ir.syphix.palladiumshop.command;

import ir.syphix.palladiumshop.annotation.AutoInitializer;
import ir.syphix.palladiumshop.core.gui.CustomGuiManager;
import ir.syphix.palladiumshop.gui.MainGui;
import ir.syrent.origin.paper.command.OriginCommand;
import ir.syrent.origin.paper.command.interfaces.SenderExtension;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.incendo.cloud.Command;


@AutoInitializer
public class ShopCommand extends OriginCommand {
    public ShopCommand() {
        super("shop");
        errorPrefix(Component.empty());

        Command.Builder<SenderExtension> shop = getBuilder()
                .permission("shop")
                .handler(context -> {
                    Player player = context.sender().player();
                    if (player == null) return;
                    player.openInventory(CustomGuiManager.mainGuiByUuid(player.getUniqueId()).getInventory());
                });
        getManager().command(shop);
    }
}
