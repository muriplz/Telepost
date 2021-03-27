package KryeitTPPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;


public class NearestPostCommand implements CommandExecutor{
    //  Player location
    int playerX, playerZ;

    //  This is the post number, so the post (2,5) would be the post at the coordinates ( 2 * gap , 5 * gap )
    int postnumberLocationX, postnumberLocationZ;

    //  This is the location of the nearest post
    int getnearpostX, getnearpostZ;

    //  This variable is the gap between posts
    public int gap = 800;

    private final KryeitTPPlugin plugin;

    public NearestPostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    //  This commands aims to be /NearestPost in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player )) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+ ChatColor.WHITE+"You cant execute this command from console.");
            return false;
        }else {
            Player player = (Player) sender;
            playerX = player.getLocation().getBlockX();
            playerZ = player.getLocation().getBlockZ();

            postnumberLocationX = playerX / gap;
            postnumberLocationZ = playerZ / gap;
            getnearpostX = postnumberLocationX * gap;
            getnearpostZ = postnumberLocationZ * gap;

            if ( playerX - getnearpostX >= gap/2 ) {
                getnearpostX = getnearpostX + gap;
                postnumberLocationX += 1;
            }

            if ( playerZ - getnearpostZ >= gap/2 ) {
                getnearpostZ = getnearpostZ + gap;
                postnumberLocationZ += 1;
            }
            player.sendMessage( ChatColor.GRAY + "You are on: (" + playerX + " , " + playerZ + ") and the nearest post is on:" + ChatColor.GREEN + " (" + getnearpostX + " , " + getnearpostZ + ")" + ChatColor.GRAY + "." );

            return true;
        }
    }

}