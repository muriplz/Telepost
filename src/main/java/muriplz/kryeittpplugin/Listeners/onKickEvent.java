package muriplz.kryeittpplugin.Listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class onKickEvent implements Listener {
    @EventHandler
    public void onKick(PlayerKickEvent event) {
        if(event.getReason().contains("Fly")||event.getReason().contains("fly")){
            event.setCancelled(true);
        }
    }
}
