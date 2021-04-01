package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import io.github.niestrat99.advancedteleport.api.Home;
import io.github.niestrat99.advancedteleport.api.Warp;
import io.github.niestrat99.advancedteleport.commands.ATCommand;
import io.github.niestrat99.advancedteleport.commands.warp.WarpsCommand;
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

import java.util.HashMap;


public class VisitCommand implements CommandExecutor{

    private final KryeitTPPlugin plugin;

    public VisitCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    //  This commands aims to be /HomePost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player )) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+ ChatColor.WHITE+"You cant execute this command from console.");
            return false;
        }else {
            Player player = (Player) sender;
            World world = player.getWorld();
            String arg = args[0];

            if (command.getLabel().equalsIgnoreCase("visit")) {
                if (args.length > 0) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        ATPlayer atPlayer = ATPlayer.getPlayer(player);
                        if(atPlayer.hasHome(arg)){
                            Location location = atPlayer.getHome(arg).getLocation();
                            player.teleport(new Location(world,location.getBlockX()+0.5, 215,location.getBlockZ()+0.5));
                            player.sendMessage(ChatColor.GREEN+"You have arrived to "+arg+"'s home post.");
                            return true;
                        }

                    }
                    else{
                        Warp warp = Warp.getWarps().get(arg);
                        Location location = warp.getLocation();
                        Location newlocation = new Location(world,location.getBlockX()+0.5,215,location.getBlockZ()+0.5);
                        player.teleport(newlocation);
                        player.sendMessage(ChatColor.GREEN+"You welcome to "+arg+".");
                    }
                }else{
                    player.sendMessage("2");
                }
            }else{
                player.sendMessage("1");
            }
            return true;
        }
    }

}
