package muriplz.telepost.Listeners;

import muriplz.telepost.Telepost;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class onKick implements Listener {
    public Telepost instance = Telepost.getInstance();

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Player p = event.getPlayer();
        if(event.getReason().toLowerCase().contains("fly")){
            if(instance.blockFall.contains(p.getUniqueId())){
                event.setCancelled(true);
            }
        }
    }
}
