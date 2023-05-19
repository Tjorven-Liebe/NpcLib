package de.tjorven.npclib;

import org.bukkit.plugin.java.JavaPlugin;

public class NpcLib extends JavaPlugin {

    private static NpcLib instance;

    @Override
    public void onEnable() {
        instance = this;
        new Register(this);
        super.onEnable();
    }

    public static NpcLib getInstance() {
        return instance;
    }
}
