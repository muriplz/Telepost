package com.kryeit.Tab;

import com.kryeit.Commands.PostAPI;
import io.github.niestrat99.advancedteleport.api.Warp;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UnnamePost implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length == 0) {
            return new ArrayList<>();
        }
        // completions is the returned Lists, starts empty
        List<String> completions = new ArrayList<>();

        // Add all names of named posts and initialize allTabs
        List<String> allTabs = new ArrayList<>(Warp.getWarps().keySet());


        String aux;

        // Add to "completions" all words that have letters that are contained on "commands" list
        for (String allTab : allTabs) {

            allTab = PostAPI.idToName(allTab);
            aux = allTab;

            for(int i = 0 ; i < args.length - 1 ; i++) {
                allTab = allTab.replaceFirst("(?i)"+args[i]+" ","");
            }

            if (aux.toLowerCase().startsWith(PostAPI.getPostName(args).toLowerCase())) {
                completions.add(allTab);
            }
        }
        return completions;
    }}
