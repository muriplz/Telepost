package muriplz.telepost.commands;

import io.github.niestrat99.advancedteleport.api.Warp;
import muriplz.telepost.Telepost;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class UnnamePost implements CommandExecutor {

    public Telepost instance = Telepost.getInstance();

    public String worldName = "world";
    // This command aims to be /UnnamePost in-game
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name+PostAPI.getMessage("cant-execute-from-console"));
        }else {
            Player player = (Player) sender;

            // Permission node
            if(!player.hasPermission("telepost.unnamepost")){
                player.sendMessage(PostAPI.getMessage("no-permission"));
                return false;
            }



            // /UnnamePost (this looks for the nearest post)
            if(args.length==0){

                if(!player.getWorld().getName().equals(worldName)){
                    player.sendMessage(PostAPI.getMessage("not-on-overworld"));
                    return false;
                }

                Location nearestPost = PostAPI.getNearPostLocation(player);
                // For the X axis
                int postX = nearestPost.getBlockX();

                // For the Z axis
                int postZ = nearestPost.getBlockZ();

                // Get all warps (named posts)
                HashMap<String, Warp> warps = Warp.getWarps();

                // Get all names of named posts and made it into a List
                Set<String> warpNames = warps.keySet();
                List<String> allWarpNames = new ArrayList<>(warpNames);

                for (String warpName : allWarpNames) {
                    Location loc = Warp.getWarps().get(warpName).getLocation();
                    if( loc.getBlockX()==postX && loc.getBlockZ()==postZ ) {
                        Warp.getWarps().get(warpName).delete(null);
                        player.sendMessage(PostAPI.getMessage("unname-named-post").replace("%POST_NAME%",PostAPI.idToName(warpName)));
                        return true;
                    }
                }
            }


            // /unnamepost <something> , that something must be a warp name or it won't do anything.
            if(args.length >= 1){

                String postName = PostAPI.getPostName(args);
                String postID = PostAPI.getPostID(args);

                if(Warp.getWarps().containsKey(postID)){
                    Warp.getWarps().get(args[0]).delete(null);
                    player.sendMessage(PostAPI.getMessage("unname-named-post").replace("%POST_NAME%",postName));
                    return true;
                }else{
                    player.sendMessage(PostAPI.getMessage("no-such-post"));
                }
            }
        }
        return false;
    }}
