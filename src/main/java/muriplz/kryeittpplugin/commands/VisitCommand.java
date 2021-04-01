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
            Player player2 = Bukkit.getPlayer(arg);


            if(player2==null) {
                Warp warp = Warp.getWarps().get(arg);
                player.teleport(new Location(world,warp.getLocation().getBlockX()+0.5, 215,warp.getLocation().getBlockZ()+0.5));
                player.sendMessage(ChatColor.GREEN+"Welcome to "+arg+".");
                return true;
                assert false;
            }else{
                if (player2.isOnline()) {
                ATPlayer atPlayer = ATPlayer.getPlayer(player);
                if(atPlayer.hasHome(arg)){
                    Location location = atPlayer.getHome(arg).getLocation();
                    player.teleport(new Location(world,location.getBlockX()+0.5, 215,location.getBlockZ()+0.5));
                    player.sendMessage(ChatColor.GREEN+"Welcome to "+arg+"'s home post.");
                    return true;
                }
            }else{ player.sendMessage(ChatColor.GREEN+"The player "+arg+" is not online or does not exist."); }}
            player.sendMessage("Use /visit <PostName/PlayerName>.");
            return true;
        }
    }

}
