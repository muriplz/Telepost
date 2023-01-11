package com.kryeit.Tab;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class NearestPost implements TabCompleter {
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        List<String> completions = new ArrayList<>();
        if(args.length == 1) {

            // Creating the lists with the autocomplete text
            List<String> commands = new ArrayList<>();


            // Adding the autocomplete text
            commands.add("on");
            commands.add("off");

            // Add to "completions" all words that have letters that are contained on "commands" list
            for (String allTab : commands) {
                if (allTab.toLowerCase().startsWith(args[0].toLowerCase())) {
                    completions.add(allTab);
                }
            }
            return completions;
        }
        return new ArrayList<>();
    }
}
