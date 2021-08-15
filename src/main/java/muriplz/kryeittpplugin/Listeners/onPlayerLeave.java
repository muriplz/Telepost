package muriplz.kryeittpplugin.Listeners;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class onPlayerLeave implements Listener {
    public KryeitTPPlugin plugin = KryeitTPPlugin.getInstance();

    public void onPlayerQuit(PlayerQuitEvent e){
        if(e instanceof Player){
            UUID uuid = e.getPlayer().getUniqueId();
            plugin.counterNearest.remove(plugin.showNearest.indexOf(uuid));
            plugin.showNearest.remove(uuid);
        }
    }
}
