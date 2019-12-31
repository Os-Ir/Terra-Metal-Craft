package com.osir.tmc.item;

import com.osir.tmc.CreativeTabList;
import com.osir.tmc.Main;

import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.items.metaitem.StandardMetaItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModMetaItem extends StandardMetaItem {
	public ModMetaItem() {
		super((short) 0);
		this.setRegistryName(Main.MODID, "meta_item");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		if (tab != CreativeTabList.tabItem || tab != CreativeTabs.SEARCH) {
			return;
		}
		for (MetaItem<?>.MetaValueItem enabledItem : metaItems.valueCollection()) {
			if (!enabledItem.isVisible()) {
				continue;
			}
			ItemStack stack = enabledItem.getStackForm();
			enabledItem.getSubItemHandler().getSubItems(stack, tab, subItems);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerSubItems() {
		MetaItems.COIN_TMC = this.addItem(0, "coin.tmc");
	}
}