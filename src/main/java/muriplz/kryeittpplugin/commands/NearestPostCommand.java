package muriplz.kryeittpplugin.commands;

import muriplz.kryeittpplugin.KryeitTPPlugin;
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

    //  This commands aims to be /NearestPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player )) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+ ChatColor.WHITE+"You cant execute this command from console.");
            return false;
        }else {
            Player player = (Player) sender;
            if(!player.getWorld().getName().equals("world")){
                player.sendMessage(ChatColor.RED+"You have to be in the Overworld to use this command.");
                return false;
            }
            int gap = plugin.getConfig().getInt("distance-between-posts");
            int originX = plugin.getConfig().getInt("post-x-location");
            int originZ = plugin.getConfig().getInt("post-z-location");
            int playerX = player.getLocation().getBlockX()-originX;
            int playerZ = player.getLocation().getBlockZ()-originZ;
            //for the X axis
            int postX=PostAPI.getNearPost(gap,playerX,originX);
            //for the Z axis
            int postZ=PostAPI.getNearPost(gap,playerZ,originZ);
            player.sendMessage( ChatColor.GRAY + "The nearest post is on:" + ChatColor.GREEN + " (" + postX + " , " + postZ + ")" + ChatColor.GRAY + "." );
            return true;
        }
    }

}