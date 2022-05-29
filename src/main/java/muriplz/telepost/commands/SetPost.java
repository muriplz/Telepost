package muriplz.telepost.commands;


import io.github.niestrat99.advancedteleport.api.ATPlayer;
import muriplz.telepost.Telepost;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class SetPost implements CommandExecutor {

    public Telepost instance = Telepost.getInstance();


    //  This commands aims to be /SetPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + PostAPI.getMessage("cant-execute-from-console"));
            return false;
        } else {

            // Get the Player
            Player player = (Player) sender;

            // If the command is not /setpost ONLY then return false
            if(args.length!=0){
                player.sendMessage(PostAPI.getMessage("setpost-usage"));
                return false;
            }

            if(!player.getWorld().getName().equals("world")){
                player.sendMessage(PostAPI.getMessage("not-on-overworld"));
                return false;
            }

            double worldBorderRadius = Bukkit.getServer().getWorld("world").getWorldBorder().getSize()/2;

            Location nearestPost = PostAPI.getNearPostLocation(player);
            // For the X axis
            int postX = nearestPost.getBlockX();

            // For the Z axis
            int postZ = nearestPost.getBlockZ();

            if(postX > worldBorderRadius || postZ > worldBorderRadius){
                player.sendMessage(PostAPI.colour("&cThe nearest post is outside the world border, try somewhere else"));
                return false;
            }

            // get atPlayer (from AdvancedTeleport API)
            ATPlayer atPlayer = ATPlayer.getPlayer(player);

            // Location of the nearest post
            Location location = new Location(player.getWorld(), postX, Telepost.getInstance().getConfig().getInt("world-height"), postZ,0,0);

            // Moving the home if he already has one
            if (atPlayer.hasHome("home")) {
                atPlayer.moveHome("home", location, null);
                PostAPI.sendActionBarOrChat(player,PostAPI.getMessage("move-post-success").replace("%POST_LOCATION%","("+postX+","+postZ+")"));
            }else{

                // Setting the post for the first time
                atPlayer.addHome("home", location, null);
                PostAPI.sendActionBarOrChat(player,PostAPI.getMessage("set-post-success").replace("%POST_LOCATION%","("+postX+","+postZ+")"));
            }
            return true;
        }
    }
}
