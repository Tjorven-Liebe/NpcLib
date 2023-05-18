package de.tjorven.npclib.npc;

import de.tjorven.npclib.npc.actions.NPCItemSlot;
import de.tjorven.npclib.npc.skin.Skin;
import de.tjorven.npclib.npc.skin.SkinUtil;
import de.tjorven.npclib.util.Pair;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.List;
import java.util.UUID;

public interface NPC {

    /**
     * Gets the name of the ServerPlayer
     * it can have multiple lines
     * @implNote "null" means that the nameTag is invisible
     *
     * @return name of the ServerPlayer
     */
    String[] getName();

    /**
     * Sets the name of the ServerPlayer
     * it can have multiple lines
     * @implNote set "null" to set the nameTag invisible
     *
     * @param name wished name of the player
     */
    void setName(String... name);

    /**
     * MineSkin implementation from Base64 encoded image of Mojang REST API
     * MineSkin has attributes name, signature and value
     *
     * @return MineSkin implementation
     * @see SkinUtil for more traceback
     */
    Skin getSkin();

    /**
     * Sets the skin of the ServerPlayer
     * use {@link SkinUtil} and {@link NPCImpl} for creation
     *
     * @param skin sets the MineSkin Value and Signature of the player
     */
    void setSkin(Skin skin);

    /**
     * Sets the skin of the ServerPlayer
     * use {@link SkinUtil} and {@link NPCImpl} for creation
     * Uses MineSkinApi for getting the skin
     *
     * @param uuid
     */
    void setSkin(UUID uuid);

    /**
     * Sets the skin of the ServerPlayer
     * use {@link SkinUtil} and {@link NPCImpl} for creation
     * Uses MineSkinApi for getting the skin
     *
     * @param name name of the skin
     * @param url  url of the file
     */
    void setSkin(String name, String url);

    /**
     * Sets the skin of the ServerPlayer
     * use {@link SkinUtil} and {@link NPCImpl} for creation
     * Uses MineSkinApi for getting the skin
     *
     * @param name name of the skin
     * @param file file of the skin
     */
    void setSkin(String name, File file);

    /**
     * @implNote Will not return the accurate location of the ServerPlayer
     * @see Location for usage info
     *
     * @return the location where the NPC was spawned
     */
    Location getSpawnLocation();

    /**
     * will punch the entity
     * and send a ClientboundAnimatePacket to the implemented player(s)
     * the hand will be the main hand
     *
     * @param entity the entity to punch
     */
    void punch(Entity entity);

    /**
     * will send a ClientboundAnimatePacket to the implemented player(s)
     * the hand will be the main hand
     */
    void punch();

    /**
     * TODO: implement
     * @param value does the NPC shift
     */
    void toggleShift(boolean value);

    /**
     * Will send a ClientboundHurtAnimationPacket to the implemented player(s)
     * @implNote The ServerPlayer will not be damaged but the animation will be played
     */
    void damage();

    /**
     * @see Entity#getEntityId()
     *
     * @return the entityId of the ServerPlayer
     */
    int getEntityId();

    /**
     * Will always look to the player the SpawnPacket was sent
     * @implNote though the ServerPlayer is only visible for players the SpawnPacket was sent to, the ServerPlayer will always look to yourself
     *
     * @param value should the NPC look to the player
     */
    void lockViewToPlayer(boolean value);

    /**
     * @see NPC#lockViewToPlayer(boolean)
     *
     * @return true if the ServerPlayer is locked to the player
     */
    boolean isLockedVieToPlayer();

    /**
     * Will set the items of the ServerPlayer
     * @see Pair for usage info
     * @see NPCItemSlot for usage info
     * @see ItemStack for usage info
     *
     * @param items the items to set
     */
    void setItems(Pair<NPCItemSlot, ItemStack>... items);

    /**
     * Will return the items the NPC has set
     *
     * @return Pair of NPCItemSlot and ItemStack
     */
    List<Pair<NPCItemSlot, ItemStack>> getItems();

    /**
     * Will spawn the ServerPlayer / NPC
     * Location will most likely be the one of the Player who ran the command
     * the field "spawnLocation" will not be changed
     *
     * @see NPC#getSpawnLocation()
     */
    void spawn();

    /**
     * Will despawn the ServerPlayer / NPC
     * @implNote the NPC will not be deleted, it will just be invisible
     */
    void despawn();
}
