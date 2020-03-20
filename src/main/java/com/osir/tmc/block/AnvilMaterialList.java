package com.osir.tmc.block;

import net.minecraft.block.material.Material;

public enum AnvilMaterialList {
	 STONE(Material.ROCK,  "stone", 0, 0x828282),
	COPPER(Material.IRON, "copper", 1, 0xff8000),
	BRONZE(Material.IRON, "bronze", 2, 0xa0501e),
	  IRON(Material.IRON,   "iron", 3, 0xaaaaaa),
	 STEEL(Material.IRON,  "steel", 4, 0x505050);

	private Material material;
	private String anvilMaterial;
	private int level, color;

	AnvilMaterialList(Material material, String anvilMaterial, int level, int color) {
		this.material = material;
		this.anvilMaterial = anvilMaterial;
		this.level = level;
		this.color = color;
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

	public int getColor() {
		return this.color;
	}
}