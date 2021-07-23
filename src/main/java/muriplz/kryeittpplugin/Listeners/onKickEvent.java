package muriplz.kryeittpplugin.Listeners;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;

public class onKickEvent implements Listener {
    private final KryeitTPPlugin plugin;

    public onKickEvent(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void onKick(PlayerKickEvent event) {
        Player p = event.getPlayer();
        if(event.getReason().contains("Fly")||event.getReason().contains("fly")){
            if(plugin.blockFall.contains(p.getUniqueId())){
                event.setCancelled(true);
            }
        }
    }
}
