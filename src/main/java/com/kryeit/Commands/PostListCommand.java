package com.kryeit.commands;

import com.kryeit.Telepost;
import com.kryeit.storage.bytes.Post;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.*;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class PostListCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Telepost instance = Telepost.getInstance();

        if (!(sender instanceof Player player)) {
            Bukkit.getConsoleSender().sendMessage(instance.name + "You can't execute this command from console.");
            return false;
        }

        List<Post> posts = Telepost.getDB().getPosts();

        // If there are no named posts (Warps) then just return false
        if (posts.isEmpty()) {
            player.sendMessage(PostAPI.getMessage("no-named-posts"));
            return false;
        }
        Collections.sort(posts);

        int pages = (int) Math.ceil((double) posts.size() / 10);
        int page;

        if(args.length == 0) {
            page = 1;
        } else if(args.length == 1) {
            page = Integer.parseInt(args[0]);
        } else return false;

        if(page > pages || page < 1) {
            PostAPI.sendMessage(player,"&cPage doesn't exist");
            return false;
        }

        // Header
        TextComponent messagePosts = new TextComponent();

        PostAPI.sendMessage(player, "                       ");
        PostAPI.sendMessage(player, PostAPI.getMessage("named-posts-translation"));
        PostAPI.sendMessage(player, "-----------------");

        int j = 1;
        for(int i = (page - 1) * 10 + 1 ; i <= posts.size() ; i++) {
            String name = posts.get(i - 1).name();
            TextComponent message = new TextComponent(i + ". " + ChatColor.GRAY + name + "\n");
            String commandString = "/visit " + name;
            message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, commandString));
            Location loc = posts.get(i - 1).location();
            String hoverText = PostAPI.getMessage("hover-postlist").replace("%POST_NAME%", name).replace("%POST_LOCATION%", "(" + loc.getBlockX() + ", " + loc.getBlockZ() + ")");
            message.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(hoverText)));
            messagePosts.addExtra(message);
            if(j == 10) break;
            j++;
        }

        player.spigot().sendMessage(messagePosts);

        BaseComponent[] leftArrow;
        if(hasPreviousPage(page)) {
            leftArrow = new ComponentBuilder("")
                    .append("<<")
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to see page " + (page - 1))
                            .color(ChatColor.LIGHT_PURPLE)
                            .create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/postlist " + (page - 1)))
                    .color(ChatColor.GOLD)
                    .create();
        } else {
            leftArrow = new ComponentBuilder("")
                    .append("<<")
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("There is no previous page")
                            .color(ChatColor.LIGHT_PURPLE)
                            .create()))
                    .color(ChatColor.GRAY)
                    .create();
        }


        BaseComponent[] rightArrow;
        if(hasNextPage(page,pages)) {
            rightArrow = new ComponentBuilder("")
                    .append(">>")
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("Click to see page " + (page + 1))
                            .color(ChatColor.LIGHT_PURPLE)
                            .create()))
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/postlist " + (page + 1)))
                    .color(ChatColor.GOLD)
                    .create();
        } else {
            rightArrow = new ComponentBuilder("")
                    .append(">>")
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("There is no next page")
                            .color(ChatColor.LIGHT_PURPLE)
                            .create()))
                    .color(ChatColor.GRAY)
                    .create();
        }

        BaseComponent[] pagination = new ComponentBuilder("")
                .append("Total posts: " + posts.size())
                .color(ChatColor.LIGHT_PURPLE)
                .append(" || ")
                .color(ChatColor.GOLD)
                .append("----")
                .color(ChatColor.GREEN)
                .append(leftArrow)
                .append("--")
                .color(ChatColor.GREEN)
                .append(rightArrow)
                .append("----")
                .color(ChatColor.GREEN)
                .create();

        player.spigot().sendMessage(pagination);
        return true;
    }

    public boolean hasPreviousPage(int currentPage) {
        return currentPage != 1;
    }

    public boolean hasNextPage(int currentPage, int maxPages) {
        return currentPage < maxPages;
    }
}
