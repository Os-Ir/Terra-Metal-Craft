package com.osir.tmc.item;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;

import com.osir.tmc.CreativeTabList;
import com.osir.tmc.Main;
import com.osir.tmc.api.item.DurabilityManager;

import gregtech.api.GregTechAPI;
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
		if (tab == GregTechAPI.TAB_GREGTECH_MATERIALS || tab == CreativeTabs.SEARCH) {
			try {
				Field fieldItem = Arrays.stream(MaterialMetaItem.class.getDeclaredFields())
						.filter((field) -> field.getName().equals("generatedItems")).findFirst()
						.orElseThrow(ReflectiveOperationException::new);
				fieldItem.setAccessible(true);
				ArrayList<Short> item = (ArrayList<Short>) fieldItem.get(this);
				for (short metadata : item) {
					subItems.add(new ItemStack(this, 1, metadata));
				}
			} catch (ReflectiveOperationException exception) {
				throw new RuntimeException(exception);
			}
		}
		if (tab == CreativeTabList.tabItem || tab == CreativeTabs.SEARCH) {
			for (MetaItem<?>.MetaValueItem metaItem : this.metaItems.valueCollection()) {
				if (!metaItem.isVisible()) {
					continue;
				}
				metaItem.getSubItemHandler().getSubItems(metaItem.getStackForm(), tab, subItems);
			}
		}
	}

	@Override
	public void registerSubItems() {
		MetaItems.coin = this.addItem(0, "coin.tmc");
		MetaItems.grindedFlint = this.addItem(1, "grinded_flint").addComponents(DurabilityManager.INSTANCE)
				.setMaxStackSize(1);
		MetaItems.chippedFlint = this.addItem(2, "chipped_flint").addComponents(DurabilityManager.INSTANCE)
				.setMaxStackSize(1);
	}
}