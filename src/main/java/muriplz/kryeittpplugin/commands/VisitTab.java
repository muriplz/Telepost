package muriplz.kryeittpplugin.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class VisitTab implements TabCompleter {
    List<String> argument = new ArrayList<>();
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
        playersName.add("BEE");

        Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
        Bukkit.getServer().getOnlinePlayers().toArray(players);
        int i = 0;
        while (i < players.length) {
            playersName.add(players[i].getName());
            i++;
        }
        if(args.length==1){
            for (String a : playersName){
                if(a.toLowerCase().startsWith(args[0].toLowerCase())){
                    argument.add(a);
                }
                return argument;
            }
        }
        return playersName;
        }
}
