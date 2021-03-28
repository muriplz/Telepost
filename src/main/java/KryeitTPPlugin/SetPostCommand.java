package KryeitTPPlugin;


import io.github.niestrat99.advancedteleport.api.ATPlayer;
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
            player.sendMessage("1");

            playerX = player.getLocation().getBlockX();
            playerZ = player.getLocation().getBlockZ();
            postnumberLocationX =  playerX / gap;
            postnumberLocationZ = playerZ / gap;
            getnearpostX = postnumberLocationX * gap;
            getnearpostZ = postnumberLocationZ * gap;
            player.sendMessage("2");
            if (playerX - getnearpostX >= gap / 2) {
                getnearpostX = getnearpostX + gap;
                postnumberLocationX += 1;
            }
            player.sendMessage("3");
            if (playerZ - getnearpostZ >= gap / 2) {
                getnearpostZ = getnearpostZ + gap;
                postnumberLocationZ += 1;
            }
            player.sendMessage("4");
            ATPlayer atPlayer = (ATPlayer) player;
            Location location = new Location(player.getWorld(), getnearpostX, 215, getnearpostZ);
            player.sendMessage("5");
            if (atPlayer.hasMainHome()) {
                player.sendMessage("6");
                atPlayer.moveHome("home", location, null);
            }
            player.sendMessage("7");

            return true;
        }
    }
}
