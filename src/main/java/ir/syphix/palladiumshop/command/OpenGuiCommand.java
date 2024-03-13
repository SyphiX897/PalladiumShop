package ir.syphix.palladiumshop.command;

import cloud.commandframework.Command.Builder;
import cloud.commandframework.arguments.standard.StringArgument;
import ir.syphix.palladiumshop.core.gui.CustomGui;
import ir.syphix.palladiumshop.core.gui.CustomGuiManager;
import ir.syphix.palladiumshop.message.Messages;
import ir.syphix.palladiumshop.utils.Utils;
import ir.syrent.origin.paper.Origin;
import ir.syrent.origin.paper.command.Command;
import ir.syrent.origin.paper.command.interfaces.ISender;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

public class OpenGuiCommand extends Command {

    public OpenGuiCommand() {
        super("palladiumshop", "ps");

        Builder<ISender> command = getBuilder()
                .argument(
                        StringArgument.<ISender>builder("menu")
                                .withSuggestionsProvider((context, input) -> CustomGuiManager.getGuis().stream().map(CustomGui::id).toList())
                )
                .argument(
                        StringArgument.<ISender>builder("player")
                                .asOptional()
                                .withSuggestionsProvider((context, input) -> Origin.getOnlinePlayers().stream().map(Player::getName).toList())
                )
                .handler(context -> {
                    Player player = context.getSender().player();
                    if (player == null) return;
                    if (!player.hasPermission("palladiumshop.opengui")) {
                        player.sendMessage(Utils.toFormattedComponent(Messages.NEED_PERMISSION));
                        return;
                    }
                    Optional<String> targetOptional = context.getOptional("player");
                    Inventory menu = CustomGuiManager.getCustomGuiById(context.get("menu")).inventory();
                    if (menu == null) {
                        player.sendMessage(Utils.toFormattedComponent(Messages.MENU_NOT_FOUND));
                        return;
                    }

                    if (targetOptional.isPresent()) {
                        Player targetPlayer = Bukkit.getPlayerExact(targetOptional.get());
                        if (targetPlayer == null) {
                            player.sendMessage(Utils.toFormattedComponent(Messages.PLAYER_NOT_FOUND));
                            return;
                        }

                        targetPlayer.openInventory(menu);
                        return;
                    }

                    player.openInventory(menu);
                });
        saveCommand(command);

    }
}
