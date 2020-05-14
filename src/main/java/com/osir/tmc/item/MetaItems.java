package com.osir.tmc.item;

import gregtech.api.items.metaitem.MetaItem;

public class MetaItems {
	public static MetaItem.MetaValueItem coin;
	public static MetaItem.MetaValueItem chippedFlint;
	public static MetaItem.MetaValueItem grindedFlint;

	public static void init() {
		new ModMetaItem();
	}
}