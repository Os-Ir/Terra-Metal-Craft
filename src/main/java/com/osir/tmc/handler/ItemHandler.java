package com.osir.tmc.handler;

import com.osir.tmc.Main;
import com.osir.tmc.item.ItemMould;
import com.osir.tmc.item.MetaItems;
import com.osir.tmc.item.ModMetaItem;

import gregtech.api.items.metaitem.MetaItem;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = Main.MODID)
public class ItemHandler {
	public static final MetaItem META_ITEM = new ModMetaItem();

	public static final Item ITEM_ORIGINAL_FORGE = new ItemBlock(BlockHandler.ORIGINAL_FORGE)
			.setRegistryName(BlockHandler.ORIGINAL_FORGE.getRegistryName());
	public static final Item ITEM_MOULD = new ItemMould();

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent e) {

	}

	@SideOnly(Side.CLIENT)
	public static void render(Item item, int meta, String name, String type) {
		ModelResourceLocation model = new ModelResourceLocation(name, type);
		ModelLoader.setCustomModelResourceLocation(item, 0, model);
	}

	@SideOnly(Side.CLIENT)
	public static void render(Item item) {
		render(item, 0, item.getRegistryName().toString(), "inventory");
	}

	@SubscribeEvent
	public static void register(Register<Item> e) {
		IForgeRegistry<Item> registry = e.getRegistry();
		MetaItems.preInit();
		registry.register(ITEM_ORIGINAL_FORGE);
		registry.register(ITEM_MOULD);
		for (int i = 0; i < BlockHandler.ANVIL.length; i++) {
			registry.register(
					new ItemBlock(BlockHandler.ANVIL[i]).setRegistryName(BlockHandler.ANVIL[i].getRegistryName()));
		}
	}
}