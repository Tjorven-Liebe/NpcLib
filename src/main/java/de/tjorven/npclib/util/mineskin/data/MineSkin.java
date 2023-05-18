package de.tjorven.npclib.util.mineskin.data;

import com.google.gson.annotations.SerializedName;

public class MineSkin {

    public int id;
    public String uuid;
    public String name;
    public SkinData data;
    public long timestamp;
    @SerializedName("private")
    public boolean prvate;
    public int views;
    public int accountId;
    public double nextRequest;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SkinData getData() {
        return data;
    }

    public void setData(SkinData data) {
        this.data = data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isPrvate() {
        return prvate;
    }

    public void setPrvate(boolean prvate) {
        this.prvate = prvate;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public double getNextRequest() {
        return nextRequest;
    }

    public void setNextRequest(double nextRequest) {
        this.nextRequest = nextRequest;
    }
}
