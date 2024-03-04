package ir.syphix.palladiumshop.command;

import cloud.commandframework.Command.Builder;
import cloud.commandframework.arguments.standard.StringArgument;
import ir.syphix.palladiumshop.PalladiumShop;
import ir.syphix.palladiumshop.core.CustomGuiManager;
import ir.syrent.origin.paper.Origin;
import ir.syrent.origin.paper.command.Command;
import ir.syrent.origin.paper.command.interfaces.ISender;
import ir.syrent.origin.paper.utils.ComponentUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class OpenGuiCommand extends Command {

    public OpenGuiCommand() {
        super("palladiumshop", "ps");
        List<String> playersList = new ArrayList<>();
        for (Player player : Origin.getOnlinePlayers()) {
            playersList.add(player.getName());
        }

        Builder<ISender> command = getBuilder()
                .permission(getPermission("opengui"))
                .argument(
                        StringArgument.<ISender>builder("menu")
                                .withSuggestionsProvider((context, input) -> PalladiumShop.configList.keySet().stream().toList())
                )
                .argument(
                        StringArgument.<ISender>builder("player")
                                .asOptional()
                                .withSuggestionsProvider((context, input) -> playersList)
                )
                .handler(context -> {
                    Player player = context.getSender().player();
                    Optional<String> targetOptional = context.getOptional("player");
                    String menuName = context.get("menu");

                    if (targetOptional.isPresent()) {
                        Player targetPlayer = Bukkit.getPlayerExact(targetOptional.get());
                        if (player == null) return;
                        if (targetPlayer == null) {
                            player.sendMessage(ComponentUtils.component("<gradient:dark_red:red>This player does not exist!"));
                            return;
                        }

                        targetPlayer.openInventory(CustomGuiManager.getCustomGuiById(menuName).getInventory());

                        return;
                    }

                    if (player == null) return;
                    player.openInventory(CustomGuiManager.getCustomGuiById(menuName).getInventory());
                });
        saveCommand(command);

    }
}
