package muriplz.kryeittpplugin.commands;


import io.github.niestrat99.advancedteleport.api.ATPlayer;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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

    public void sendActionBarOrChat(Player player,String message){
        // This will send the message on the action bar, so it looks cooler
        if(plugin.getConfig().getBoolean("send-arrival-messages-on-action-bar")){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(message));
        }else{
            PostAPI.sendMessage(player,message);
        }
    }

    //  This commands aims to be /SetPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name + "You can't execute this command from console.");
            return false;
        } else {

            // Get the Player
            Player player = (Player) sender;

            // If the command is not /setpost ONLY then return false
            if(args.length!=0){
                PostAPI.sendMessage(player,"Use /setpost.");
                return false;
            }

            if(!player.getWorld().getName().equals("world")){
                sendActionBarOrChat(player, ChatColor.translateAlternateColorCodes('&',"&cYou have to be in the Overworld to use this command."));
                return false;
            }

            // for the X axis
            int originX = plugin.getConfig().getInt("post-x-location");
            int postX = PostAPI.getNearPost(player.getLocation().getBlockX(),plugin,originX);

            // for the Z axis
            int originZ = plugin.getConfig().getInt("post-z-location");
            int postZ = PostAPI.getNearPost(player.getLocation().getBlockZ(),plugin,originZ);

            // get atPlayer (from AdvancedTeleport API)
            ATPlayer atPlayer = ATPlayer.getPlayer(player);

            // Location of the nearest post
            Location location = new Location(player.getWorld(), postX, 265, postZ,0,0);

            // moving the home if he already has one
            if (atPlayer.hasMainHome()) {
                atPlayer.moveHome(atPlayer.getMainHome().getName(), location, null);
                // Actually this part does not work (TODO)
                sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&fYou have successfully moved your home post at: &6("+postX+","+postZ+")&f."));
            }else{

                // setting the post for the first time
                atPlayer.addHome("home", location, null);
                sendActionBarOrChat(player,ChatColor.translateAlternateColorCodes('&',"&fYou have successfully set your home post at: &6("+postX+","+postZ+")&f."));
            }
            return true;
        }
    }
}
