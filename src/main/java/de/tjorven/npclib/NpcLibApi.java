package de.tjorven.npclib;

import de.tjorven.npclib.npc.NPC;
import de.tjorven.npclib.npc.NPCImplementation;
import de.tjorven.npclib.npc.skin.Skin;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NpcLibApi {

    public static NPC createNPC(Location location, String name) {
        return new NPCImplementation(location, name);
    }

    public static NPC createNPC(Location location, String name, Player... implemented) {
        return new NPCImplementation(location, name, implemented);
    }

    public static NPC createNPC(Player player, String name) {
        return new NPCImplementation(player, name);
    }

    public static NPC createNPC(Player player, String name, Player... implemented) {
        return new NPCImplementation(player, name, implemented);
    }

    public static NPC createNPC(Player player, String name, Skin skin) {
        NPC npc = createNPC(player, name);
        npc.setSkin(skin);
        return npc;
    }

    public static NPC createNPC(Player player, String name, Skin skin, Player... implemented) {
        NPC npc = createNPC(player, name, implemented);
        npc.setSkin(skin);
        return npc;
    }

    public static NPC createNPC(Location location, String name, Skin skin) {
        NPC npc = createNPC(location, name);
        npc.setSkin(skin);
        return npc;
    }

    public static NPC createNPC(Location location, String name, Skin skin, Player... implemented) {
        NPC npc = createNPC(location, name, implemented);
        npc.setSkin(skin);
        return npc;
    }

}
