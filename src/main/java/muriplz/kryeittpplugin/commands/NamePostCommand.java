package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.Warp;
import io.github.niestrat99.advancedteleport.sql.WarpSQLManager;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;


public class NamePostCommand implements CommandExecutor {

    public KryeitTPPlugin instance = KryeitTPPlugin.getInstance();


    // This command aims to be /NamePost in-game
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name+"You can't execute this command from console.");
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
            if(!player.getWorld().getName().equals("world")){
                player.sendMessage(PostAPI.getMessage("not-on-overworld"));
                return false;
            }
            if(args.length==1){

                Location nearestPost = PostAPI.getNearPostLocation(player);
                // For the X axis
                int postX = nearestPost.getBlockX();

                // For the Z axis
                int postZ = nearestPost.getBlockZ();

                if(Warp.getWarps().containsKey(args[0])){
                    PostAPI.sendMessage(player,"&cThe post &6"+args[0]+"&c already exists.");
                    return false;
                }
                Location nearestpostLocation = new Location(player.getWorld(), postX , 265, postZ ,0,0);
                HashMap<String, Warp> warps = Warp.getWarps();
                Set<String> warpNames = warps.keySet();
                for(String warpName: warpNames){
                    if(Warp.getWarps().get(warpName).getLocation().getBlockX()==postX&&Warp.getWarps().get(warpName).getLocation().getBlockZ()==postZ&&!instance.getConfig().getBoolean("multiple-names-per-post")){
                        PostAPI.sendMessage(player,"&cThe nearest post is already named, it's &6"+warpName+"&c.");
                        return false;
                    }
                }

                WarpSQLManager.get().addWarp(new Warp(player.getUniqueId(),
                        args[0],
                        nearestpostLocation,
                        System.currentTimeMillis(),
                        System.currentTimeMillis()), callback ->
                        PostAPI.sendMessage(player,"&fYou have named &6"+args[0]+"&f the nearest post."));
                return true;
        }
        }
        return false;
    }}
