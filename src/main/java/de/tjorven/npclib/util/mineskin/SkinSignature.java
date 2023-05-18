package de.tjorven.npclib.util.mineskin;

import de.tjorven.npclib.util.mineskin.data.*;
import de.tjorven.npclib.util.mineskin.data.MineSkin;
import de.tjorven.npclib.util.mineskin.data.Texture;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.ExecutionException;

public class SkinSignature {

    String name;
    String file;
    Texture texture;
    MineSkin skin;

    public SkinSignature(String name, String filePath) {
        this.name = name;
        this.file = filePath;
        System.out.println(name + ":" + filePath);
        File skinFile = new File("plugins/NPC/skins/" + filePath);
        try {
            skin = new MineskinClient("Java/1.0").generateUpload(skinFile).get();
        } catch (InterruptedException | ExecutionException | FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        texture = skin.data.getTexture();
    }

    public SkinSignature(String name, String value, String signature) {
        MineSkin skin = new MineSkin();
        skin.setName(name);
        SkinData data = new SkinData();
        Texture texture = new Texture(value, signature);
        texture.setSignature(signature);
        texture.setUrl(null);
        texture.setValue(value);
        data.setTexture(texture);
        skin.setData(data);
        this.skin = skin;
        this.texture = texture;
    }

    public String getSignature() {
        return skin.getData().getTexture().getSignature();
    }

    public  MineSkin getSkin() {
        return skin;
    }

    public Texture getTexture() {
        return texture;
    }

    public SkinData getSkinData() {
        return skin.getData();
    }

}
