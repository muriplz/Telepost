package com.kryeit.Commands;

import com.kryeit.Telepost;
import io.github.niestrat99.advancedteleport.api.Warp;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class PostsList implements CommandExecutor {
    public Telepost instance = Telepost.getInstance();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(instance
                    .name+"You can't execute this command from console.");
            return false;
        }else{

            // Get all names of named posts and made it into a List
            Set<String> warpNames = Warp.getWarps().keySet();

            // If there are no named posts (Warps) then just return false
            if(warpNames.isEmpty()) {
                player.sendMessage(PostAPI.getMessage("no-named-posts"));
                return false;
            }

            // Place all the warpNames into an arraylist
            List<String> allWarpNames = new ArrayList<>(warpNames);

            // Initialize all the TextComponents
            TextComponent messagePosts = new TextComponent();
            TextComponent message;

            // Header
            if(Warp.getWarps().size() < 9) {
                messagePosts.addExtra(PostAPI.getMessage("named-posts-translation")+":");
            } else {
                PostAPI.sendMessage(player, "&7-----------------");
                PostAPI.sendMessage(player, PostAPI.getMessage("named-posts-translation")+" ");
                PostAPI.sendMessage(player, "&7-----------------");
            }

            // Sort all warp names
            java.util.Collections.sort(allWarpNames);

            // Initialize the strings
            String commandString;
            String hoverText;

            int i = 1;
            // Add to messagePosts all components to teleport to every warp
            for (String warpName : allWarpNames) {
                message = new TextComponent( i+". "+ChatColor.GRAY+ PostAPI.idToName(warpName)+"\n");
                commandString = "/visit " + PostAPI.idToName(warpName);
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandString ));
                Location loc = Warp.getWarps().get(warpName).getLocation();
                hoverText = PostAPI.getMessage("hover-postlist").replace("%POST_NAME%",PostAPI.idToName(warpName)).replace("%POST_LOCATION%","("+loc.getBlockX()+" , "+loc.getBlockZ()+")");
                message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText)));
                messagePosts.addExtra(message);
                i++;
            }
            // Send the message with the Text components to the player
            player.spigot().sendMessage(messagePosts);
            if(Warp.getWarps().size() >= 9) {
                PostAPI.sendMessage(player, "&7-----------------------");
            }
            return true;
        }
    }
}
