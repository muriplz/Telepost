package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.Warp;
import muriplz.kryeittpplugin.KryeitTPPlugin;
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

public class UnnamePostCommand implements CommandExecutor {

    private final KryeitTPPlugin plugin;

    public UnnamePostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    // This command aims to be /UnnamePost in-game
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+"You can't execute this command from console.");
        }else {
            Player player = (Player) sender;

            // Permission node
            if(!player.hasPermission("telepost.unnamepost")){
                PostAPI.sendMessage(player, "&cYou don't have permission to use this command.");
                return false;
            }

            // /UnnamePost (this looks for the nearest post)
            if(args.length==0){
                // get Distance between posts from config.yml
                int gap = plugin.getConfig().getInt("distance-between-posts");

                // for the X axis
                int originX = plugin.getConfig().getInt("post-x-location");
                int postX = PostAPI.getNearPost(gap,player.getLocation().getBlockX(),originX);

                // for the Z axis
                int originZ = plugin.getConfig().getInt("post-z-location");
                int postZ = PostAPI.getNearPost(gap,player.getLocation().getBlockZ(),originZ);

                // Get all warps (named posts)
                HashMap<String, Warp> warps = Warp.getWarps();

                // Get all names of named posts and made it into a List
                Set<String> warpNames = warps.keySet();
                List<String> allWarpNames = new ArrayList<>(warpNames);

                for (String warpName : allWarpNames) {
                    Location loc = Warp.getWarps().get(warpName).getLocation();
                    if( loc.getBlockX()==postX && loc.getBlockZ()==postZ ) {
                        Warp.getWarps().get(warpName).delete(null);
                        PostAPI.sendMessage(player,"&aThe &6"+warpName+" &apost has been unnamed.");
                        return true;
                    }
                }
            }


            // /unnamepost <something> , that something must be a warp name or it won't do anything.
            if(args.length==1){
                if(Warp.getWarps().containsKey(args[0])){
                    Warp.getWarps().get(args[0]).delete(null);
                    PostAPI.sendMessage(player,"&aThe &6"+args[0]+"&a post has been unnamed.");
                    return true;
                }else{
                    PostAPI.sendMessage(player,"&cNo posts by that name.");
                }
            }
        }
        return false;
    }}
