package de.tjorven.npclib.util.mineskin.data;

import java.util.UUID;

public class SkinData {

	public UUID    uuid;
	public Texture texture;

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture texture) {
		this.texture = texture;
	}
}
