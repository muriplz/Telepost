package muriplz.kryeittpplugin.Listeners;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class onPlayerLeave implements Listener {
    private final KryeitTPPlugin plugin;
    public onPlayerLeave(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }
    public void onPlayerQuit(PlayerQuitEvent e){
        if(e instanceof Player){
            UUID uuid = e.getPlayer().getUniqueId();
            plugin.blockFall.remove(uuid);
            plugin.counterNearest.remove(plugin.showNearest.indexOf(uuid));
            plugin.showNearest.remove(uuid);
        }
    }
}
