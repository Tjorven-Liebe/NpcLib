package de.tjorven.npclib.util.save;

import de.tjorven.npclib.npc.NPC;

import java.util.ArrayList;
import java.util.HashMap;

public class NPCRegister {

    public HashMap<Integer, NPC> npcs = new HashMap<>();

    public void load() {
//        npcs.put()
    }

    public void getNPC(int id) {
        npcs.get(id);
    }

    public void addNPC(int id, NPC npc) {
        npcs.put(id, npc);
    }

    public void addNPC(NPC npc) {
        addNPC(npc.getEntityId(), npc);
    }

    public void removeNPC(int id) {
        npcs.remove(id);
    }

    public void removeNPC(NPC npc) {
        npcs.remove(npc.getEntityId());
    }
}
