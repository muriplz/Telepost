package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.Warp;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UnnamePostCommand implements CommandExecutor {

    private final KryeitTPPlugin plugin;

    public UnnamePostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if( ! ( sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+ ChatColor.WHITE+"You cant execute this command from console.");
            return false;
        }else {
            Player player = (Player) sender;
            if(args.length==0){
                player.sendMessage("Use /UnnamePost <PostName> to unname the nearest post.");
                return false;
            }
            if(!player.hasPermission("telepost.unnamepost")){
                player.sendMessage(ChatColor.RED+"You don't have permission to use this command.");
                return false;
            }
            if(args.length==1){
                if(Warp.getWarps().containsKey(args[0])){
                    Warp.getWarps().get(args[0]).delete(null);
                    player.sendMessage(ChatColor.GREEN+"The "+args[0]+" post has been unnamed, to rename it stand close to the desired post and use /namepost "+args[0]+".");
                    return true;
                }else{
                    player.sendMessage(ChatColor.RED+"No posts by that name.");
                }
            }
            return false;
        }
    }}
