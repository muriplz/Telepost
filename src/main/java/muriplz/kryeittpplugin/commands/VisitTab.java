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
    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length==1){
            List<String> playersName = new ArrayList<>();
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (int i = 0; i < players.length; i++) {
                playersName.add(players[i].getName());

            }
            playersName.add("Pangea");
            playersName.add("Fossil");
            playersName.add("Agua");
            playersName.add("Magma");
            playersName.add("Trident");
            playersName.add("Seahorse");
            playersName.add("Rock");
            playersName.add("Extremadura");
            playersName.add("BEE");
            return playersName;
        }
    return null;}
}
