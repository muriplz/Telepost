package com.kryeit.storage;

import com.kryeit.storage.bytes.Home;
import com.kryeit.storage.bytes.Post;
import com.kryeit.storage.bytes.ReadableByteArray;
import com.kryeit.storage.bytes.WritableByteArray;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.DBIterator;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class LevelDBImpl implements IDatabase {
    private final DB postsDB;
    private final DB homesDB;

    public LevelDBImpl() {
        postsDB = createDB("posts");
        homesDB = createDB("homes");
    }

    private static DB createDB(String name) {
        Options options = new Options();
        options.createIfMissing(true);
        options.cacheSize(4_194_304); // 4 MB

        try {
            return Iq80DBFactory.factory.open(new File("plugins/Telepost/db/" + name), options);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void stop() {
        try {
            postsDB.close();
            homesDB.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<Home> getHome(UUID playerID) {
        WritableByteArray key = new WritableByteArray(16);
        key.writeUUID(playerID);
        byte[] bytes = homesDB.get(key.toByteArray());
        return bytes == null ? Optional.empty() : Optional.of(Home.fromBytes(new ReadableByteArray(bytes)));
    }

    @Override
    public Optional<Post> getPost(String id) {
        WritableByteArray key = new WritableByteArray();
        key.writeString(id.toLowerCase());
        byte[] bytes = postsDB.get(key.toByteArray());
        return bytes == null ? Optional.empty() : Optional.of(Post.fromBytes(id, new ReadableByteArray(bytes)));
    }

    @Override
    public String dump() {
        StringBuilder builder = new StringBuilder();
        builder.append("Posts:\n");
        for (Post post : getPosts()) {
            builder.append(MessageFormat.format(" -{0}: {1} at {2}\n", post.id(), post.name(), post.location()));
        }
        builder.append('\n');

        builder.append("Homes:\n");
        try (DBIterator iterator = homesDB.iterator()) {
            for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                Home home = Home.fromBytes(new ReadableByteArray(iterator.peekNext().getValue()));
                builder.append(MessageFormat.format(" -{0}: {1}\n", home.playerID(), home.location()));
            }
        } catch (IOException ignored) {
        }
        return builder.toString();
    }

    @Override
    public List<Post> getPosts() {
        List<Post> posts = new ArrayList<>();
        try (DBIterator iterator = postsDB.iterator()) {
            for (iterator.seekToFirst(); iterator.hasNext(); iterator.next()) {
                String id = new ReadableByteArray(iterator.peekNext().getKey()).readString();
                posts.add(Post.fromBytes(id, new ReadableByteArray(iterator.peekNext().getValue())));
            }
        } catch (IOException ignored) {
        }
        return posts;
    }

    @Override
    public void addPost(Post post) {
        WritableByteArray key = new WritableByteArray();
        key.writeString(post.id());
        postsDB.put(key.toByteArray(), post.toBytes());
    }

    @Override
    public void deletePost(String id) {
        WritableByteArray key = new WritableByteArray();
        key.writeString(id.toLowerCase());
        postsDB.delete(key.toByteArray());
    }

    @Override
    public void setHome(UUID playerID, Home home) {
        WritableByteArray key = new WritableByteArray(16);
        key.writeUUID(playerID);
        homesDB.put(key.toByteArray(), home.toBytes());
    }
}
