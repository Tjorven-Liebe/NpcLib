package de.tjorven.npclib.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tjorven.npclib.NpcLib;
import de.tjorven.npclib.npc.actions.NPCItemSlot;
import de.tjorven.npclib.npc.skin.ClothType;
import de.tjorven.npclib.npc.skin.Skin;
import de.tjorven.npclib.npc.skin.SkinUtil;
import de.tjorven.npclib.util.Pair;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.scores.PlayerTeam;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.*;

public class NPCImplementation implements NPC {

    private final ServerPlayer serverPlayer;
    private final Location spawnLocation;
    private final List<Player> implementedPlayers = new ArrayList<>();
    private final List<String> names = new LinkedList<>();
    private final GameProfile profile;
    private final SynchedEntityData data;
    private final String name;

    public NPCImplementation(Location location, String name) {
        this.spawnLocation = location;
        if (implementedPlayers.isEmpty())
            implementedPlayers.addAll(Bukkit.getOnlinePlayers());
        ServerLevel handle = ((CraftWorld) location.getWorld()).getHandle();
        profile = new GameProfile(UUID.randomUUID(), name);
        serverPlayer = new ServerPlayer(handle.getServer(), handle, profile);
        data = serverPlayer.getEntityData();
        data.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);
        this.name = name;
    }

    public NPCImplementation(Player player, String name) {
        this(player.getLocation(), name);
    }

    /**
     * @param player      the location of the NPC
     * @param name        the name of the NPC's game profile
     * @param implemented the players that will get the packets
     * @implNote Intended for test usage
     */
    public NPCImplementation(Player player, String name, Player... implemented) {
        this(player.getLocation(), name);
        implementedPlayers.addAll(Arrays.stream(implemented).toList());
    }

    /**
     * @param location    the location of the NPC
     * @param name        the name of the NPC's game profile
     * @param implemented the players that will get the packets
     * @implNote Intended for test usage
     */
    public NPCImplementation(Location location, String name, Player... implemented) {
        this(location, name);
        implementedPlayers.addAll(Arrays.stream(implemented).toList());
    }

    @Override
    public void setCloth(ClothType type) {
        setCloth(type.getByte());
    }

    @Override
    public void setCloth(byte value) {
        data.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) value);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String[] getDisplayNames() {
        return names.toArray(new String[0]);
    }

    @Override
    public void setDisplayName(String... name) {
        //TODO: set name
        if (name.length == 0) {
            try {
                Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
                Team hideNameTag = mainScoreboard.getTeam("hide_nametag") != null ? mainScoreboard.getTeam("hide_nametag") : mainScoreboard.registerNewTeam("hide_nametag");
                hideNameTag.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
                CraftPlayer bukkitEntity = serverPlayer.getBukkitEntity();
                hideNameTag.addPlayer(bukkitEntity);
                implementedPlayers.forEach(player -> {
                    ((CraftPlayer) player).getHandle().connection.send(ClientboundSetPlayerTeamPacket.createPlayerPacket((PlayerTeam) hideNameTag, getName(), ClientboundSetPlayerTeamPacket.Action.ADD));
                });
            } catch (Exception ignoredBecauseItsCorrect) {
            }
            return;
        }
        names.addAll(Arrays.stream(name).toList());
    }

    @Override
    public Skin getSkin() {
        return null;
    }

    @Override
    public void setSkin(Skin skin) {
        profile.getProperties().get("textures").clear();
        profile.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
    }

    @Override
    public void setSkin(UUID uuid) {
        SkinUtil.getSkin(uuid).thenAcceptAsync(this::setSkin);
    }

    @Override
    public void setSkin(String name, File file) {
        SkinUtil.createSkin(name, file).thenAcceptAsync(this::setSkin);
    }

    @Override
    public void setSkin(String name, String url) {
        SkinUtil.createSkin(name, url).thenAcceptAsync(this::setSkin);
    }

    @Override
    public Location getSpawnLocation() {
        return spawnLocation;
    }

    @Override
    public void punch(Entity entity) {
        //Todo: punch the entity send the packet
    }

    @Override
    public void punch() {
        //TODO: send the packet
    }

    @Override
    public void toggleShift(boolean value) {
        //TODO: toggle Shift
    }

    @Override
    public boolean isShifting() {
        return false;
    }

    @Override
    public void damage() {
        //TODO: send the damage packet to the player
    }

    @Override
    public int getEntityId() {
        return serverPlayer.getId();
    }

    @Override
    public void lockViewToPlayer(boolean value) {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(NpcLib.getInstance(), () -> {
            implementedPlayers.forEach(player -> {
                if (Bukkit.getOnlinePlayers().size() > 0) {
                    if (!serverPlayer.getBukkitEntity().getWorld().getName().equals(player.getWorld().getName()))
                        return;
                    org.bukkit.util.Vector differenceX = player.getLocation().subtract(serverPlayer.getBukkitEntity().getLocation()).toVector().normalize();
                    float degreesX = (float) Math.toDegrees(Math.atan2(differenceX.getZ(), differenceX.getX()) - Math.PI / 2);
                    byte angleX = (byte) Mth.floor((degreesX * 256.0F) / 360.0F);
                    Vector differenceY = player.getLocation().subtract(serverPlayer.getBukkitEntity().getLocation()).toVector().normalize();
                    float degreesY = (float) (Math.toDegrees(Math.atan(differenceY.getY())) - Math.PI / 2);
                    byte angleY = (byte) Mth.floor((degreesY * 256.0F) / 360.0F);
                    ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
                    connection.send(new ClientboundRotateHeadPacket(serverPlayer, angleX));
                    connection.send(new ClientboundMoveEntityPacket.Rot(serverPlayer.getBukkitEntity().getEntityId(), angleX, (byte) -angleY, true));
                }
            });
        }, 0, 3);
    }

    @Override
    public void lookTo(Location location) {
        implementedPlayers.forEach(player -> {
            ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
            connection.send(new ClientboundRotateHeadPacket(serverPlayer, (byte) (location.getYaw() * 256.0F / 360.0F)));
            connection.send(new ClientboundMoveEntityPacket.Rot(serverPlayer.getBukkitEntity().getEntityId(), (byte) (location.getYaw() * 256.0F / 360.0F), (byte) (location.getPitch() * 256.0F / 360.0F), true));
        });
    }

    @Override
    public boolean isLockedViewToPlayer() {
        //TODO: create field lockedToPlayer
        return false;
    }

    @Override
    public List<Pair<NPCItemSlot, ItemStack>> getItems() {
        //Todo: create a list of the items
        return null;
    }

    @Override
    public void setItems(Pair<NPCItemSlot, ItemStack>... items) {
        //TODO: create a list of the items
        //TODO: send ClientboundSetEquipmentPacket packet to implemented players
        List<com.mojang.datafixers.util.Pair<EquipmentSlot, net.minecraft.world.item.ItemStack>> list = new ArrayList<>();
        for (Pair<NPCItemSlot, ItemStack> item : items) {
            switch (item.getFirst()) {
                case MAIN_HAND -> {
                    list.add(new com.mojang.datafixers.util.Pair<>(EquipmentSlot.MAINHAND, CraftItemStack.asNMSCopy(item.getSecond())));
                }
                case OFF_HAND -> {
                    list.add(new com.mojang.datafixers.util.Pair<>(EquipmentSlot.OFFHAND, CraftItemStack.asNMSCopy(item.getSecond())));
                }
                case LEGS -> {
                    list.add(new com.mojang.datafixers.util.Pair<>(EquipmentSlot.LEGS, CraftItemStack.asNMSCopy(item.getSecond())));
                }
                case HEAD -> {
                    list.add(new com.mojang.datafixers.util.Pair<>(EquipmentSlot.HEAD, CraftItemStack.asNMSCopy(item.getSecond())));
                }
                case FEET -> {
                    list.add(new com.mojang.datafixers.util.Pair<>(EquipmentSlot.FEET, CraftItemStack.asNMSCopy(item.getSecond())));
                }
                case CHEST -> {
                    list.add(new com.mojang.datafixers.util.Pair<>(EquipmentSlot.CHEST, CraftItemStack.asNMSCopy(item.getSecond())));
                }
            }
        }
    }

    @Override
    public void spawn() {
        implementedPlayers.forEach(player -> {
            ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
            connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, serverPlayer));
            Bukkit.getScheduler().runTaskLaterAsynchronously(NpcLib.getInstance(), () -> {
                connection.send(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(serverPlayer.getUUID())));
            }, 20);
            serverPlayer.moveTo(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), spawnLocation.getYaw(), spawnLocation.getPitch());
            connection.send(new ClientboundAddPlayerPacket(serverPlayer));
            lookTo(spawnLocation);
            data.refresh(((CraftPlayer) player).getHandle());
        });
    }

    @Override
    public boolean isSpawned() {
        //Todo: is spawned
        return false;
    }

    @Override
    public void despawn() {
        implementedPlayers.forEach(player -> ((CraftPlayer) player).getHandle().connection.send(new ClientboundRemoveEntitiesPacket(getEntityId())));
    }
}
