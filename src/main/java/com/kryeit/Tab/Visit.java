package com.kryeit.Tab;

import com.kryeit.Commands.PostAPI;
import com.kryeit.Telepost;
import io.github.niestrat99.advancedteleport.api.Warp;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Visit implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        int size = args.length;

        if(size == 0) {
            return new ArrayList<>();
        }

        // Initialize Lists

        // completions is the returned Lists, starts empty
        List<String> completions = new ArrayList<>();

        // Add all names of named posts and initialize allTabs
        List<String> allTabs = new ArrayList<>(Warp.getWarps().keySet());

        // Get the name of all online players and add it to allTabs
        Bukkit.getOnlinePlayers().forEach(p -> allTabs.add(p.getName()));

        Player player = (Player) sender;

        // Add all offline player's names if the player has the right permission node
        if(player.hasPermission("telepost.visit.others") && size == 1) {
            allTabs.addAll(Telepost.getInstance().offlineHomed);
        }

        String aux;

        // Add to "completions" all words that have letters that are contained on "commands" list
        for (String allTab : allTabs) {

            allTab = PostAPI.idToName(allTab);
            aux = allTab;

            for( int i = 0 ; i < size - 1 ; i++ ) {
                allTab = allTab.replaceFirst("(?i)"+args[i]+" ","");
            }

            if (aux.toLowerCase().startsWith(PostAPI.getPostName(args).toLowerCase())) {
                completions.add(allTab);
            }
        }
        return completions;

}}
