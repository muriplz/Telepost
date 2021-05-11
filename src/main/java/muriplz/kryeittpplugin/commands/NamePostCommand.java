package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.Warp;
import io.github.niestrat99.advancedteleport.sql.WarpSQLManager;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;


public class NamePostCommand implements CommandExecutor {

    private final KryeitTPPlugin plugin;

    public NamePostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+ ChatColor.WHITE+"You cant execute this command from console.");
            return false;
        }else {
            Player player = (Player) sender;
            if(args.length==0){
                player.sendMessage("Use /NamePost <PostName> to name the nearest post.");
                return false;
            }
            if(!player.hasPermission("telepost.namepost")){
                player.sendMessage(ChatColor.RED+"You don't have permission to use this command.");
                return false;
            }
            if(!player.getWorld().getName().equals("world")){
                player.sendMessage(ChatColor.RED+"You have to be in the Overworld to use this command.");
                return false;
            }
            if(args.length==1){
                if(Warp.getWarps().containsKey(args[0])){
                    player.sendMessage(ChatColor.RED+"The post "+args[0]+" already exists, try a different name or unname the post using /UnnamePost Agua.");
                    return false;
                }

                int gap = plugin.getConfig().getInt("distance-between-posts");
                int originX = plugin.getConfig().getInt("post-x-location");
                int originZ = plugin.getConfig().getInt("post-z-location");
                int playerX = player.getLocation().getBlockX()-originX;
                int playerZ = player.getLocation().getBlockZ()-originZ;
                //for the X axis
                int postX = PostAPI.getNearPostX(gap,playerX,originX);
                //for the Z axis
                int postZ = PostAPI.getNearPostZ(gap,playerZ,originZ);
                Location nearestpostLocation = new Location(player.getWorld(), postX , 260, postZ );
                HashMap<String, Warp> warps = Warp.getWarps();
                Set<String> warpNames = warps.keySet();
                for(String warpName: warpNames){
                    if(Warp.getWarps().get(warpName).getLocation().getBlockX()==nearestpostLocation.getBlockX()&&Warp.getWarps().get(warpName).getLocation().getBlockZ()==nearestpostLocation.getBlockZ()&&!plugin.getConfig().getBoolean("multiple-names-per-post")){
                        player.sendMessage(ChatColor.RED+"The nearest post is already named, it's "+warpName+".");
                        return false;
                    }
                }
                final int finalpostX = postX;
                final int finalpostZ = postZ;
                WarpSQLManager.get().addWarp(new Warp(player.getUniqueId(),
                        args[0],
                        nearestpostLocation,
                        System.currentTimeMillis(),
                        System.currentTimeMillis()), callback ->
                        player.sendMessage(ChatColor.GRAY+"You have named to "+ChatColor.GOLD+args[0]+ChatColor.GRAY+" the nearest post "+ChatColor.GREEN+"("+finalpostX+" , "+finalpostZ+" )"+ChatColor.GRAY+". To visit this post use /visit "+args[0]));
                return true;
        }
        return false;
    }
}}
