package muriplz.kryeittpplugin.commands;


import io.github.niestrat99.advancedteleport.api.ATPlayer;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class SetPostCommand implements CommandExecutor {

    private final KryeitTPPlugin plugin;

    public SetPostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    //  This commands aims to be /SetPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name + ChatColor.WHITE + "You cant execute this command from console.");
            return false;
        } else {
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
            //para el eje X
            int postX = PostAPI.getNearPost(gap,playerX,originX);
            //para el eje Z
            int postZ = PostAPI.getNearPost(gap,playerZ,originZ);
            ATPlayer atPlayer = ATPlayer.getPlayer(player);
            Location location = new Location(player.getWorld(), postX, 215, postZ);
            if (atPlayer.hasMainHome()) {
                atPlayer.moveHome(atPlayer.getMainHome().getName(), location, null);
                player.sendMessage(ChatColor.GREEN+"You have successfully moved your home post at: ("+postX+","+postZ+").");
            }else{
                atPlayer.addHome("home", location, null);
                player.sendMessage(ChatColor.GREEN+"You have successfully set your home post at: ("+postX+","+postZ+"), now this will be your /homepost.");
            }
            return true;
        }
    }
}
