# KryeitTPPlugin
This is a Teleporting plugin for Minecraft Java Edition.
1)Dependencies: This plugin requires AdvancedTeleport (https://www.spigotmc.org/resources/advancedteleport.64139/) github: (https://github.com/Niestrat99/AT-Rewritten/)
2)Tested versions: This plugin has been tested on 1.16.5 Mohist server 

# What's a post?
It's a structure you can tp from, but not to. (you can only tp to a named post, your home post or a home post you have been invited to)
There is a post every 800 (default) blocks.

# Commands:
- /nearestpost: Tells you where the nearest post is, based on your position.
- /setpost: Sets a home on the nearest post but on y=260.
- /homepost: Teleports you to your home (That can only be set by /setpost, so disable homes feature on the AdvancedTeleport config.yml)
- /invite <Player>: Invite a player to your home post, this invitation lasts 5 minutes.
- /visit <Player/NamedPost>: Teleports you to an invited post or to a Named Post (to name a post use /setwarp Agua (Extremadura, etc))

This plugin is a WIP, so the Named Post system, the generation of the post itself (as you will have to build everyone of them by hand, FAWE or something like that), and the protection of the post, as well as the fall damage. All of this can be achieved with other plugins.
