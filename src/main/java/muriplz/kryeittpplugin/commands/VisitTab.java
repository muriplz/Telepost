package muriplz.kryeittpplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class VisitTab implements TabCompleter {
    List<String> playersName = new ArrayList<>();

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        playersName.add("Pangea");
        playersName.add("Fossil");
        playersName.add("Agua");
        playersName.add("Magma");
        playersName.add("Trident");
        playersName.add("Seahorse");
        playersName.add("Rock");
        playersName.add("Extremadura");
        playersName.add("Bee");

        Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
        Bukkit.getServer().getOnlinePlayers().toArray(players);
        int i = 0;
        while (i < players.length) {
            playersName.add(players[i].getName());
            i++;
        }
        String input = args[0].toLowerCase();
        String inputUP = args[0].toUpperCase();
        List<String> completions = null;
        for (String s : playersName){
            if(s.startsWith(input)||s.startsWith(inputUP)){
                if (completions==null){
                    completions= new ArrayList<>();
                }
                completions.add(s);
            }
        }
        if (completions!=null){
            Collections.sort(completions);
        }
        return completions;
        }
}
