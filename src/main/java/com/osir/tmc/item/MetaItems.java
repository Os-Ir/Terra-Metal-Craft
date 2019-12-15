package com.osir.tmc.item;

import com.osir.tmc.Main;

import gregtech.api.items.materialitem.MaterialMetaItem;
import gregtech.api.items.metaitem.MetaItem;

public class MetaItems {
	public static MaterialMetaItem MATERIAL_ITEM;

	public static MetaItem<?>.MetaValueItem COIN;

	public static void preInit() {
		MATERIAL_ITEM = new MaterialItem();
		MATERIAL_ITEM.setRegistryName(Main.MODID, "material_item");
	}
}