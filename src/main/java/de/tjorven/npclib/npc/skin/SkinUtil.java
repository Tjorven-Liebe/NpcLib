package de.tjorven.npclib.npc.skin;

import de.tjorven.npclib.util.mineskin.MineskinClient;
import de.tjorven.npclib.util.mineskin.data.Texture;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class SkinUtil {

    private static final MineskinClient MINESKIN_CLIENT = new MineskinClient("Java/1.0");

    public static CompletableFuture<Skin> createSkin(String name, String url) {
        return MINESKIN_CLIENT.generateUrl(url).thenApplyAsync(mineSkin -> {
            Texture texture = mineSkin.data.texture;
            return new Skin(name, texture.getSignature(), texture.getValue());
        });
    }

    public static CompletableFuture<Skin> createSkin(String name, File file) {
        try {
            return MINESKIN_CLIENT.generateUpload(file).thenApplyAsync(mineSkin -> {
                Texture texture = mineSkin.data.texture;
                return new Skin(name, texture.getSignature(), texture.getValue());
            });
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public static CompletableFuture<Skin> getSkin(UUID uuid) {
        return MINESKIN_CLIENT.generateUser(uuid).thenApplyAsync(mineSkin -> {
            Texture texture = mineSkin.data.texture;
            return new Skin(mineSkin.name, texture.getSignature(), texture.getValue());
        });
    }

}
