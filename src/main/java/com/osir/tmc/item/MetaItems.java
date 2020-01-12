package com.osir.tmc.item;

import com.osir.tmc.Main;

import gregtech.api.items.materialitem.MaterialMetaItem;
import gregtech.api.items.metaitem.MetaItem;

public class MetaItems {
	public static MetaItem<?>.MetaValueItem COIN_TMC;

	public static void preInit() {
		new ModMetaItem();
		new MaterialItem();
	}
}