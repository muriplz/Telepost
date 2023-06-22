package com.kryeit.tab;

import com.kryeit.Telepost;
import com.kryeit.commands.PostAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class UnnamePost implements TabCompleter {

    public Telepost instance = Telepost.getInstance();

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        instance = Telepost.getInstance();

        if (args.length == 0) {
            return new ArrayList<>();
        }
        // completions is the returned Lists, starts empty
        List<String> completions = new ArrayList<>();

        // Add all names of named posts and initialize allTabs
        List<String> allTabs = new ArrayList<>();
        Telepost.getDB().getPosts().forEach(p -> allTabs.add(p.name()));

        // Add to "completions" all words that have letters that are contained on "commands" list
        for (String allTab : allTabs) {

            String aux = allTab;

            for (int i = 0 ; i < args.length - 1 ; i++) {
                allTab = allTab.replaceFirst("(?i)" + args[i] + " ", "");
            }

            if (aux.toLowerCase().startsWith(PostAPI.getPostName(args).toLowerCase())) {
                completions.add(allTab);
            }
        }
        return completions;
    }
}
