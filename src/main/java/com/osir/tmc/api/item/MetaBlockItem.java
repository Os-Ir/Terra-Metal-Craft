package com.osir.tmc.api.item;

import com.osir.tmc.api.block.MetaBlock;
import com.osir.tmc.api.te.MetaTileEntityRegistry;
import com.osir.tmc.api.te.MetaValueTileEntity;

import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class MetaBlockItem extends ItemBlock {
	public MetaBlockItem(MetaBlock block) {
		super(block);
		this.setRegistryName(block.getRegistryName());
		this.setHasSubtypes(true);
	}

	public String getUnlocalizedName(ItemStack stack) {
		MetaValueTileEntity meta = MetaTileEntityRegistry.getMetaTileEntity(stack);
		if (meta != null) {
			return "tile." + meta.getModid() + "." + meta.getName();
		}
		return "tile.unnamed";
	}
}