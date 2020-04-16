package com.osir.tmc.api.te;

import java.util.HashMap;
import java.util.Map;

import com.osir.tmc.api.block.MetaBlock;

import gregtech.api.util.GTControlledRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class MetaTileEntityRegistry {
	public static final Map<String, GTControlledRegistry<String, MetaValueTileEntity>> REGISTRY = new HashMap<String, GTControlledRegistry<String, MetaValueTileEntity>>();
	public static final Map<String, Block> REGISTRY_BLOCK = new HashMap<String, Block>();

	public static void register(int id, MetaValueTileEntity meta) {
		String modid = meta.getModid();
		REGISTRY.putIfAbsent(modid, new GTControlledRegistry<String, MetaValueTileEntity>(1000));
		GTControlledRegistry<String, MetaValueTileEntity> registry = REGISTRY.get(modid);
		registry.register(id, meta.getModid(), meta);
		REGISTRY_BLOCK.putIfAbsent(modid, new MetaBlock(modid));
	}

	public static GTControlledRegistry<String, MetaValueTileEntity> getRegistry(Block block) {
		if (!(block instanceof MetaBlock)) {
			return null;
		}
		return REGISTRY.get(((MetaBlock) block).getModid());
	}

	public static MetaValueTileEntity getMetaTileEntity(Block block, int id) {
		return getRegistry(block).getObjectById(id);
	}

	public static MetaValueTileEntity getMetaTileEntity(ItemStack stack) {
		return getMetaTileEntity(((ItemBlock) stack.getItem()).getBlock(), stack.getMetadata());
	}

	public static int getId(MetaValueTileEntity meta) {
		return REGISTRY.get(meta.getModid()).getIdByObjectName(meta.getName());
	}
}