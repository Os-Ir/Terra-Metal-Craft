package com.osir.tmc.handler;

import javax.annotation.Nonnull;

import com.osir.tmc.Main;
import com.osir.tmc.api.util.AnvilMaterialList;
import com.osir.tmc.block.BlockAnvil;
import com.osir.tmc.block.BlockMould;
import com.osir.tmc.block.BlockOriginalForge;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
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
	public static final Block ANVIL_STEEL = new BlockAnvil(AnvilMaterialList.STEEL);

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent e) {
		render(MOULD);
		render(ORIGINAL_FORGE);
		render(ANVIL_STEEL, 0, "tmc:anvil", "inventory");
		ModelLoader.setCustomStateMapper(ANVIL_STEEL, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				return new ModelResourceLocation(Main.MODID + ":anvil", getPropertyString(state.getProperties()));
			}
		});
	}

	@SideOnly(Side.CLIENT)
	public static void render(Block block, int meta, String name, String type) {
		ItemHandler.render(Item.getItemFromBlock(block), meta, name, type);
	}

	@SideOnly(Side.CLIENT)
	public static void render(Block block) {
		ItemHandler.render(Item.getItemFromBlock(block));
	}

	@SubscribeEvent
	public static void register(Register<Block> e) {
		IForgeRegistry<Block> registry = e.getRegistry();
		registry.register(ORIGINAL_FORGE);
		registry.register(MOULD);
		registry.register(ANVIL_STEEL);
	}
}