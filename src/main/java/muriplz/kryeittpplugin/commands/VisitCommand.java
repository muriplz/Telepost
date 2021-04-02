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
            World world = player.getWorld();
            // /visit
            if (args.length == 0) {
                player.sendMessage("Use /visit <PostName/PlayerName>.");
                return false;
            } // /visit <something>
            if (args.length==1){
                // /visit <Warp/Named Post>
                if(args[0].equals("Pangea") || args[0].equals("Fossil") || args[0].equals("Agua") || args[0].equals("Magma") || args[0].equals("Trident") || args[0].equals("Seahorse") || args[0].equals("Extremadura") || args[0].equals("Rock") || args[0].equals("BEE")){
                    Warp warp = Warp.getWarps().get(args[0]);
                    player.teleport(new Location(world, warp.getLocation().getBlockX() + 0.5, 215, warp.getLocation().getBlockZ() + 0.5));
                    player.sendMessage(ChatColor.GRAY + "Welcome to " + args[0] + ".");
                    return true;
                }
                // /visit <Yourself>
                if(player.getName().equals(args[0])){
                    ATPlayer atPlayer = ATPlayer.getPlayer(player);
                    Location location = atPlayer.getHome("home").getLocation();
                    player.teleport(new Location(world, location.getBlockX() + 0.5, 215, location.getBlockZ() + 0.5));
                    player.sendMessage(ChatColor.GREEN + "Welcome to your home post.");
                    return true;
                }
                // /visit <Player post>
                if(!(Bukkit.getPlayer(args[0])==null)){
                    ATPlayer atPlayer = ATPlayer.getPlayer(player);
                    // Check if the sender has the home created named by the invitor
                    if (atPlayer.hasHome(args[0])) {
                        Location location = atPlayer.getHome(args[0]).getLocation();
                        player.teleport(new Location(world, location.getBlockX() + 0.5, 215, location.getBlockZ() + 0.5));
                        player.sendMessage(ChatColor.GREEN + "Welcome to " + args[0] + "'s home post.");
                        return true;
                    }else{
                        player.sendMessage(ChatColor.RED+args[0]+" has not invited you.");
                        return false;
                    }
                }else{
                    player.sendMessage(ChatColor.RED+"That is not a valid post, try another.");
                    return false;
                }

            }}return true;}}




