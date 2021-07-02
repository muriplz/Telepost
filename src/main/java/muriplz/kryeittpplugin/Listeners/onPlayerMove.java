package muriplz.kryeittpplugin.Listeners;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class onPlayerMove implements Listener {
    private final KryeitTPPlugin plugin;

    public onPlayerMove(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }
    @EventHandler
    public void checkIfOnWater(PlayerMoveEvent event) {
        Player player = (Player) event.getPlayer();
        Material material = player.getLocation().getBlock().getType();
        if(material== Material.WATER||Material.LEGACY_STATIONARY_WATER==material||material==Material.LAVA||Material.LEGACY_STATIONARY_LAVA==material){
            plugin.blockFall.remove(player.getUniqueId());
        }
    }
}
