package muriplz.kryeittpplugin.commands;

import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class NearestPostCommand implements CommandExecutor{
    //  This variable is the gap between posts

    private final KryeitTPPlugin plugin;

    public NearestPostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    //  This commands aims to be /NearestPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player )) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+ ChatColor.WHITE+"You cant execute this command from console.");
            return false;
        }else {
            Player player = (Player) sender;
            //TODO, this has to come from a config.yml file
            int gap = plugin.getConfig().getInt("distance-between-posts");
            int originX = plugin.getConfig().getInt("post-x-location");
            int originZ = plugin.getConfig().getInt("post-z-location");
            int playerX = player.getLocation().getBlockX()-originX;
            int playerZ = player.getLocation().getBlockZ()-originZ;
            //para el eje X
            int postX=0;
            while(true){
                if(playerX>=gap && playerX>0){
                    playerX=playerX-gap;
                    postX+=gap;
                }
                else if(playerX<=-gap && playerX<0){
                    playerX=playerX+gap;
                    postX-=gap;
                }
                else{break;}
            }
            if(playerX>gap/2&&playerX>0){
                postX+=gap;
            }
            if(playerX<-gap/2&&playerX<0){
                postX-=gap;
            }
            //para el eje Z
            int postZ=0;
            while(true){
                if(playerZ>=gap && playerZ>0){
                    playerZ=playerZ-gap;
                    postZ+=gap;
                }
                else if(playerZ<=-gap && playerZ<0){
                    playerZ=playerZ+gap;
                    postZ-=gap;
                }
                else{break;}
            }
            if(playerZ>gap/2&&playerZ>0){
                postZ+=gap;
            }
            if(playerZ<-gap/2&&playerZ<0){
                postZ-=gap;
            }
            postX+=originX;
            postZ+=originZ;
            player.sendMessage( ChatColor.GRAY + "The nearest post is on:" + ChatColor.GREEN + " (" + postX + " , " + postZ + ")" + ChatColor.GRAY + "." );
            return true;
        }
    }

}