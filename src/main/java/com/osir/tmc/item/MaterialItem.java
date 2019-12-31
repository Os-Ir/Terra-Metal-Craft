package com.osir.tmc.item;

import com.osir.tmc.Main;

import gregtech.api.items.materialitem.MaterialMetaItem;
import gregtech.api.unification.ore.OrePrefix;

public class MaterialItem extends MaterialMetaItem {
	public MaterialItem() {
		super(OrePrefix.valueOf("ingotDouble"), OrePrefix.valueOf("plateDouble"), null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null, null,
				null, null, null, null, null, null, null);
		this.setRegistryName(Main.MODID, "material_item");
	}
}