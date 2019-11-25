package com.osir.tmc;

import com.osir.tmc.handler.BlockHandler;
import com.osir.tmc.handler.ItemHandler;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

public class CreativeTabList {
	public static CreativeTabs tabEquipment;
	public static CreativeTabs tabItem;
	public static CreativeTabs tabMaterial;

	public static void register() {
		tabEquipment = new CreativeTabs(Main.MODID + ".equipment") {
			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(BlockHandler.ORIGINAL_FORGE);
			}
		};
		tabItem = new CreativeTabs(Main.MODID + ".item") {
			@Override
			public ItemStack getTabIconItem() {
				return new ItemStack(ItemHandler.COIN);
			}
		};
	}
}