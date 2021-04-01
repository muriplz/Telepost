package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import io.github.niestrat99.advancedteleport.api.Home;
import io.github.niestrat99.advancedteleport.api.Warp;
import io.github.niestrat99.advancedteleport.commands.ATCommand;
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

            if (command.getLabel().equalsIgnoreCase("visit")) {
                if (args.length > 0) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        String arg = args[0];
                        ATPlayer atPlayer = ATPlayer.getPlayer(player);
                        if(atPlayer.hasHome(arg)){
                            Location location = atPlayer.getHome(arg).getLocation();
                            World world = player.getWorld();
                            player.teleport(new Location(world,location.getBlockX()+0.5,(double) 215,location.getBlockZ()+0.5));
                            player.sendMessage(ChatColor.GREEN+"You have arrived to "+arg+"'s home post.");
                            return true;
                        }
                        boolean a = true;
                        while (a) {
                            HashMap<String,Warp>() = Warp.getWarps();
                        }
                    }else{
                        player.sendMessage("3");
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
