package KryeitTPPlugin;

import com.griefdefender.api.GriefDefender;
import com.griefdefender.api.claim.Claim;
import com.griefdefender.api.permission.flag.Flag;
import com.griefdefender.api.util.generator.DummyObjectProvider;
import io.github.niestrat99.advancedteleport.api.ATPlayer;
import io.github.niestrat99.advancedteleport.sql.SQLManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class SetPostCommand implements CommandExecutor {
    int playerX, playerZ, gap, postnumberLocationX, postnumberLocationZ, getnearpostX, getnearpostZ;

    private final KryeitTPPlugin plugin;

    public SetPostCommand(KryeitTPPlugin plugin) {
        this.plugin = plugin;
    }
    //  This commands aims to be /SetPost in-game
    public boolean onCommand(@NotNull CommandSender sender,@NotNull Command command,@NotNull String label,@NotNull String[] args) {

        if( ! ( sender instanceof Player)) {
            Bukkit.getConsoleSender().sendMessage(plugin.name+ ChatColor.WHITE+"You cant execute this command from console.");
            return false;
        }else {
            Player player = (Player) sender;
            playerX = player.getLocation().getBlockX();
            playerZ = player.getLocation().getBlockZ();
            postnumberLocationX = playerX / gap;
            postnumberLocationZ = playerZ / gap;
            getnearpostX = postnumberLocationX * gap;
            getnearpostZ = postnumberLocationZ * gap;

            if ( playerX - getnearpostX >= gap/2 ) {
                getnearpostX = getnearpostX + gap;
                postnumberLocationX += 1;
            }

            if ( playerZ - getnearpostZ >= gap/2 ) {
                getnearpostZ = getnearpostZ + gap;
                postnumberLocationZ += 1;
            }
            ATPlayer atPlayer = ATPlayer.getPlayer(player);
            boolean hashome = atPlayer.hasHome("home");
            if(hashome){
                atPlayer.removeHome("home", new SQLManager.SQLCallback<>() {
                    @Override
                    public void onSuccess(Boolean data) {player.sendMessage("TEST: Works.");
                    }
                        //Here I have to delete the GD perms to entity-teleport-to that post, in case he has the perms.(TODO)


                    @Override
                    public void onFail() {
                        player.sendMessage(ChatColor.RED+"Error deleting your last home: 001aa.");
                    }
                        });
            }
            double nearpostX = getnearpostX + 0.5;
            double nearpostZ = getnearpostZ + 0.5;
            World world = player.getWorld();
            Location location = new Location(world, nearpostX, (215) , nearpostZ);
            atPlayer.addHome("home", location, new SQLManager.SQLCallback<>() {
                @Override
                public void onSuccess(Boolean aBoolean) {
                    player.sendMessage(ChatColor.GRAY+"You have successfully moved into the post at:"+ChatColor.GREEN + " (" + getnearpostX +  " , " + getnearpostZ +  ")" + ChatColor.GRAY + ".");
                }

                @Override
                public void onFail() {
                    player.sendMessage(ChatColor.RED+"Error moving your home: 002aa.");
                }
            });
            //I call the claim containing the nearest post. (I think this is the way¿?)
            Claim claim = GriefDefender.getCore().getClaimManager(player.getWorld().getUID()).getClaimAt(getnearpostX,215,getnearpostZ);
            //Here I call the UUID of the player (In case he changes the nickname).
            UUID uuid = player.getUniqueId();
            //Here I call the flag, entity-teleport-to
            public static final Flag ENTITY_TELEPORT_TO = DummyObjectProvider.createFor(Flag.class, "entity-teleport-to");
            //Here I have to add GD perms to entity-teleport-to , in case he does not have them.(TODO)

        return true;
    }
}
}
