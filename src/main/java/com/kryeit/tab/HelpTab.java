package com.kryeit.tab;

import com.kryeit.Telepost;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class HelpTab implements TabCompleter {
    public Telepost instance = Telepost.getInstance();

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {

        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            // Getting the Player
            Player player = (Player) sender;

            // Creating the lists with the autocomplete text
            List<String> commands = new ArrayList<>();


            // Adding the autocomplete text
            if(player.hasPermission("telepost.namepost")) {
                commands.add("namepost");
            }
            if(player.hasPermission("telepost.unnamepost")) {
                commands.add("unnamepost");
            }
            commands.add("aliases");
            commands.add("nearestpost");
            commands.add("setpost");
            commands.add("homepost");
            commands.add("visit");
            commands.add("postlist");
            commands.add("invite");

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
