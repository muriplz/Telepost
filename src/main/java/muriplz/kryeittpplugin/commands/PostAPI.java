package muriplz.kryeittpplugin.commands;

import io.github.thatsmusic99.configurationmaster.CMFile;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PostAPI {
    public static int getNearPost(int gap, int playerX, int originX) {
        playerX-=originX;
        int postX = 0;
        while (true) {
            if (playerX >= gap && playerX > 0) {
                playerX = playerX - gap;
                postX += gap;
            } else if (playerX <= -gap && playerX < 0) {
                playerX = playerX + gap;
                postX -= gap;
            } else {
                break;
            }
        }
        if (playerX > gap / 2 && playerX > 0) {
            postX += gap;
        }
        if (playerX < -gap / 2 && playerX < 0) {
            postX -= gap;
        }
        postX += originX;
        return postX;
    }
    public static void sendMessage(Player player,String message){
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
    public static boolean isPlayerOnPost(Player player,int originX,int originZ,int width,int gap){
        int postX = PostAPI.getNearPost(gap,player.getLocation().getBlockX(),originX);
        int postZ = PostAPI.getNearPost(gap,player.getLocation().getBlockZ(),originZ);
        if(postX>=0){
            if(player.getLocation().getBlockX()<postX-width||player.getLocation().getBlockX()>postX+width){
                return true;
            }
        }
        if(postX<0){
            if(player.getLocation().getBlockX()>postX+width||player.getLocation().getBlockX()<postX-width){
                return true;
            }
        }
        if(postZ>=0){
            if(player.getLocation().getBlockZ()<postZ-width||player.getLocation().getBlockZ()>postZ+width){
                return true;
            }
        }
        if(postZ<0){
            if(player.getLocation().getBlockZ()>postZ+width||player.getLocation().getBlockZ()<postZ-width){
                return true;
            }
        }
        return false;
    }
}