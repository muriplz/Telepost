package muriplz.telepost.commands;

import muriplz.telepost.Telepost;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;


public class NearestPost implements CommandExecutor{

    Telepost plugin = Telepost.getInstance();
    public String worldName = Telepost.getInstance().getConfig().getString("world-name");

    //  This commands aims to be /NearestPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! (sender instanceof Player )) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+"You can't execute this command from console.");
            return false;
        }else {
            Player player = (Player) sender;

            // Player has to be in the Overworld
            if(!player.getWorld().getName().equals(worldName)){
                player.sendMessage(PostAPI.getMessage("not-on-overworld"));
                return false;
            }

            Location nearestPost = PostAPI.getNearPostLocation(player);
            // For the X axis
            int postX = nearestPost.getBlockX();

            // For the Z axis
            int postZ = nearestPost.getBlockZ();

            if(args.length==0) {

                String postName = PostAPI.NearestPostName(player);
                if(postName!=null){
                    player.sendMessage(PostAPI.colour(PostAPI.getMessage("nearest-message-named").replace("%POST_LOCATION%","(" + postX + " , " + postZ + ")").replace("%POST_NAME%",postName)));
                }else {
                    player.sendMessage(PostAPI.getMessage("nearest-message").replace("%POST_LOCATION%", "(" + postX + " , " + postZ + ")"));
                }
            }

            return true;
        }
    }

}