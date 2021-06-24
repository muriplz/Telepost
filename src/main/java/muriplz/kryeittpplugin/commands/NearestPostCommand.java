package muriplz.kryeittpplugin.commands;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;



public class NearestPostCommand implements CommandExecutor{

    private final KryeitTPPlugin plugin;

    public NearestPostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    public void sendActionBarOrChat(Player player,String message){
        // This will send the message on the action bar, so it looks cooler
        if(plugin.getConfig().getBoolean("send-arrival-messages-on-action-bar")){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        }else{
            PostAPI.sendMessage(player,message);
        }
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
                sendActionBarOrChat(player, ChatColor.translateAlternateColorCodes('&',"&cYou have to be in the Overworld to use this command."));
                return false;
            }

            // get Distance between posts from config.yml
            int gap = plugin.getConfig().getInt("distance-between-posts");

            // for the X axis
            int originX = plugin.getConfig().getInt("post-x-location");
            int postX=PostAPI.getNearPost(gap,player.getLocation().getBlockX(),originX);

            // for the Z axis
            int originZ = plugin.getConfig().getInt("post-z-location");
            int postZ=PostAPI.getNearPost(gap,player.getLocation().getBlockZ(),originZ);

            sendActionBarOrChat(player, ChatColor.translateAlternateColorCodes('&',"&aThe nearest post is on: &6(" + postX + " , " + postZ + ")&a."));
            return true;
        }
    }

}