package com.kryeit.Commands;

import com.kryeit.Telepost;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;


public class NearestPost implements CommandExecutor{

    Telepost plugin = Telepost.getInstance();
    public String worldName = "world";
    //  This commands aims to be /NearestPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! (sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+"You can't execute this command from console.");
            return false;
        }else {

            // Player has to be in the Overworld
            if(!player.getWorld().getName().equals(worldName)){
                player.sendMessage(PostAPI.getMessage("not-on-overworld"));
                return false;
            }

            double worldBorderRadius = Objects.requireNonNull(Bukkit.getServer().getWorld(worldName)).getWorldBorder().getSize()/2;

            Location nearestPost = PostAPI.getNearPostLocation(player);
            // For the X axis
            int postX = nearestPost.getBlockX();

            // For the Z axis
            int postZ = nearestPost.getBlockZ();

            if(postX > worldBorderRadius || postZ > worldBorderRadius){
                player.sendMessage(PostAPI.colour("&cThe nearest post is outside the world border, try somewhere else"));
                return false;
            }

            if(args.length==0) {

                String postName = PostAPI.getNearestPostID(player);
                if(postName!=null){
                    player.sendMessage(PostAPI.colour(PostAPI.getMessage("nearest-message-named").replace("%POST_LOCATION%","(" + postX + " , " + postZ + ")").replace("%POST_NAME%",PostAPI.idToName(postName))));
                }else {
                    player.sendMessage(PostAPI.getMessage("nearest-message").replace("%POST_LOCATION%", "(" + postX + " , " + postZ + ")"));
                }
            }

            return true;
        }
    }

}