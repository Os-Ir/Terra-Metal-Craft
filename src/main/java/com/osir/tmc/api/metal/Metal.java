package com.osir.tmc.api.metal;

import com.osir.tmc.api.heat.HeatMaterial;

import net.minecraft.item.Item;

public class Metal {
	private HeatMaterial material;
	private String name;
	private Item ingot, melted;

	public Metal(String name, HeatMaterial material) {
		this.name = name;
		this.material = material;
	}

	public Metal(String name, Item ingot, Item melted, HeatMaterial material) {
		this(name, material);
		this.ingot = ingot;
		this.melted = melted;
	}

	public HeatMaterial getMaterial() {
		return this.material;
	}

	public String getName() {
		return this.name;
	}

	public Item getIngot() {
		return this.ingot;
	}

	public Item getMelted() {
		return this.melted;
	}
}