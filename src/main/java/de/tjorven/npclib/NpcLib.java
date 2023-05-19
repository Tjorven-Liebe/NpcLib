package de.tjorven.npclib;

import de.tjorven.npclib.listener.PlayerJoinListener;
import org.bukkit.plugin.java.JavaPlugin;

public class NpcLib extends JavaPlugin {

    private static NpcLib instance;
    private static Register register;

    @Override
    public void onEnable() {
        instance = this;
        register = new Register(this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        super.onEnable();
    }

    public static Register getRegister() {
        return register;
    }

    public static NpcLib getInstance() {
        return instance;
    }
}
