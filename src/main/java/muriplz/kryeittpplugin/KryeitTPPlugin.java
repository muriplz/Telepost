package muriplz.kryeittpplugin;


import muriplz.kryeittpplugin.commands.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class KryeitTPPlugin extends JavaPlugin implements Listener {
    PluginDescriptionFile pdffile = getDescription();
    public String name = ChatColor.YELLOW+"["+ChatColor.WHITE+pdffile.getName()+ChatColor.YELLOW+"]";
    public String version = pdffile.getVersion();

    public void onEnable(){
        Bukkit.getConsoleSender().sendMessage(name+ChatColor.GRAY+" The plugin has been activated. version: "+ChatColor.GREEN+version+ChatColor.GRAY+".");
        registerCommands();
    }
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(name+ChatColor.WHITE+" The plugin has been deactivated.");
    }
    public void registerCommands() {
        Objects.requireNonNull(this.getCommand("nearestpost")).setExecutor(new NearestPostCommand(this));
        Objects.requireNonNull(this.getCommand("setpost")).setExecutor(new SetPostCommand(this));
        Objects.requireNonNull(this.getCommand("homepost")).setExecutor(new HomePostCommand(this));
        Objects.requireNonNull(this.getCommand("invite")).setExecutor(new InviteCommand(this));
        Objects.requireNonNull(this.getCommand("visit")).setExecutor(new VisitCommand(this));
        Objects.requireNonNull(getCommand("visit")).setTabCompleter(new VisitTab());
    }}


