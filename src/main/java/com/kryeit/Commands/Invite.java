package com.kryeit.Commands;

import com.kryeit.Telepost;
import io.github.niestrat99.advancedteleport.api.ATPlayer;
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
        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + "You can't execute this command from console.");
            return false;
        }

        ATPlayer atPlayer = ATPlayer.getPlayer(player);

        if (args.length != 1) {
            player.sendMessage(PostAPI.getMessage("invite-usage"));
            return false;
        }

        Player inviting = Bukkit.getPlayer(args[0]);

        if (inviting == null) {
            player.sendMessage(PostAPI.getMessage("not-found"));
            return false;
        }
        if (atPlayer.hasHome("home")) {
            Location location = atPlayer.getHome("home").getLocation();

            if(player == inviting) {
                player.sendMessage(PostAPI.getMessage("own-invite"));
                return false;
            }

            ATPlayer atPlayer2 = ATPlayer.getPlayer(inviting);
            String postinvited = player.getName();

            atPlayer2.addHome(postinvited,location,null);
            player.sendMessage(PostAPI.getMessage("inviting").replace("%PLAYER_NAME%",inviting.getName()));
            inviting.sendMessage(PostAPI.getMessage("invited").replace("%PLAYER_NAME%",player.getName()));
            final Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    atPlayer2.removeHome(postinvited,null);
                    player.sendMessage(PostAPI.getMessage("invite-expire").replace("%PLAYER_NAME%",inviting.getName()));
                    timer.cancel();
                }
            },300000);
            return true;
        }


        return true;
        }
    }
