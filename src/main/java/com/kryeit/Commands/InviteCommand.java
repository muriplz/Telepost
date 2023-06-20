package com.kryeit.commands;

import com.kryeit.Telepost;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InviteCommand implements CommandExecutor {
    // This commands aims to be /Invite in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Telepost instance = Telepost.getInstance();

        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + "You can't execute this command from console.");
            return false;
        }

        if (args.length != 1) {
            player.sendMessage(PostAPI.getMessage("invite-usage"));
            return false;
        }

        Player invitee = Bukkit.getPlayer(args[0]);
        if (player == invitee) {
            player.sendMessage(PostAPI.getMessage("own-invite"));
            return false;
        }

        if (invitee == null) {
            player.sendMessage(PostAPI.getMessage("not-found"));
            return false;
        }

        if (Telepost.getDB().getHome(player).isEmpty()) {
            player.sendMessage(PostAPI.getMessage("homepost-without-setpost"));
            return false;
        }

        instance.invites.addValue(player.getUniqueId(), invitee.getUniqueId());

        player.sendMessage(PostAPI.getMessage("inviting").replace("%PLAYER_NAME%", invitee.getName()));
        invitee.sendMessage(PostAPI.getMessage("invited").replace("%PLAYER_NAME%", player.getName()));

        Bukkit.getScheduler().scheduleSyncDelayedTask(Telepost.getInstance(), () -> {
            player.sendMessage(PostAPI.getMessage("invite-expire").replace("%PLAYER_NAME%", invitee.getName()));
            instance.invites.removeValue(player.getUniqueId(), invitee.getUniqueId());
        }, 6_000);
        return true;
    }
}
