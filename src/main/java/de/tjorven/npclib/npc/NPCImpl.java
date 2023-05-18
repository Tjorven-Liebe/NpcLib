package de.tjorven.npclib.npc;

import de.tjorven.npclib.NpcLib;
import de.tjorven.npclib.npc.actions.NPCItemSlot;
import de.tjorven.npclib.npc.skin.Skin;
import de.tjorven.npclib.npc.skin.SkinUtil;
import de.tjorven.npclib.util.Pair;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class NPCImpl implements NPC {

    ServerPlayer serverPlayer;
    Location spawnLocation;
    List<Player> implementedPlayers = new ArrayList<>();

    public NPCImpl(ServerPlayer serverPlayer) {
        this.serverPlayer = serverPlayer;
    }

    @Override
    public String[] getName() {
        //TODO: get name
        return null;
    }

    @Override
    public void setName(String... name) {
        //TODO: set name
    }

    @Override
    public Skin getSkin() {
        return null;
    }

    @Override
    public void setSkin(Skin skin) {
        //TODO: set skin
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
    public void damage() {
        //TODO: send the damage packet to the player
    }

    @Override
    public int getEntityId() {
        return serverPlayer.getId();
    }

    @Override
    public void lockViewToPlayer(boolean value) {
        //TODO: lock the view to the player
    }

    @Override
    public boolean isLockedVieToPlayer() {
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
            SynchedEntityData data = serverPlayer.getEntityData();
            data.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);
            connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, serverPlayer));
            Bukkit.getScheduler().runTaskLaterAsynchronously(NpcLib.getInstance(), () -> {
                connection.send(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(serverPlayer.getUUID())));
            }, 20);
            serverPlayer.moveTo(spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), spawnLocation.getYaw(), spawnLocation.getPitch());
            connection.send(new ClientboundAddPlayerPacket(serverPlayer));
            data.refresh(((CraftPlayer) player).getHandle());
        });
    }

    @Override
    public void despawn() {
        implementedPlayers.forEach(player -> ((CraftPlayer) player).getHandle().connection.send(new ClientboundRemoveEntitiesPacket(getEntityId())));
    }
}
