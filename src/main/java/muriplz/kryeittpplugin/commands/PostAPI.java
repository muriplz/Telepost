package muriplz.kryeittpplugin.commands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class PostAPI {
    public static int getNearPost(int gap, int playerX, int originX) {
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
}