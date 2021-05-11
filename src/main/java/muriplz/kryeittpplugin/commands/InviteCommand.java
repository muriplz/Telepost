package muriplz.kryeittpplugin.commands;

import io.github.niestrat99.advancedteleport.api.ATPlayer;
import muriplz.kryeittpplugin.KryeitTPPlugin;
import org.bukkit.Bukkit;
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
            Bukkit.getConsoleSender().sendMessage(plugin.name + "You cant execute this command from console.");
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
                                PostAPI.sendMessage(player,"&cYou cant invite yourself.");
                                return false;
                            }
                            if(!(player2.isOnline())){
                                PostAPI.sendMessage(player,"&cThe player is not online or does not exist.");
                                return false;
                            }
                            ATPlayer atPlayer2 = ATPlayer.getPlayer(player2);
                            String postinvited = player.getName();
                            atPlayer2.addHome(postinvited,location,null);
                            PostAPI.sendMessage(player,"&aYou have invited "+player2.getName()+" to your post.");
                            PostAPI.sendMessage(player2,"&aYou have been invited by "+player.getName()+" to his post, you have 5 minutes to use /v "+player.getName()+".");

                            final Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    atPlayer2.removeHome(postinvited,null);
                                    PostAPI.sendMessage(player,"&7The player &s"+player2.getName()+"&7 does not have access to your home post anymore.");
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
        PostAPI.sendMessage(player,"&fUse /invite <Player>.");
        return true;
        }
    }
