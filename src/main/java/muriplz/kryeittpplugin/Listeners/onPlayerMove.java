package muriplz.kryeittpplugin.Listeners;

import me.clip.placeholderapi.PlaceholderAPI;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import muriplz.kryeittpplugin.commands.PostAPI;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Objects;

public class onPlayerMove implements Listener {
    public KryeitTPPlugin plugin = KryeitTPPlugin.getInstance();

    @EventHandler
    public void OnMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Material material = player.getLocation().getBlock().getType();





        if(Objects.requireNonNull(Bukkit.getEntity(player.getUniqueId())).isOnGround()){
            plugin.blockFall.remove(player.getUniqueId());
        }
        if(material== Material.WATER||material==Material.LAVA){
            plugin.blockFall.remove(player.getUniqueId());
        }
        if(plugin.showNearest.contains(player.getUniqueId())){
            int index = plugin.showNearest.indexOf(player.getUniqueId());
            if(plugin.counterNearest.isEmpty()){
                plugin.counterNearest.add(index,1);
            }else if(plugin.counterNearest.get(index)<27){
                plugin.counterNearest.add(index,plugin.counterNearest.get(index)+1);
            }else{

                // for the X axis
                int originX = plugin.getConfig().getInt("post-x-location");
                int postX = PostAPI.getNearPost(player.getLocation().getBlockX(),originX);
                // for the Z axis
                int originZ = plugin.getConfig().getInt("post-z-location");
                int postZ = PostAPI.getNearPost(player.getLocation().getBlockZ(),originZ);

                String postName = PostAPI.NearestPostName(player);


                if(postName!=null){
                    sendActionbar(player,PostAPI.getMessage("nearest-message-named").replace("%POST_LOCATION%","(" + postX + " , " + postZ + ")").replace("%POST_NAME%",postName));
                }else{
                    sendActionbar(player,PostAPI.getMessage("nearest-message").replace("%POST_LOCATION%", "(" + postX + " , " + postZ + ")"));
                }
                plugin.counterNearest.add(index,0);
            }
        }
    }
    public void sendActionbar(Player player,String message){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,TextComponent.fromLegacyText(message));
    }
}
