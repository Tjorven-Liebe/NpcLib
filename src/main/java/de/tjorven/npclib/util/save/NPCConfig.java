package de.tjorven.npclib.util.save;

import de.tjorven.npclib.NpcLibApi;
import de.tjorven.npclib.npc.NPC;
import de.tjorven.npclib.npc.enums.NPCItemSlot;
import de.tjorven.npclib.npc.skin.Skin;
import de.tjorven.npclib.util.Pair;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class NPCConfig {
    private static final File FILE = new File("plugins/NpcLib/cache/npcs.yml");
    private static final YamlConfiguration CONFIGURATION = YamlConfiguration.loadConfiguration(FILE);

    public static void add(NPC npc) {
        String key = npc.getName();
        Skin skin = npc.getSkin();
        CONFIGURATION.set("npcs." + key + ".meta.spawnLocation", npc.getSpawnLocation());
        CONFIGURATION.set("npcs." + key + ".meta.name", "");
        CONFIGURATION.set("npcs." + key + ".meta.names", Arrays.stream(npc.getDisplayNames()).toList());
        npc.getItems().forEach(itemPair -> {
            CONFIGURATION.set("npcs." + key + ".meta.items." + itemPair.getFirst().name(), itemPair.getSecond());
        });
        CONFIGURATION.set("npcs." + key + ".meta.skin.name", skin.getName());
        CONFIGURATION.set("npcs." + key + ".meta.skin.value", skin.getValue());
        CONFIGURATION.set("npcs." + key + ".meta.skin.signature", skin.getSignature());
        CONFIGURATION.set("npcs." + key + ".state.shift", npc.isShifting());
        CONFIGURATION.set("npcs." + key + ".state.lockToPlayer", npc.isLockedViewToPlayer());
        CONFIGURATION.set("npcs." + key + ".state.isSpawned", npc.isSpawned());
        save();
    }

    public static void remove(NPC npc) {

    }

    public static List<NPC> getNPCs() {
        List<NPC> list = new LinkedList<>();
        CONFIGURATION.getConfigurationSection("npcs").getKeys(false).forEach(npcsSection -> {
            list.add(getNPC(npcsSection));
        });
        return list;
    }

    public static NPC getNPC(String key) {
        Location location = CONFIGURATION.getLocation("npcs." + key + ".meta.spawnLocation");
        String name = CONFIGURATION.getString("npcs." + key + ".meta.spawnName");
        List<String> names = CONFIGURATION.getStringList("npcs." + key + ".meta.names");
        Skin skin = new Skin(CONFIGURATION.getString("npcs." + key + ".meta.skin.name"),
                CONFIGURATION.getString("npcs." + key + ".meta.skin.value"),
                CONFIGURATION.getString("npcs." + key + ".meta.skin.signature"));
        boolean shift = CONFIGURATION.getBoolean("npcs." + key + ".state.shift");
        boolean lockToPlayer = CONFIGURATION.getBoolean("npcs." + key + ".state.lockToPlayer");
        boolean isSpawned = CONFIGURATION.getBoolean("npcs." + key + ".state.isSpawned");
        List<Pair<NPCItemSlot, ItemStack>> items = new ArrayList<>();
        for (String section : CONFIGURATION.getConfigurationSection("meta.items").getKeys(false)) {
           items.add(new Pair<>(NPCItemSlot.valueOf(section), CONFIGURATION.getItemStack("npcs." + key + ".state.items." + section)));
        }
        NPC npc = NpcLibApi.createNPC(location, name);
        npc.setDisplayName(names.toArray(new String[0]));
        npc.setSkin(skin);
        npc.toggleShift(shift);
        npc.lockViewToPlayer(lockToPlayer);
        if (isSpawned) {
            npc.spawn();
            npc.setItems(items.toArray(new Pair[0]));
        }
        return null;
    }

    public static void save() {
        try {
            CONFIGURATION.save(FILE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
