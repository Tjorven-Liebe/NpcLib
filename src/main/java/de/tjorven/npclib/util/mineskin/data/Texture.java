package de.tjorven.npclib.util.mineskin.data;

import com.mojang.authlib.properties.Property;

public class Texture {

	public String value;
	public String signature;
	public String url;

	public Texture(Property property) {
		this.value = property.value();
		this.signature = property.signature();
	}

	public Texture(String value, String signature) {
		this.value = value;
		this.signature = signature;
	}

	public Texture(String value, String signature, String url) {
		this.value = value;
		this.signature = signature;
		this.url = url;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}