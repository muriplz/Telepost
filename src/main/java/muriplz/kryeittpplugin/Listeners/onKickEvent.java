package muriplz.kryeittpplugin.Listeners;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class onKickEvent implements Listener {
    public KryeitTPPlugin instance = KryeitTPPlugin.getInstance();

    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Player p = event.getPlayer();
        if(event.getReason().contains("Fly")||event.getReason().contains("fly")){
            if(instance.blockFall.contains(p.getUniqueId())){
                event.setCancelled(true);
            }
        }
    }
}
