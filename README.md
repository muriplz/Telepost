<p align="center"><img src="https://user-images.githubusercontent.com/46825658/164082042-940d18c0-4a4f-4dd2-bdf7-bf3cdea93663.png" alt="Logo" width="200"></p>

<h1 align="center">Telepost <br>
	<a href="https://www.spigotmc.org/resources/telepost.91988/"><img src="https://img.shields.io/badge/Spigot-ff5733" alt="Spigot"></a>
	<a href="https://discord.gg/5zQ8RVEzvw"><img src="https://img.shields.io/discord/929394649884405761?color=5865f2&label=Discord&style=flat" alt="Discord"></a></h1>
This is a Teleporting plugin for Minecraft Java Edition. Download: https://www.spigotmc.org/resources/telepost.91988/
 

Tested versions: This plugin has been tested on 1.16.5 Mohist server 

# What's a post?
It's a structure you can tp from, but not to. (you can only tp to a named post, your home post or a home post you have been invited to)
There is a post every 800 (default) blocks.

# Commands:
- /nearestpost: Tells you where the nearest post is, based on your position.
- /setpost: Sets a home on the nearest post but on y=260.
- /homepost: Teleports you to your home (That can only be set by /setpost, so disable homes feature on the AdvancedTeleport config.yml)
- /invite <Player>: Invite a player to your home post, this invitation lasts 5 minutes.
- /visit <Player/NamedPost>: Teleports you to an invited post or to a Named Post (to name a post use /setwarp Agua (Extremadura, etc))
- /namepost <PostName> : gives a name to the nearest post. Only for admins.
- /unnamepost <PostName> : unnames a post. Only for admins.
- /posthelp (Command): sends a message with information about commands.
- /postlist: shows all Named Posts and lets you click them in order to teleport.

# Permissions:
- telepost.homepost : lets you use /homepost everywhere in the Overworld.
- telepost.visit : lets you use /v and /visit everywhere in the Overworld.
- telepost.visit.others : lets you use /visit <Player/OfflinePlayer> without being invited
- telepost.namepost : lets you use /namepost.
- telepost.unnamepost : lets you use /unnamepost.
