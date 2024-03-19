package ir.syphix.palladiumshop.listener;

import ir.syphix.palladiumshop.core.gui.CustomGuiManager;
import ir.syphix.palladiumshop.gui.SellGui;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        SellGui sellGui = new SellGui();
        CustomGuiManager.sellGuis.put(event.getPlayer().getUniqueId(), sellGui);
    }
}
