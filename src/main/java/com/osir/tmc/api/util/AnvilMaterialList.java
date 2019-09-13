package com.osir.tmc.api.util;

import net.minecraft.block.material.Material;

public enum AnvilMaterialList {
	 STONE(Material.ROCK,  "stone", 0),
	COPPER(Material.IRON, "copper", 1),
	BRONZE(Material.IRON, "bronze", 2),
	  IRON(Material.IRON,   "iron", 3),
	 STEEL(Material.IRON,  "steel", 4);

	private Material material;
	private String anvilMaterial;
	private int level;

	private AnvilMaterialList(Material material, String anvilMaterial, int level) {
		this.material = material;
		this.anvilMaterial = anvilMaterial;
		this.level = level;
	}

	public Material getMaterial() {
		return this.material;
	}

	public String getAnvilMaterial() {
		return this.anvilMaterial;
	}

	public int getLevel() {
		return this.level;
	}
}