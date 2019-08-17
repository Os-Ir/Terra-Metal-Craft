package com.osir.tmc.handler;

import com.osir.tmc.CreativeTabList;
import com.osir.tmc.Main;

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
	public static final Item COIN = new Item().setUnlocalizedName("coin").setCreativeTab(CreativeTabList.tabItem)
			.setRegistryName("coin");
	public static final Item ITEM_ORIGINAL_FORGE = new ItemBlock(BlockHandler.ORIGINAL_FORGE)
			.setRegistryName(BlockHandler.ORIGINAL_FORGE.getRegistryName());
	public static final Item ITEM_MOULD = new ItemBlock(BlockHandler.MOULD)
			.setRegistryName(BlockHandler.MOULD.getRegistryName());

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent e) {
		render(COIN);
		render(ITEM_MOULD);
		render(ITEM_ORIGINAL_FORGE);
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
		registry.register(COIN);
		registry.register(ITEM_ORIGINAL_FORGE);
		registry.register(ITEM_MOULD);
	}
}