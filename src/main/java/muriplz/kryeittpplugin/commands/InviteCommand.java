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

import java.util.Timer;
import java.util.TimerTask;

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
                        if (atPlayer.hasHome("home")) {
                            Location location = atPlayer.getHome("home").getLocation();
                            String arg = args[0];
                            Player player2 = Bukkit.getPlayer(arg);
                            assert player2 != null;
                            if(player==player2){
                                player.sendMessage(ChatColor.RED+"You cant invite yourself.");
                                return false;
                            }
                            if(!(player2.isOnline())){
                                player.sendMessage(ChatColor.RED+"The player is not online or does not exist.");
                                return false;
                            }
                            ATPlayer atPlayer2 = ATPlayer.getPlayer(player2);
                            String postinvited = player.getName();
                            atPlayer2.addHome(postinvited,location,null);
                            String name = player.getName();
                            String name2 = player2.getName();
                            player.sendMessage(ChatColor.GREEN+"You have invited "+name2+" to your post.");
                            player2.sendMessage(ChatColor.GREEN+"You have been invited by "+name+" to his post, you have 5 minutes to use /v "+name+".");

                            final Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    atPlayer2.removeHome(postinvited,null);
                                    player.sendMessage(ChatColor.GRAY+"The player"+ChatColor.GREEN+" "+name2+ChatColor.GRAY+" does not have access to your home post anymore.");
                                    timer.cancel();
                                }
                            },300000);
                            return true;
                        }
                    }
                    }
                }


            }
        Player player = (Player) sender;
        player.sendMessage(ChatColor.GRAY+"Use /invite <Player>.");
        return true;
        }
    }
