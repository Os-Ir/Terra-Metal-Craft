package com.osir.tmc.handler;

import com.osir.tmc.Main;
import com.osir.tmc.block.BlockMould;
import com.osir.tmc.block.BlockOriginalForge;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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

	@SideOnly(Side.CLIENT)
	public static void registerRender() {
		render(ORIGINAL_FORGE);
	}

	@SideOnly(Side.CLIENT)
	public static void render(Block block) {
		ModelResourceLocation model = new ModelResourceLocation(block.getRegistryName(), "inventory");
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, model);
	}

	@SubscribeEvent
	public static void register(Register<Block> e) {
		IForgeRegistry<Block> registry = e.getRegistry();
		registry.register(ORIGINAL_FORGE);
	}
}