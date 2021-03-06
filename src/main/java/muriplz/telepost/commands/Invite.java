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

import java.util.Timer;
import java.util.TimerTask;

public class Invite implements CommandExecutor {

    Telepost instance = Telepost.getInstance();


    // This commands aims to be /Invite in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + "You can't execute this command from console.");
            return false;
        }

        Player player = (Player) sender;
        ATPlayer atPlayer = ATPlayer.getPlayer(player);

        if (args.length == 1) {
            if (Bukkit.getPlayer(args[0]) != null) {
                if (atPlayer.hasHome("home")) {
                    Location location = atPlayer.getHome("home").getLocation();
                    String arg = args[0];
                    Player player2 = Bukkit.getPlayer(arg);
                    if(player2==null){
                        player.sendMessage(PostAPI.getMessage("not-found"));
                        return false;
                    }
                    if(player==player2){
                        player.sendMessage(PostAPI.getMessage("own-invite"));
                        return false;
                    }
                    ATPlayer atPlayer2 = ATPlayer.getPlayer(player2);
                    String postinvited = player.getName();

                    atPlayer2.addHome(postinvited,location,null);
                    player.sendMessage(PostAPI.getMessage("inviting").replace("%PLAYER_NAME%",player2.getName()));
                    player2.sendMessage(PostAPI.getMessage("invited").replace("%PLAYER_NAME%",player.getName()));
                    final Timer timer = new Timer();
                    timer.schedule(new TimerTask() {
                        @Override
                        public void run() {
                            atPlayer2.removeHome(postinvited,null);
                            player.sendMessage(PostAPI.getMessage("invite-expire").replace("%PLAYER_NAME%",player2.getName()));
                            timer.cancel();
                        }
                    },300000);
                    return true;
                }
            }
        }
        player.sendMessage(PostAPI.getMessage("invite-usage"));

        return true;
        }
    }
