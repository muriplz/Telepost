package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class HomePostCommand implements CommandExecutor{

    private final KryeitTPPlugin plugin;

    public HomePostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    //  This commands aims to be /HomePost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player )) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+ ChatColor.WHITE+"You cant execute this command from console.");
            return false;
        }else {

            double playerX, playerZ,playerY;
            Player player = (Player) sender;
            ATPlayer atPlayer = ATPlayer.getPlayer(player);
            if(atPlayer.hasHome("home")) {
                Location location = atPlayer.getHome("home").getLocation();
                playerX = location.getBlockX() + 0.5;
                playerZ = location.getBlockZ() + 0.5;
                playerY = location.getBlockY();
                World world = player.getWorld();
                Location newlocation =new Location(world,playerX,playerY,playerZ);
                player.teleport(newlocation);
                player.sendMessage(ChatColor.GREEN+"Welcome to your home post.");
            }else{
                player.sendMessage(ChatColor.GREEN+"Please, set a post with /SetPost first.");
            }
            return true;
        }
    }

}
