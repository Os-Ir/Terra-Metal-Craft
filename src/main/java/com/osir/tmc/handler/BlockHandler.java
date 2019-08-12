package com.osir.tmc.handler;

import com.osir.tmc.Main;
import com.osir.tmc.block.BlockMould;
import com.osir.tmc.block.BlockOriginalForge;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = Main.MODID)
public class BlockHandler {
	public static final Block ORIGINAL_FORGE = new BlockOriginalForge();
	public static final Block MOULD = new BlockMould();

	@SideOnly(Side.CLIENT)
	public static void registerRender() {
		render(ORIGINAL_FORGE);
	}

	@SideOnly(Side.CLIENT)
	public static void render(Block block) {
		Item item = Item.getItemFromBlock(block);
		ModelResourceLocation model = new ModelResourceLocation(item.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(item, 0, model);
	}

	@SubscribeEvent
	public static void register(Register<Block> e) {
		IForgeRegistry<Block> registry = e.getRegistry();
		registry.register(ORIGINAL_FORGE);
		registry.register(MOULD);
	}
}