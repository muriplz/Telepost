package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.Warp;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Set;


public class NearestPostCommand implements CommandExecutor{

    private final KryeitTPPlugin plugin;

    public NearestPostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    //  This commands aims to be /NearestPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player )) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+"You can't execute this command from console.");
            return false;
        }else {
            Player player = (Player) sender;

            // Player has to be in the Overworld
            if(!player.getWorld().getName().equals("world")){
                PostAPI.sendMessage(player,"&cYou have to be in the Overworld to use this command.");
                return false;
            }

            // get Distance between posts from config.yml
            int gap = plugin.getConfig().getInt("distance-between-posts");

            // for the X axis
            int originX = plugin.getConfig().getInt("post-x-location");
            int postX = PostAPI.getNearPost(gap,player.getLocation().getBlockX(),originX);

            // for the Z axis
            int originZ = plugin.getConfig().getInt("post-z-location");
            int postZ = PostAPI.getNearPost(gap,player.getLocation().getBlockZ(),originZ);

            // Get all the warpNames
            HashMap<String, Warp> warps = Warp.getWarps();
            Set<String> warpNames = warps.keySet();
            if(args.length==0) {

                for(String warpName: warpNames){
                    Location postLocation = Warp.getWarps().get(warpName).getLocation();
                    if( postLocation.getBlockX()==postX && postLocation.getBlockZ()==postZ ){
                        PostAPI.sendMessage(player,"&fThe nearest post is on: &6(" + postX + " , " + postZ + ")&f, it's &6"+warpName+"&f.");
                        return true;
                    }
                }
                PostAPI.sendMessage(player,"&fThe nearest post is on: &6(" + postX + " , " + postZ + ")&f.");

            }else if (args.length==1) {
                if(args[0].equals("on")) {
                    if (!plugin.showNearest.contains(player.getUniqueId())){
                        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("The nearest post is on: "+ ChatColor.GOLD+"(" + postX + " , " + postZ + ")"+ChatColor.WHITE+"."));
                        plugin.showNearest.add(player.getUniqueId());
                    }else{
                        PostAPI.sendMessage(player,"&cYou already have the option enabled.");
                    }
                }else if (args[0].equals("off")){
                    if (plugin.showNearest.contains(player.getUniqueId())){
                        plugin.counterNearest.remove(plugin.showNearest.indexOf(player.getUniqueId()));
                        plugin.showNearest.remove(player.getUniqueId());
                    }else{
                        PostAPI.sendMessage(player,"&cYou don't have the option enabled.");
                    }

                }
            }

            return true;
        }
    }

}