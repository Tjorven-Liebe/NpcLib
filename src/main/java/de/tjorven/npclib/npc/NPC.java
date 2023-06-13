package de.tjorven.npclib.npc;

import de.tjorven.npclib.npc.enums.*;
import de.tjorven.npclib.npc.skin.ClothType;
import de.tjorven.npclib.npc.skin.Skin;
import de.tjorven.npclib.npc.skin.SkinUtil;
import de.tjorven.npclib.util.Pair;
import net.minecraft.world.item.alchemy.Potion;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.UUID;

public interface NPC {

    /**
     * @return The real name of the player set in the gameProfile
     */
    String getName();

    /**
     * Gets the name of the ServerPlayer
     * it can have multiple lines
     *
     * @return name of the ServerPlayer
     * @implNote "null" means that the nameTag is invisible
     */
    String[] getDisplayNames();

    /**
     * Sets the name of the ServerPlayer
     * it can have multiple lines
     *
     * @param name wished name of the player
     * @implNote set no parameters to set the nameTag invisible
     */
    void setDisplayName(String... name);

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
     * use {@link SkinUtil} and {@link NPCImplementation} for creation
     *
     * @param skin sets the MineSkin Value and Signature of the player
     */
    void setSkin(Skin skin);

    /**
     * Sets the skin of the ServerPlayer
     * use {@link SkinUtil} and {@link NPCImplementation} for creation
     * Uses MineSkinApi for getting the skin
     *
     * @param uuid the uniqueID of the player which has the wished skin
     */
    void setSkin(UUID uuid);

    /**
     * Sets the skin of the ServerPlayer
     * use {@link SkinUtil} and {@link NPCImplementation} for creation
     * Uses MineSkinApi for getting the skin
     *
     * @param name name of the skin
     * @param url  url of the file
     */
    void setSkin(String name, String url);

    /**
     * Sets the skin of the ServerPlayer
     * use {@link SkinUtil} and {@link NPCImplementation} for creation
     * Uses MineSkinApi for getting the skin
     *
     * @param name name of the skin
     * @param file file of the skin
     */
    void setSkin(String name, File file);

    /**
     * @return the location where the NPC was spawned
     * @implNote Will not return the accurate location of the ServerPlayer
     * @see Location for usage info
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

    void setPotionEffect(PotionEffectType effect, Color color);

    void toggleElytraFly();

    void toggleGlowEffect();

    /**
     * Sets the player in a swim position
     */
    void toggleSwimming();

    /**
     * Sets the player virtually on fire or disables it (toggle)
     */
    void toggleFire();

    void jump();

    /**
     * I'ts not working perfect but it works
     * //TODO: implement it in a better way
     *
     * @param mob the mob the serverplayer will ride
     */
    void setOnVehicle(Mob mob);

    /**
     * @param shoulder which shoulder
     * @param variant  which color
     * @param value    should the parrot be added or removed?
     */
    void setParrotOnShoulder(Shoulder shoulder, ParrotVariant variant, boolean value);

    /**
     * Used in punch for example
     *
     * @param hand the new main hand
     */
    void setMainHand(Hand hand);

    /**
     * Stingers are the tails of bees
     *
     * @param flag how many stingers should stick to the player
     */
    void setStingersInEntity(int flag);

    /**
     * @param flag how many arrow should stick to the player
     */
    void setArrowsInBody(int flag);

    /**
     * it's the attack type of riptide
     */
    void toggleSpinAttack();

    void setPose(EntityPose pose, boolean value);

    /**
     * @param entityPose the Pose
     * @return if player has pose entityPose
     */
    boolean isPosing(EntityPose entityPose);

    EntityPose getPose();

    /**
     * Will send a ClientboundHurtAnimationPacket to the implemented player(s)
     *
     * @implNote The ServerPlayer will not be damaged but the animation will be played
     */
    void damage();

    void swingOffHand();

    void takeCritical();

    void takeMagicalCritical();

    /**
     * @return the entityId of the ServerPlayer
     * @see Entity#getEntityId()
     */
    int getEntityId();

    /**
     * Will always look to the player the SpawnPacket was sent
     *
     * @param value should the NPC look to the player
     * @implNote though the ServerPlayer is only visible for players the SpawnPacket was sent to, the ServerPlayer will always look to yourself
     */
    void lockViewToPlayer(boolean value);

    /**
     * @return true if the ServerPlayer is locked to the player
     * @see NPC#lockViewToPlayer(boolean)
     */
    boolean isLockedViewToPlayer();

    /**
     * Will return the items the NPC has set
     *
     * @return Pair of NPCItemSlot and ItemStack
     */
    List<Pair<NPCItemSlot, ItemStack>> getItems();

    /**
     * Will set the items of the ServerPlayer
     *
     * @param items the items to set
     * @see Pair for usage info
     * @see NPCItemSlot for usage info
     * @see ItemStack for usage info
     */
    void setItems(Pair<NPCItemSlot, ItemStack>... items);

    /**
     * Will spawn the ServerPlayer / NPC
     * Location will most likely be the one of the Player who ran the command
     * the field "spawnLocation" will not be changed
     *
     * @see NPC#getSpawnLocation()
     */
    void spawn();

    /**
     * @return if the npc is currently spawned
     */
    boolean isSpawned();

    /**
     * Will despawn the ServerPlayer / NPC
     *
     * @implNote the NPC will not be deleted, it will just be invisible
     */
    void despawn();


    /**
     * Set the skin layers that are active
     *
     * @param value value of the skin layers
     * @see <a href="https://wikivg.tjorven-liebe.de/index.html#Client_Settings">Client Settings, Wiki.vg</a>
     */
    void setCloth(byte value);

    /**
     * Set the skin layers that are active
     *
     * @param type value of the skin layers
     * @see <a href="https://wikivg.tjorven-liebe.de/index.html#Client_Settings">Client Settings, Wiki.vg</a>
     */
    void setCloth(ClothType type);

    /**
     * Rotates the head to the specified location
     *
     * @param location the specified location
     */
    void lookTo(Location location);
}
