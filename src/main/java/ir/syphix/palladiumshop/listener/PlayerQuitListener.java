package ir.syphix.palladiumshop.listener;

import ir.syphix.palladiumshop.core.gui.CustomGuiManager;
import ir.syphix.palladiumshop.utils.SkullUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {


    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        CustomGuiManager.sellGuiHashMap().remove(event.getPlayer().getUniqueId());
        CustomGuiManager.mainGuiHashMap().remove(event.getPlayer().getUniqueId());
    }
}
