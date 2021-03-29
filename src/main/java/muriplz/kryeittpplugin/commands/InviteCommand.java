package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class InviteCommand implements CommandExecutor {

    private final KryeitTPPlugin plugin;

    public InviteCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }

    //  This commands aims to be /Invite in-game
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!(sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name + ChatColor.WHITE + "You cant execute this command from console.");
            return false;
        } else {
            Player player = (Player) sender;
            ATPlayer atPlayer = ATPlayer.getPlayer(player);
            if (command.getLabel().equalsIgnoreCase("invite")) {

                if (args.length > 0) {
                    if (Bukkit.getPlayer(args[0]) != null) {
                        if (args.length > 1) {
                            if (atPlayer.hasHome("home")) {
                                Location location = atPlayer.getHome("home").getLocation();
                                String arg = args[0];
                                Player player2 = Bukkit.getPlayer(arg);
                                assert player2 != null;
                                ATPlayer atPlayer2 = ATPlayer.getPlayer(player2);
                                String postinvited = player.getName();
                                atPlayer2.addHome(postinvited,location,null);
                                String name = player.getName();
                                String name2 = player2.getName();
                                player.sendMessage(ChatColor.GREEN+"You have invited "+name2+" to your post.");
                                player2.sendMessage(ChatColor.GREEN+"You have been invited by "+name+" to his post, you have 5 minutes to use /visit <"+name+">.");
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                atPlayer2.removeHome(postinvited,null);


                            }
                        }
                    player.sendMessage(ChatColor.RED+"Please, choose a player to invite.");
                    }
                }
            }
            return true;
        }
    }
}