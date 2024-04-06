package ir.syphix.palladiumshop.command;

import ir.syphix.palladiumshop.PalladiumShop;
import ir.syphix.palladiumshop.annotation.AutoInitializer;
import ir.syphix.palladiumshop.core.gui.CustomGui;
import ir.syphix.palladiumshop.core.gui.CustomGuiManager;
import ir.syphix.palladiumshop.message.Messages;
import ir.syphix.palladiumshop.utils.TextUtils;
import ir.syrent.origin.paper.Origin;
import ir.syrent.origin.paper.command.OriginCommand;
import ir.syrent.origin.paper.command.interfaces.SenderExtension;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.bukkit.parser.PlayerParser;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.SuggestionProvider;

@SuppressWarnings("unused")
@AutoInitializer
public class MainCommand extends OriginCommand {

    public MainCommand() {
        super("palladiumshop", "ps");
        errorPrefix(Component.empty());

        Command.Builder<SenderExtension> menu = getBuilder()
                .literal("menu")
                .required("menu", StringParser.stringParser(), SuggestionProvider.suggestingStrings(CustomGuiManager.guis().stream().map(CustomGui::id).toList()))
                .optional("player", PlayerParser.playerParser())
                .permission("opengui")
                .handler(context -> {
                    Player player = context.sender().player();
                    if (player == null) return;
                    Player targetPlayer = context.<Player>optional("player").orElse(player);
                    String guiName = context.get("menu");
                    switch (guiName) {
                        case "main_gui" -> player.openInventory(CustomGuiManager.mainGuiByUuid(targetPlayer.getUniqueId()).getInventory());
                        case "sell_gui" -> player.openInventory(CustomGuiManager.sellGuiByUuid(targetPlayer.getUniqueId()).getInventory());
                        default -> {
                            CustomGui gui = CustomGuiManager.guiById(guiName);
                            if (gui == null) {
                                player.sendMessage(TextUtils.toFormattedComponent(Messages.MENU_NOT_FOUND));
                                return;
                            }
                            targetPlayer.openInventory(gui.inventory());
                        }
                    }
                });
        getManager().command(menu);

        Command.Builder<SenderExtension> reload = getBuilder()
                .literal("reload")
                .permission("reload")
                .handler(context -> {
                    Player player = context.sender().player();
                    Origin.getPlugin().reloadConfig();
                    PalladiumShop.initialize();
                    if (player == null) return;
                    player.sendMessage(TextUtils.toFormattedComponent(Messages.RELOAD));
                });
        getManager().command(reload);


    }
}
