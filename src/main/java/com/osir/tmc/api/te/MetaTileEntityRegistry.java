package com.osir.tmc.api.te;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.osir.tmc.api.block.MetaBlock;

import gregtech.api.util.GTControlledRegistry;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class MetaTileEntityRegistry {
	private static final Map<Pair<String, Material>, GTControlledRegistry<String, MetaValueTileEntity>> REGISTRY_MTE = new HashMap<Pair<String, Material>, GTControlledRegistry<String, MetaValueTileEntity>>();
	private static final Map<Pair<String, Material>, MetaBlock> REGISTRY_BLOCK = new HashMap<Pair<String, Material>, MetaBlock>();

	private static Pair<String, Material> getRegistryKey(String modid, Material material) {
		return Pair.of(modid, material);
	}

	private static Pair<String, Material> getRegistryKey(MetaValueTileEntity meta) {
		return getRegistryKey(meta.getModid(), meta.getMaterial());
	}

	private static Pair<String, Material> getRegistryKey(MetaBlock block) {
		return getRegistryKey(block.getModid(), block.getDefaultState().getMaterial());
	}

	public static void register(int id, MetaValueTileEntity meta) {
		String modid = meta.getModid();
		Material material = meta.getMaterial();
		Pair<String, Material> key = getRegistryKey(modid, material);
		if (!REGISTRY_MTE.containsKey(key)) {
			REGISTRY_MTE.put(key, new GTControlledRegistry<String, MetaValueTileEntity>(100));
		}
		if (!REGISTRY_BLOCK.containsKey(key)) {
			REGISTRY_BLOCK.put(key, new MetaBlock(modid, material));
		}
		GTControlledRegistry<String, MetaValueTileEntity> registryMTE = REGISTRY_MTE.get(key);
		registryMTE.register(id, meta.getName(), meta);
	}

	public static GTControlledRegistry<String, MetaValueTileEntity> getMTERegistry(MetaBlock block) {
		return REGISTRY_MTE.get(getRegistryKey(block));
	}

	public static MetaValueTileEntity getMetaTileEntity(String modid, Material material, String name) {
		return REGISTRY_MTE.get(getRegistryKey(modid, material)).getObject(name);
	}

	public static MetaValueTileEntity getMetaTileEntity(String modid, Material material, int id) {
		return REGISTRY_MTE.get(getRegistryKey(modid, material)).getObjectById(id);
	}

	public static MetaValueTileEntity getMetaTileEntity(MetaBlock block, int id) {
		return getMTERegistry(block).getObjectById(id);
	}

	public static MetaValueTileEntity getMetaTileEntity(ItemStack stack) {
		return getMetaTileEntity(getBlock(stack), stack.getMetadata());
	}

	public static int getId(MetaValueTileEntity meta) {
		return REGISTRY_MTE.get(getRegistryKey(meta)).getIdByObjectName(meta.getName());
	}

	public static List<MetaBlock> getBlockList() {
		List<MetaBlock> list = new ArrayList<MetaBlock>();
		REGISTRY_BLOCK.keySet().forEach((key) -> list.add(REGISTRY_BLOCK.get(key)));
		return list;
	}

	public static MetaBlock getBlock(String modid, Material material) {
		return REGISTRY_BLOCK.get(getRegistryKey(modid, material));
	}

	public static MetaBlock getBlock(MetaValueTileEntity mte) {
		return getBlock(mte.getModid(), mte.getMaterial());
	}

	public static MetaBlock getBlock(ItemStack stack) {
		return (MetaBlock) ((ItemBlock) stack.getItem()).getBlock();
	}
}