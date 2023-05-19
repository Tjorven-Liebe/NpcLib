package de.tjorven.npclib.npc.skin;

public enum ClothType {

    CAPE_ENABLED((byte) 0x01),
    JACKET_ENABLED((byte) 0x02),
    LEFT_SLEEVE((byte) 0x04),
    RIGHT_SLEEVE((byte) 0x08),
    LEFT_PANTS_LEG((byte) 0x10),
    RIGHT_PANTS_LEG((byte) 0x20),
    HAT((byte) 0x40),
    ALL((byte) 0xff);

    private final byte bite;

    ClothType(byte type) {
        this.bite = type;
    }

    public byte getByte() {
        return bite;
    }
}
