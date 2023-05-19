package de.tjorven.npclib.util.save;

import de.tjorven.npclib.npc.NPC;

import java.util.HashMap;

public class NPCRegister {

    private static final HashMap<Integer, NPC> NPCS = new HashMap<>();

    public void load() {
//        npcs.put()
    }

    public static NPC[] getNPCS() {
        return NPCS.values().toArray(new NPC[0]);
    }

    public static boolean contains(int id) {
        return NPCS.containsKey(id);
    }

    public static NPC getNPC(int id) {
        return NPCS.get(id);
    }

    public static void addNPC(int id, NPC npc) {
        NPCS.put(id, npc);
    }

    public static void addNPC(NPC npc) {
        addNPC(npc.getEntityId(), npc);
    }

    public static void removeNPC(int id) {
        NPCS.remove(id);
    }

    public static void removeNPC(NPC npc) {
        NPCS.remove(npc.getEntityId());
    }
}
