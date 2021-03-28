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
    int playerX, playerZ, postnumberLocationX, postnumberLocationZ, getnearpostX, getnearpostZ;
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

            playerX = player.getLocation().getBlockX();
            playerZ = player.getLocation().getBlockZ();
            postnumberLocationX =  playerX / gap;
            postnumberLocationZ = playerZ / gap;
            getnearpostX = postnumberLocationX * gap;
            getnearpostZ = postnumberLocationZ * gap;

            if (playerX - getnearpostX >= gap / 2) {
                getnearpostX = getnearpostX + gap;
                postnumberLocationX += 1;
            }

            if (playerZ - getnearpostZ >= gap / 2) {
                getnearpostZ = getnearpostZ + gap;
                postnumberLocationZ += 1;
            }
            ATPlayer atPlayer = ATPlayer.getPlayer(player);
            Location location = new Location(player.getWorld(), getnearpostX, 215, getnearpostZ);
            if (atPlayer.hasMainHome()) {
                atPlayer.moveHome(atPlayer.getMainHome().getName(), location, null);
                player.sendMessage(ChatColor.GREEN+"You have successfully moved your home post at: ( "+getnearpostX+","+getnearpostZ+").");
            }else{
                atPlayer.addHome("home", location, null);
                player.sendMessage(ChatColor.GREEN+"You have successfully set your home post at: ( "+getnearpostX+","+getnearpostZ+").");
            }
            return true;
        }
    }
}
