package ir.syphix.palladiumshop.listener;

import ir.syphix.palladiumshop.core.gui.CustomGuiManager;
import ir.syphix.palladiumshop.gui.MainGui;
import ir.syphix.palladiumshop.gui.SellGui;
import ir.syrent.origin.paper.Origin;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        CustomGuiManager.sellGuiHashMap().put(event.getPlayer().getUniqueId(), new SellGui());
        CustomGuiManager.mainGuiHashMap().put(event.getPlayer().getUniqueId(), new MainGui());
    }
}
