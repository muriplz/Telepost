package muriplz.telepost.commands;

import io.github.niestrat99.advancedteleport.api.Warp;
import io.github.niestrat99.advancedteleport.sql.WarpSQLManager;
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


public class NamePost implements CommandExecutor {

    public Telepost instance = Telepost.getInstance();
    public String worldName = "world";
    // This command aims to be /NamePost in-game
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name+PostAPI.getMessage("cant-execute-from-console"));
        }else {
            Player player = (Player) sender;
            if(args.length==0){
                PostAPI.sendMessage(player,PostAPI.getMessage("namepost-usage"));
                return false;
            }
            if(!player.hasPermission("telepost.namepost")){
                player.sendMessage(PostAPI.getMessage("no-permission"));
                return false;
            }
            if(!player.getWorld().getName().equals(worldName)){
                player.sendMessage(PostAPI.getMessage("not-on-overworld"));
                return false;
            }
            Location nearestPost = PostAPI.getNearPostLocation(player);
            // For the X axis
            int postX = nearestPost.getBlockX();

            // For the Z axis
            int postZ = nearestPost.getBlockZ();

            String postName = PostAPI.getPostName(args);
            String postID = PostAPI.getPostID(args);

            if(Warp.getWarps().containsKey(postID)){
                player.sendMessage(PostAPI.getMessage("named-post-already-exists").replace("%POST_NAME%",postName));
                return false;
            }

            Location nearestpostLocation = new Location(player.getWorld(), postX , 265, postZ ,0,0);

            List<String> warpNames = new ArrayList<>(Warp.getWarps().keySet());

            for(String warpName: warpNames){
                if(Warp.getWarps().get(warpName).getLocation().getBlockX()==postX&&Warp.getWarps().get(warpName).getLocation().getBlockZ()==postZ&&!instance.getConfig().getBoolean("multiple-names-per-post")){
                    player.sendMessage(PostAPI.getMessage("nearest-already-named").replace("%POST_NAME%",warpName));
                    return false;
                }
            }

            WarpSQLManager.get().addWarp(new Warp(player.getUniqueId(),
                    args[0],
                    nearestpostLocation,
                    System.currentTimeMillis(),
                    System.currentTimeMillis()), callback ->
                    player.sendMessage(PostAPI.getMessage("name-post").replace("%POST_NAME%",postName)));
            return true;
        }
        return false;
    }}
