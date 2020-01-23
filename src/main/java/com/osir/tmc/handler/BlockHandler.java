package com.osir.tmc.handler;

import com.osir.tmc.Main;
import com.osir.tmc.block.AnvilMaterialList;
import com.osir.tmc.block.BlockAnvil;
import com.osir.tmc.block.BlockMould;
import com.osir.tmc.block.BlockOriginalForge;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

@EventBusSubscriber(modid = Main.MODID)
public class BlockHandler {
	public static final BlockOriginalForge ORIGINAL_FORGE = new BlockOriginalForge();
	public static final BlockMould MOULD = new BlockMould();
	public static final BlockAnvil[] ANVIL = new BlockAnvil[] { new BlockAnvil(AnvilMaterialList.STONE),
			new BlockAnvil(AnvilMaterialList.COPPER), new BlockAnvil(AnvilMaterialList.BRONZE),
			new BlockAnvil(AnvilMaterialList.IRON), new BlockAnvil(AnvilMaterialList.STEEL) };

	@SubscribeEvent
	public static void onModelRegister(ModelRegistryEvent e) {
		render(MOULD);
		render(ORIGINAL_FORGE);
		for (int i = 0; i < ANVIL.length; i++) {
			render(ANVIL[i], 0, "tmc:anvil", "inventory");
			ANVIL[i].registerModel();
		}
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
		for (int i = 0; i < ANVIL.length; i++) {
			registry.register(ANVIL[i]);
		}
	}
}