package muriplz.telepost.Listeners;

import muriplz.telepost.Telepost;
import muriplz.telepost.commands.PostAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.UUID;

public class onPlayerMove implements Listener {
    public Telepost plugin = Telepost.getInstance();

    public boolean isActionBarEnabled(Player p){
        return plugin.showNearest.contains(p.getUniqueId());
    }

    @EventHandler
    public void OnMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Material material = player.getLocation().getBlock().getType();

        ArrayList<UUID> showNearest = plugin.showNearest;
        ArrayList<UUID> blockFall = plugin.blockFall;
        ArrayList<Integer> counterNearest = plugin.counterNearest;

        // Prevent first fall damage after teleport
        if(blockFall.contains(player.getUniqueId())){
            if(Bukkit.getEntity(player.getUniqueId()).isOnGround()){
                blockFall.remove(player.getUniqueId());
            }
            if(material== Material.WATER||material==Material.LAVA){
                blockFall.remove(player.getUniqueId());
            }
        }
        if(isActionBarEnabled(player)){
            int index = showNearest.indexOf(player.getUniqueId());
            if(counterNearest.isEmpty()){
                counterNearest.add(index,1);
            }else if(counterNearest.get(index)<27){
                counterNearest.add(index,counterNearest.get(index)+1);
            }else{

                // for the X axis
                int originX = plugin.getConfig().getInt("post-x-location");
                int postX = PostAPI.getNearPost(player.getLocation().getBlockX(),originX);
                // for the Z axis
                int originZ = plugin.getConfig().getInt("post-z-location");
                int postZ = PostAPI.getNearPost(player.getLocation().getBlockZ(),originZ);

                String postName = PostAPI.NearestPostName(player);
                String cords = "(" + postX + " , " + postZ + ")";


                if(postName!=null){
                    sendActionbar(player,PostAPI.getMessage("nearest-message-named").replace("%POST_LOCATION%",cords).replace("%POST_NAME%",postName));
                }else{
                    sendActionbar(player,PostAPI.getMessage("nearest-message").replace("%POST_LOCATION%", cords));
                }
                counterNearest.add(index,0);
            }
        }
    }
    public void sendActionbar(Player player,String message){
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR,TextComponent.fromLegacyText(message));
    }
}
