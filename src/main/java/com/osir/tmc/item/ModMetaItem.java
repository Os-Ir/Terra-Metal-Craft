package com.osir.tmc.item;

import gregtech.api.items.metaitem.StandardMetaItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModMetaItem extends StandardMetaItem {
	public ModMetaItem(short metaItemOffset) {
		super(metaItemOffset);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerSubItems() {
		MetaItems.COIN = this.addItem(0, "coin");
	}
}