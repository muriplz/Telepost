package muriplz.kryeittpplugin.commands;


import io.github.niestrat99.advancedteleport.api.ATPlayer;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
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
            Bukkit.getConsoleSender().sendMessage(plugin.name + "You cant execute this command from console.");
            return false;
        } else {
            Player player = (Player) sender;
            if(!player.getWorld().getName().equals("world")){
                PostAPI.sendMessage(player,"&cYou have to be in the Overworld to use this command.");
                return false;
            }
            // get distance between posts from config.yml
            int gap = plugin.getConfig().getInt("distance-between-posts");

            // for the X axis
            int originX = plugin.getConfig().getInt("post-x-location");
            int postX = PostAPI.getNearPost(gap,player.getLocation().getBlockX(),originX);

            // for the Z axis
            int originZ = plugin.getConfig().getInt("post-z-location");
            int postZ = PostAPI.getNearPost(gap,player.getLocation().getBlockZ(),originZ);

            // get atPlayer (from AdvancedTeleport API)
            ATPlayer atPlayer = ATPlayer.getPlayer(player);

            // Location of the nearest post
            Location location = new Location(player.getWorld(), postX, 215, postZ);

            // moving the home if he already has one
            if (atPlayer.hasMainHome()) {
                atPlayer.moveHome(atPlayer.getMainHome().getName(), location, null);
                PostAPI.sendMessage(player,"&aYou have successfully moved your home post at: ("+postX+","+postZ+").");
            }else{

                // setting the post for the first time
                atPlayer.addHome("home", location, null);
                PostAPI.sendMessage(player,"&aYou have successfully set your home post at: ("+postX+","+postZ+"), now this will be your /homepost.");
            }
            return true;
        }
    }
}
