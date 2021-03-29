package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import io.github.niestrat99.advancedteleport.api.Warp;
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

import java.util.MissingResourceException;


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
            ATPlayer atPlayer = ATPlayer.getPlayer(player);
            if (command.getLabel().equalsIgnoreCase("visit")) {
                if (args.length > 0) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        if (args.length > 1) {
                            String arg = args[0];

                            if (arg == "Pangea") {
                                Location location = new Location(player.getWorld(),0.5,215,0.5);
                                player.teleport(location);
                            }
                            
                        }}}


                        }
            return true;
        }
    }

}

