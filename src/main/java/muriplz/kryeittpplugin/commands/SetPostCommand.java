package muriplz.kryeittpplugin.commands;


import io.github.niestrat99.advancedteleport.api.ATPlayer;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class SetPostCommand implements CommandExecutor {
    int gap = 800;
    private final KryeitTPPlugin plugin;

    public SetPostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    //  This commands aims to be /SetPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name + ChatColor.WHITE + "You cant execute this command from console.");
            return false;
        } else {

            Player player = (Player) sender;

            int playerX = player.getLocation().getBlockX();
            int playerZ = player.getLocation().getBlockZ();
            //para el eje X
            int postX=0;
            while(true){
                if(playerX>=gap && playerX>0){
                    playerX=playerX-gap;
                    postX+=gap;
                }
                else if(playerX<=-gap && playerX<0){
                    playerX=playerX+gap;
                    postX-=gap;
                }
                else{break;}
            }
            if(playerX>gap/2&&playerX>0){
                postX+=gap;
            }
            if(playerX<-gap/2&&playerX<0){
                postX-=gap;
            }
            //para el eje Z
            int postZ=0;
            while(true){
                if(playerZ>=gap && playerZ>0){
                    playerZ=playerZ-gap;
                    postZ+=gap;
                }
                else if(playerZ<=-gap && playerZ<0){
                    playerZ=playerZ+gap;
                    postZ-=gap;
                }
                else{break;}
            }
            if(playerZ>gap/2&&playerZ>0){
                postZ+=gap;
            }
            if(playerZ<-gap/2&&playerZ<0){
                postZ-=gap;
            }

            ATPlayer atPlayer = ATPlayer.getPlayer(player);
            Location location = new Location(player.getWorld(), postX, 215, postZ);
            if (atPlayer.hasMainHome()) {
                atPlayer.moveHome(atPlayer.getMainHome().getName(), location, null);
                player.sendMessage(ChatColor.GREEN+"You have successfully moved your home post at: ("+postX+","+postZ+").");
            }else{
                atPlayer.addHome("home", location, null);
                player.sendMessage(ChatColor.GREEN+"You have successfully set your home post at: ("+postX+","+postZ+"), now this will be your /homepost.");
            }

            return true;
        }
    }
}
