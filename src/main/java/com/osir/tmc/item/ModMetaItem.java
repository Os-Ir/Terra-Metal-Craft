package com.osir.tmc.item;

import com.osir.tmc.CreativeTabList;
import com.osir.tmc.Main;
import com.osir.tmc.api.TMCLog;

import gregtech.api.items.materialitem.MaterialMetaItem;
import gregtech.api.items.metaitem.MetaItem;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModMetaItem extends MaterialMetaItem {
	public ModMetaItem() {
		super(OrePrefix.valueOf("ingotDouble"), OrePrefix.valueOf("plateDouble"), null, null, null, null, null, null,
				null, null, null, null, null, null, null, null, OrePrefix.valueOf("oreCobble"), null, null, null, null,
				null, null, null, null, null, null, null, null, null, null, null);
		this.setRegistryName(Main.MODID, "meta_item");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> subItems) {
		super.getSubItems(tab, subItems);
		if (tab != CreativeTabList.tabItem && tab != CreativeTabs.SEARCH) {
			return;
		}
		for (MetaItem<?>.MetaValueItem metaItem : this.metaItems.valueCollection()) {
			if (!metaItem.isVisible()) {
				continue;
			}
			metaItem.getSubItemHandler().getSubItems(metaItem.getStackForm(), tab, subItems);
		}
	}

	@Override
	public void registerSubItems() {
		MetaItems.COIN_TMC = this.addItem(0, "coin.tmc");
	}
}