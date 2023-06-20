package com.kryeit.storage;

import com.kryeit.storage.bytes.Home;
import com.kryeit.storage.bytes.Post;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IDatabase {
    List<Post> getPosts();

    void addPost(Post post);

    void deletePost(String id);

    void setHome(UUID playerID, Home home);

    void stop();

    Optional<Home> getHome(UUID playerID);

    default Optional<Home> getHome(Player player) {
        return getHome(player.getUniqueId());
    }

    Optional<Post> getPost(String id);

    String dump();
}
