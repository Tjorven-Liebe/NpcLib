package de.tjorven.npclib;

import de.tjorven.npclib.npc.NPC;
import de.tjorven.npclib.npc.NPCImpl;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class NpcLibApi {

    public static NPC createNPC(Location location, String name) {
        return new NPCImpl(location, name);
    }

    public static NPC createNPC(Location location, String name, Player... implemented) {
        return new NPCImpl(location, name, implemented);
    }


    public static NPC createNPC(Player player, String name) {
        return new NPCImpl(player, name);
    }

    public static NPC createNPC(Player player, String name, Player... implemented) {
        return new NPCImpl(player, name, implemented);
    }

}
