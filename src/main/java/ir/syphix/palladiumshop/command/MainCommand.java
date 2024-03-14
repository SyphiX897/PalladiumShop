package ir.syphix.palladiumshop.command;

import ir.syphix.palladiumshop.PalladiumShop;
import ir.syphix.palladiumshop.core.gui.CustomGui;
import ir.syphix.palladiumshop.core.gui.CustomGuiManager;
import ir.syphix.palladiumshop.message.Messages;
import ir.syphix.palladiumshop.utils.Utils;
import ir.syrent.origin.paper.Origin;
import ir.syrent.origin.paper.command.OriginCommand;
import ir.syrent.origin.paper.command.interfaces.SenderExtension;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.incendo.cloud.Command;
import org.incendo.cloud.parser.standard.StringParser;
import org.incendo.cloud.suggestion.SuggestionProvider;

import java.util.Optional;

public class MainCommand extends OriginCommand {

    public MainCommand() {
        super("palladiumshop", "ps");
        errorPrefix(Component.empty());


        Command.Builder<SenderExtension> menu = getBuilder()
                .literal("menu")
                .required("menu", StringParser.stringParser(), SuggestionProvider.suggestingStrings(CustomGuiManager.getGuis().stream().map(CustomGui::id).filter(content -> content != "sell_gui").toList()))
                .optional("player", StringParser.stringParser(), SuggestionProvider.suggestingStrings(Bukkit.getOnlinePlayers().stream().map(Player::getName).toList()))
                .permission("palladiumshop.opengui")
                .handler(context -> {
                    Player player = context.sender().player();
                    if (player == null) return;
                    Optional<String> targetOptional = context.optional("player");
                    CustomGui gui = CustomGuiManager.getCustomGuiById(context.get("menu"));
                    if (gui == null) {
                        player.sendMessage(Utils.toFormattedComponent(Messages.MENU_NOT_FOUND));
                        return;
                    }

                    if (targetOptional.isPresent()) {
                        Player targetPlayer = Bukkit.getPlayerExact(targetOptional.get());
                        if (targetPlayer == null) {
                            player.sendMessage(Utils.toFormattedComponent(Messages.PLAYER_NOT_FOUND));
                            return;
                        }

                        targetPlayer.openInventory(gui.inventory());
                        return;
                    }

                    player.openInventory(gui.inventory());
                });
        getManager().command(menu);

        Command.Builder<SenderExtension> reload = getBuilder()
                .literal("reload")
                .handler(context -> {
                    Player player = context.sender().player();
                    Origin.getPlugin().reloadConfig();
                    PalladiumShop.initialize();
                    if (player == null) return;
                    player.sendMessage(Utils.toFormattedComponent(Messages.RELOAD));
                });
        getManager().command(reload);


    }
}
