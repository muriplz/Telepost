package muriplz.kryeittpplugin.commands;

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


public class NearestPostCommand implements CommandExecutor{

    public KryeitTPPlugin instance = KryeitTPPlugin.getInstance();
    //  This commands aims to be /NearestPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player )) {
            Bukkit.getConsoleSender().sendMessage(instance.name+"You can't execute this command from console.");
            return false;
        }else {
            Player player = (Player) sender;

            // Player has to be in the Overworld
            if(!player.getWorld().getName().equals("world")){
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
                    PostAPI.sendMessage(player,PostAPI.getMessage("nearestpost-message") + "(" + postX + " , " + postZ + ")&f, it's &6"+postName+"&f.");
                }else{
                    PostAPI.sendMessage(player,PostAPI.getMessage("nearestpost-message") + "(" + postX + " , " + postZ + ")&f.");
                }

            }else if (args.length==1) {
                if(args[0].equals("on")) {
                    if (!instance.showNearest.contains(player.getUniqueId())){
                        String postName = PostAPI.NearestPostName(player);
                        if(postName!=null){
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',"The nearest post is on: &6(" + postX + " , " + postZ + ")&f, it's &6"+postName+"&f.")));
                        }else{
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&',"The nearest post is on: &6(" + postX + " , " + postZ + ")&f.")));
                        }
                        instance.showNearest.add(player.getUniqueId());
                    }else{
                        PostAPI.sendMessage(player,PostAPI.getMessage("nearestpost-already-on"));
                    }
                }else if (args[0].equals("off")){
                    if (instance.showNearest.contains(player.getUniqueId())){
                        instance.counterNearest.remove(instance.showNearest.indexOf(player.getUniqueId()));
                        instance.showNearest.remove(player.getUniqueId());
                    }else{
                        PostAPI.sendMessage(player,PostAPI.getMessage("nearestpost-already-off"));
                    }

                }
            }
            return true;
        }
    }

}