package de.tjorven.npclib.npc.skin;

public class Skin {

    private String name;
    private String signature;
    private String value;

    public Skin(String name, String signature, String value) {
        this.name = name;
        this.signature = signature;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
