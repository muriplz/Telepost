package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.Warp;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class VisitTab implements TabCompleter {

    @Nullable
    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if(args.length==1){
            List<String> completions = new ArrayList<>();
            HashMap<String, Warp> warps = Warp.getWarps();
            Set<String> warpNames = warps.keySet();
            List<String> commands = new ArrayList<>(warpNames);
            Bukkit.getOnlinePlayers().forEach(p -> commands.add(p.getName()));
            //Sort the list and show it to the player
            int i=0;
            while(i< commands.size()){
                if(commands.get(i).toLowerCase().contains(args[0])||commands.get(i).contains(args[0])){
                    completions.add(commands.get(i));
                }
                i++;
            }
            return completions;
        }
        return null;
}}
