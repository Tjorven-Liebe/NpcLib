package de.tjorven.npclib.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import de.tjorven.npclib.NpcLib;
import de.tjorven.npclib.npc.enums.Hand;
import de.tjorven.npclib.npc.enums.NPCItemSlot;
import de.tjorven.npclib.npc.enums.ParrotVariant;
import de.tjorven.npclib.npc.enums.Shoulder;
import de.tjorven.npclib.npc.skin.ClothType;
import de.tjorven.npclib.npc.skin.Skin;
import de.tjorven.npclib.npc.skin.SkinUtil;
import de.tjorven.npclib.util.Pair;
import de.tjorven.npclib.util.save.NPCRegister;
import net.kyori.adventure.text.Component;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.entity.animal.Parrot;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftMob;
import org.bukkit.craftbukkit.v1_19_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R3.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.io.File;
import java.util.*;

public class NPCImplementation implements NPC {

    private final ServerPlayer SERVER_PLAYER;
    private final Location SPAWN_LOCATION;
    private final List<Player> IMPLEMENTED_PLAYERS = new ArrayList<>();
    private final List<String> NAMES = new LinkedList<>();
    private final GameProfile GAME_PROFILE;
    private final SynchedEntityData DATA;
    private final String NAME;
    private final List<ArmorStand> NAME_TAGS = new ArrayList<>();
    private boolean isSpawned;

    public NPCImplementation(Location location, String name) {
        this.SPAWN_LOCATION = location;
        if (IMPLEMENTED_PLAYERS.isEmpty())
            IMPLEMENTED_PLAYERS.addAll(Bukkit.getOnlinePlayers());
        ServerLevel handle = ((CraftWorld) location.getWorld()).getHandle();
        GAME_PROFILE = new GameProfile(UUID.randomUUID(), name);
        SERVER_PLAYER = new ServerPlayer(handle.getServer(), handle, GAME_PROFILE);
        DATA = SERVER_PLAYER.getEntityData();
        DATA.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);
        this.NAME = name;
        NPCRegister.addNPC(this);
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
        IMPLEMENTED_PLAYERS.addAll(Arrays.stream(implemented).toList());
    }

    /**
     * @param location    the location of the NPC
     * @param name        the name of the NPC's game profile
     * @param implemented the players that will get the packets
     * @implNote Intended for test usage
     */
    public NPCImplementation(Location location, String name, Player... implemented) {
        this(location, name);
        IMPLEMENTED_PLAYERS.addAll(Arrays.stream(implemented).toList());
    }

    @Override
    public void setCloth(ClothType type) {
        setCloth(type.getByte());
    }

    @Override
    public void setCloth(byte value) {
        DATA.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), value);
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public String[] getDisplayNames() {
        return NAMES.toArray(new String[0]);
    }

    @Override
    public void setDisplayName(String... names) {
        //TODO: set name
        try {
            Scoreboard mainScoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
            Team hideNameTag = mainScoreboard.getTeam("hide_nametag") != null ? mainScoreboard.getTeam("hide_nametag") : mainScoreboard.registerNewTeam("hide_nametag");
            assert hideNameTag != null;
            hideNameTag.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
            CraftPlayer bukkitEntity = SERVER_PLAYER.getBukkitEntity();
            hideNameTag.addPlayer(bukkitEntity);
            IMPLEMENTED_PLAYERS.forEach(player -> {
                ((CraftPlayer) player).getHandle().connection.send(ClientboundSetPlayerTeamPacket.createPlayerPacket((PlayerTeam) hideNameTag, getName(), ClientboundSetPlayerTeamPacket.Action.ADD));
            });
        } catch (Exception ignoredBecauseItsCorrect) {
        }
        if (names.length == 0) {
            return;
        }
        IMPLEMENTED_PLAYERS.forEach(player -> {
            for (int i = names.length - 1; i >= 0; i--) {
                System.out.println(names[i]);
                Location clone = SPAWN_LOCATION.clone();
                clone.subtract(0, 0.2, 0);
                ArmorStand armorStand = (ArmorStand) player.getWorld().spawnEntity(clone, EntityType.ARMOR_STAND);
                armorStand.setGravity(false);
                armorStand.teleport(clone.add(0, (double) i / 2.75, 0));
                armorStand.setInvulnerable(true);
                armorStand.setCustomNameVisible(true);
                armorStand.setVisible(false);
                armorStand.customName(Component.text(ChatColor.translateAlternateColorCodes('&', names[i])));
                NAME_TAGS.add(armorStand);
            }
        });
        this.NAMES.addAll(Arrays.stream(names).toList());
    }

    @Override
    public Skin getSkin() {
        return null;
    }

    @Override
    public void setSkin(Skin skin) {
        GAME_PROFILE.getProperties().get("textures").clear();
        GAME_PROFILE.getProperties().put("textures", new Property("textures", skin.getValue(), skin.getSignature()));
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
        return SPAWN_LOCATION;
    }

    @Override
    public void punch(Entity entity) {
        punch();
        entity.setVelocity(entity.getLocation().getDirection().multiply(-0.5));
    }

    @Override
    public void punch() {
        send(new ClientboundAnimatePacket(SERVER_PLAYER, 0));
    }

    @Override
    public void setParrotOnShoulder(Shoulder shoulder, ParrotVariant variant, boolean value) {
        if (!value) {
            DATA.set(new EntityDataAccessor<>(19, EntityDataSerializers.COMPOUND_TAG), new CompoundTag());
            DATA.set(new EntityDataAccessor<>(19, EntityDataSerializers.COMPOUND_TAG), new CompoundTag());
        } else {
            if (shoulder.equals(Shoulder.LEFT)) {
                DATA.set(new EntityDataAccessor<>(19, EntityDataSerializers.COMPOUND_TAG), createParrot(variant));
            } else {
                DATA.set(new EntityDataAccessor<>(20, EntityDataSerializers.COMPOUND_TAG), createParrot(variant));
            }
        }
        IMPLEMENTED_PLAYERS.forEach(player -> {
            DATA.refresh(((CraftPlayer) player).getHandle());
        });
    }

    private CompoundTag createParrot(ParrotVariant variant) {
        Parrot parrot = new Parrot(net.minecraft.world.entity.EntityType.PARROT, ((CraftWorld) SPAWN_LOCATION.getWorld()).getHandle());
        CompoundTag tag = new CompoundTag();
        System.out.println(tag.tags);
        parrot.setVariant(Parrot.Variant.valueOf(variant.name()));
        parrot.save(tag);
        return tag;
    }

    @Override
    public void setMainHand(Hand hand) {
        DATA.set(new EntityDataAccessor<>(18, EntityDataSerializers.BYTE), (byte) (hand.equals(Hand.MAIN_HAND) ? 0 : 1));
        IMPLEMENTED_PLAYERS.forEach(player -> {
            DATA.refresh(((CraftPlayer) player).getHandle());
        });
    }

    @Override
    public void setStingersInEntity(int flag) {
        DATA.set(new EntityDataAccessor<>(13, EntityDataSerializers.INT), flag);
        IMPLEMENTED_PLAYERS.forEach(player -> {
            DATA.refresh(((CraftPlayer) player).getHandle());
        });
    }

    @Override
    public void setArrowsInBody(int flag) {
        DATA.set(new EntityDataAccessor<>(12, EntityDataSerializers.INT), flag);
        IMPLEMENTED_PLAYERS.forEach(player -> {
            DATA.refresh(((CraftPlayer) player).getHandle());
        });
    }

    @Override
    public void toggleSpinAttack() {
        DATA.set(new EntityDataAccessor<>(8, EntityDataSerializers.BYTE), (byte) 0x04);
        IMPLEMENTED_PLAYERS.forEach(player -> {
            DATA.refresh(((CraftPlayer) player).getHandle());
        });
    }

    @Override
    public void toggleShift(boolean value) {
        DATA.set(new EntityDataAccessor<>(6, EntityDataSerializers.POSE), value ? Pose.CROUCHING : Pose.STANDING);
        IMPLEMENTED_PLAYERS.forEach(player -> {
            DATA.refresh(((CraftPlayer) player).getHandle());
        });
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
        return SERVER_PLAYER.getId();
    }

    @Override
    public void lockViewToPlayer(boolean value) {
        Bukkit.getScheduler().scheduleAsyncRepeatingTask(NpcLib.getInstance(), () -> {
            IMPLEMENTED_PLAYERS.forEach(player -> {
                if (Bukkit.getOnlinePlayers().size() > 0) {
                    if (!SERVER_PLAYER.getBukkitEntity().getWorld().getName().equals(player.getWorld().getName()))
                        return;
                    org.bukkit.util.Vector differenceX = player.getLocation().subtract(SERVER_PLAYER.getBukkitEntity().getLocation()).toVector().normalize();
                    float degreesX = (float) Math.toDegrees(Math.atan2(differenceX.getZ(), differenceX.getX()) - Math.PI / 2);
                    byte angleX = (byte) Mth.floor((degreesX * 256.0F) / 360.0F);
                    Vector differenceY = player.getLocation().subtract(SERVER_PLAYER.getBukkitEntity().getLocation()).toVector().normalize();
                    float degreesY = (float) (Math.toDegrees(Math.atan(differenceY.getY())) - Math.PI / 2);
                    byte angleY = (byte) Mth.floor((degreesY * 256.0F) / 360.0F);
                    ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
                    connection.send(new ClientboundRotateHeadPacket(SERVER_PLAYER, angleX));
                    connection.send(new ClientboundMoveEntityPacket.Rot(SERVER_PLAYER.getBukkitEntity().getEntityId(), angleX, (byte) -angleY, true));
                }
            });
        }, 0, 3);
    }

    @Override
    public void lookTo(Location location) {
        send(new ClientboundRotateHeadPacket(SERVER_PLAYER, (byte) (location.getYaw() * 256.0F / 360.0F)));
        send(new ClientboundMoveEntityPacket.Rot(SERVER_PLAYER.getBukkitEntity().getEntityId(), (byte) (location.getYaw() * 256.0F / 360.0F), (byte) (location.getPitch() * 256.0F / 360.0F), true));
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
        isSpawned = true;
        IMPLEMENTED_PLAYERS.forEach(player -> {
            ServerGamePacketListenerImpl connection = ((CraftPlayer) player).getHandle().connection;
            connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, SERVER_PLAYER));
            Bukkit.getScheduler().runTaskLaterAsynchronously(NpcLib.getInstance(), () -> {
                connection.send(new ClientboundPlayerInfoRemovePacket(Collections.singletonList(SERVER_PLAYER.getUUID())));
            }, 20);
            SERVER_PLAYER.moveTo(SPAWN_LOCATION.getX(), SPAWN_LOCATION.getY(), SPAWN_LOCATION.getZ(), SPAWN_LOCATION.getYaw(), SPAWN_LOCATION.getPitch());
            connection.send(new ClientboundAddPlayerPacket(SERVER_PLAYER));
            lookTo(SPAWN_LOCATION);
            DATA.refresh(((CraftPlayer) player).getHandle());
        });
    }

    @Override
    public boolean isSpawned() {
        return isSpawned;
    }

    @Override
    public void despawn() {
        isSpawned = false;
        send(new ClientboundRemoveEntitiesPacket(getEntityId()));
    }

    private void send(Packet<?> packet) {
        IMPLEMENTED_PLAYERS.forEach(player -> {
            ((CraftPlayer) player).getHandle().connection.send(packet);
        });
    }
}
