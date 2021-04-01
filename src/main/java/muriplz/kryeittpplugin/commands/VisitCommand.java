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

import java.util.Objects;


public class VisitCommand implements CommandExecutor{

    private final KryeitTPPlugin plugin;

    public VisitCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    //  This commands aims to be /HomePost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name + ChatColor.WHITE + "You cant execute this command from console.");
            return false;
        } else {
            Player player = (Player) sender;
            if (args.length == 0) {
                player.sendMessage("Use /visit <PostName/PlayerName>.");
                return false;
            }

            World world = player.getWorld();
            String arg = args[0];
            Boolean a = Bukkit.getPlayer(arg).isOnline();

            if (args.length == 1 && !a) {
                Player player2 = Bukkit.getPlayer(arg);
                assert player2 != null;
                if (player2.isOnline()) {
                    ATPlayer atPlayer = ATPlayer.getPlayer(player);
                    if (atPlayer.hasHome(arg)) {
                        Location location = atPlayer.getHome(arg).getLocation();
                        player.teleport(new Location(world, location.getBlockX() + 0.5, 215, location.getBlockZ() + 0.5));
                        player.sendMessage(ChatColor.GREEN + "Welcome to " + arg + "'s home post.");
                        return true;
                    } else {
                        player.sendMessage(arg+" has not invited you.");
                        return true;
                    }
                } else {
                    player.sendMessage(ChatColor.GREEN + arg + " is not online.");
                    return true;
                }

            }else{
                if(args.length==1){
                    Warp warp = Warp.getWarps().get(arg);
                    player.teleport(new Location(world, warp.getLocation().getBlockX() + 0.5, 215, warp.getLocation().getBlockZ() + 0.5));
                    player.sendMessage(ChatColor.GREEN + "Welcome to " + arg + ".");
                    return true;
                }
            }
        }
    return true;
    }}
