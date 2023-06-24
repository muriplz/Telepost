package com.kryeit;

import com.kryeit.commands.*;
import com.kryeit.compat.CompatAddon;
import com.kryeit.leash.onLeash;
import com.kryeit.listeners.onFall;
import com.kryeit.listeners.onGlide;
import com.kryeit.listeners.onKick;
import com.kryeit.listeners.onPlayerMove;
import com.kryeit.storage.CommandDumpDB;
import com.kryeit.storage.IDatabase;
import com.kryeit.storage.LevelDBImpl;
import com.kryeit.storage.PlayerNamedPosts;
import com.kryeit.tab.*;
import com.kryeit.util.ArrayListHashMap;
import io.github.thatsmusic99.configurationmaster.CMFile;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.PluginCommand;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Telepost extends JavaPlugin {

    // All global lists
    public final ArrayList<UUID> blockFall = new ArrayList<>();
    public final HashMap<UUID, UUID> leashed = new HashMap<>();
    public final List<String> offline = new ArrayList<>();

    PluginDescriptionFile pdffile = getDescription();
    public final String name = ChatColor.YELLOW + "[" + ChatColor.WHITE + pdffile.getName() + ChatColor.YELLOW + "]" + ChatColor.WHITE;
    public final String version = pdffile.getVersion();

    public static Telepost instance;

    public final ArrayListHashMap<UUID, UUID> invites = new ArrayListHashMap<>();

    public PlayerNamedPosts playerNamedPosts;
    public IDatabase database;

    @Override
    public void onLoad() {
        database = new LevelDBImpl();
        try {
            playerNamedPosts = new PlayerNamedPosts("plugins/Telepost/PlayerPosts");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public YamlConfiguration messages;

    @Override
    public void onEnable() {

        // Set the config.yml file
        loadConfig();

        // Set the messages.yml file
        loadMessages();

        // Set the structures folder
        setupStructureData();

        instance = this;

        // Register all commands and tab completions
        registerCommands();

        // Register events
        registerEvents();

        messages = YamlConfiguration.loadConfiguration(new File(getInstance().getDataFolder(), "messages.yml"));

        // Plugin activated at this point
        Bukkit.getConsoleSender().sendMessage(name + ChatColor.GRAY + " The plugin has been activated. Version: " + ChatColor.GREEN + version);
    }

    @Override
    public void onDisable() {
        database.stop();
        Bukkit.getConsoleSender().sendMessage(name + ChatColor.WHITE + " The plugin has been deactivated.");
    }

    private void loadConfig() {
        CMFile myConfigFile = new CMFile(this, "config") {
            @Override
            public void loadDefaults() {
                addLink("GitHub", "https://github.com/Kryeit/Telepost");
                addLink("Modrinth", "https://modrinth.com/plugin/telepost");

                addComment("This number has to be higher than 0. (default = 800 blocks)");
                addDefault("distance-between-posts", 800);

                addComment("First post's X coordinate (default -> x = 0)");
                addDefault("post-x-location", 0);

                addComment("First post's Z coordinate (default -> z = 0)");
                addDefault("post-z-location", 0);

                addComment("The width of the post, with center on /nearestpost. Only odd, don't even. ( default = 5 blocks, 2 to each coordinate + the center )\n" +
                        "Please, keep in mind that default.nbt (or any other post .nbt) should have the same width as stated here.");
                addDefault("post-width", 5);

                addComment("/homepost and /visit have this feature, this launches you to the sky before teleporting. ( default = true )");
                addDefault("launch-feature", true);

                addComment("After using a TP command you get teleported to y = 265 if this is true, if not, it will teleport you to ground level. ( default = true )");
                addDefault("tp-in-the-air", true);

                addComment("Shows the message on the Action Bar. ( doesn't work for all commands ) ( default = true )");
                addDefault("messages-on-action-bar", true);

                addComment("If leashed entities get teteported");
                addDefault("teleport-leashed", true);

                addComment("ClaimBlocks needed to name a post. This needs to have GriefDefender installed on the server.");
                addDefault("needed-blocks", 40000);
            }
        };
        myConfigFile.load();
    }

    private void loadMessages() {
        CMFile myMessagesFile = new CMFile(this, "messages") {
            @Override
            public void loadDefaults() {
                addLink("GitHub", "https://github.com/Kryeit/Telepost");
                addLink("Spigot", "https://www.spigotmc.org/resources/telepost.91988");

                addComment("Usage:");
                addDefault("namepost-usage", "&fUse /NamePost <PostName>.");
                addDefault("visit-usage", "&fUse /visit <PostName/PlayerName>.");
                addDefault("setpost-usage", "&fUse /setpost.");
                addDefault("invite-usage", "&fUse /invite <Player>.");
                addDefault("homepost-usage", "&fUse /HomePost.");

                addComment("Global:");
                addDefault("cant-execute-from-console", "You can't execute this command from console.");
                addDefault("no-permission", "&cYou don't have permission to use this command.");
                addDefault("not-on-overworld", "&cYou have to be in the Overworld to use this command.");
                addDefault("not-inside-post", "&cYou have to be inside a post.");

                addComment("/PostList:");
                addDefault("named-posts-translation", "&6Named posts");
                addDefault("hover-postlist", "&fClick to teleport to &7%POST_NAME% &fpost.\nThis post is at &6%POST_LOCATION%&f.");

                addComment("/SetPost:");
                addDefault("set-post-success", "&fYou have successfully set your home post at: &6%POST_LOCATION%&f.");
                addDefault("move-post-success", "&fYou have successfully moved your home post at: &6%POST_LOCATION%&f.");

                addComment("/HomePost or /Visit:");
                addDefault("already-at-homepost", "&cYou are already at your home post.");
                addDefault("named-post-arrival", "&fWelcome to &6%POST_NAME%&f.");
                addDefault("invited-home-arrival", "&fWelcome to &6%PLAYER_NAME%&f's home post.");
                addDefault("own-homepost-arrival", "&fWelcome to your home post.");
                addDefault("homepost-without-setpost", "&fPlease, set a post with &6/SetPost&f first.");
                addDefault("visit-not-invited", "&cYou have not been invited.");
                addDefault("no-homepost", "&cYou do not have a home post yet.");
                addDefault("already-invited-post", "&cYou are already at his/her home post.");
                addDefault("already-at-namedpost", "&cYou are already in&6 %POST_NAME%&c.");
                addDefault("unknown-post", "&6%POST_NAME%&f's post does not exist.");
                addDefault("block-above", "&cYou have blocks above of you, try on the post structure.");

                addComment("/NearestPost:");
                addDefault("nearest-message", "&fThe nearest post is on: &6%POST_LOCATION%&f. It's &6%DISTANCE% &7blocks away.");
                addDefault("nearest-message-named", "&fThe nearest post is on: &6%POST_LOCATION%&f, it's &6%POST_NAME%&f. It's &6%DISTANCE% &fblocks away.");

                addComment("/Invite:");
                addDefault("own-invite", "&cYou can't invite yourself.");
                addDefault("not-found", "&cPlayer not found.");
                addDefault("invite-expire", "&6%PLAYER_NAME%&f does not have access to your home post anymore.");
                addDefault("inviting", "&fYou have invited &6%PLAYER_NAME%&f.");
                addDefault("invited", "&fYou have been invited by &6%PLAYER_NAME%&f.");

                addComment("/UnnamePost:");
                addDefault("unname-named-post", "&6%POST_NAME% &apost has been unnamed.");
                addDefault("no-such-post", "&cNo posts by that name.");

                addComment("/NamePost:");
                addDefault("not-enough-claimblocks","&cYou don't have enough claimblocks to name this post, you need %CLAIM_BLOCKS% CB's");
                addDefault("nearest-already-named", "&cThe nearest post is already named, it's &6%POST_NAME%&c.");
                addDefault("no-named-posts", "&cThere are no named posts.");
                addDefault("name-post", "&fYou have given the name &6%POST_NAME%&f to the nearest post.");
                addDefault("named-post-already-exists", "&cThe post &6%POST_NAME%&c already exists.");

                addComment("Post Building");
                addDefault("unnamed-post","Unnamed post");
            }
        };
        myMessagesFile.load();
    }

    public static Telepost getInstance() {
        return instance;
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new onGlide(), this);
        getServer().getPluginManager().registerEvents(new onFall(), this);
        getServer().getPluginManager().registerEvents(new onPlayerMove(), this);
        getServer().getPluginManager().registerEvents(new onKick(), this);
        if (getConfig().getBoolean("teleport-leashed")) {
            getServer().getPluginManager().registerEvents(new onLeash(), this);
        }
    }

    public void registerCommands() {

        if(CompatAddon.GRIEF_DEFENDER.isLoaded()) {
            registerCommand("claimposts", new ClaimPostsCommand());
        }
        registerCommand("buildposts", new BuildPostsCommand());
        // /nearestpost
        registerCommand("nearestpost", new NearestPostCommand());

        // /setpost
        registerCommand("setpost", new SetPostCommand());

        // /homepost
        registerCommand("homepost", new HomePostCommand());

        // /invite <Player>
        registerCommand("invite", new InviteCommand(), new InviteTab());

        // /visit <NamedPost/Player>
        registerCommand("visit", new VisitCommand(), new VisitTab());

        // /namepost <Name>
        registerCommand("namepost", new NamePostCommand());

        // /unnamepost (Name)
        registerCommand("unnamepost", new UnnamePostCommand(), new UnnamePost());

        // /posthelp (command)
        // <> means that has to be used, () is optional
        registerCommand("posthelp", new HelpCommand(), new HelpTab());

        // /postlist
        registerCommand("postlist", new PostListCommand(), new PostListTab());

        // /dumpdb
        registerCommand("dumpdb", new CommandDumpDB());

    }

    private void registerCommand(String name, CommandExecutor executor) {
        registerCommand(name, executor, new ReturnEmptyTab());
    }

    private void registerCommand(String name, CommandExecutor executor, TabCompleter tabCompleter) {
        PluginCommand command = getCommand(name);
        if (command == null)
            throw new RuntimeException("Failed to register command \"" + name + "\"! Add it to plugin.yml!");

        command.setExecutor(executor);
        if (tabCompleter != null) command.setTabCompleter(tabCompleter);
    }

    public void setupStructureData() {
        // Create "structures" directory if it doesn't exist
        File structuresFolder = new File(getDataFolder(), "structures");
        if (!structuresFolder.exists()) {
            structuresFolder.mkdirs();
        }

        // Create "default.nbt" file if it doesn't exist
        File defaultNbtFile = new File(structuresFolder, "default.nbt");
        if (!defaultNbtFile.exists()) {
            try (InputStream in = getResource("default.nbt")) {
                Files.copy(in, defaultNbtFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                // Handle exception
                e.printStackTrace();
            }
        }
        Path sourcePath = Paths.get(getDataFolder().getAbsolutePath(), "structures", "default.nbt");
        Path worldDirectory = Bukkit.getServer().getWorldContainer().toPath();
        Path targetPath = worldDirectory.resolve(Paths.get("world", "generated", "minecraft", "structures", "default.nbt"));

        try {
            Files.createDirectories(targetPath.getParent());
            Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public static IDatabase getDB() {
        return getInstance().database;
    }
}
