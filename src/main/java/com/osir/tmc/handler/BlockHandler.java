package com.osir.tmc.handler;

import com.github.zi_jing.cuckoolib.util.registry.UnorderedRegistry;
import com.osir.tmc.Main;
import com.osir.tmc.api.te.MetaTileEntity;
import com.osir.tmc.api.te.MetaTileEntityRegistry;
import com.osir.tmc.block.AnvilMaterialList;
import com.osir.tmc.block.BlockAnvil;
import com.osir.tmc.block.BlockBasin;
import com.osir.tmc.block.BlockMould;
import com.osir.tmc.block.BlockOriginalForge;
import com.osir.tmc.te.MTEBlower;
import com.osir.tmc.te.MTEStoneWorkTable;
import com.osir.tmc.te.TEAnvil;
import com.osir.tmc.te.TEBasin;
import com.osir.tmc.te.TEMould;
import com.osir.tmc.te.TEOriginalForge;

import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@EventBusSubscriber(modid = Main.MODID)
public class BlockHandler {
	public static final UnorderedRegistry<Block> BLOCK_REGISTRY = new UnorderedRegistry<Block>();

	public static final BlockOriginalForge ORIGINAL_FORGE = new BlockOriginalForge();
	public static final BlockMould MOULD = new BlockMould();
	public static final BlockBasin BASIN = new BlockBasin();
	public static final BlockAnvil[] ANVIL = new BlockAnvil[] { new BlockAnvil(AnvilMaterialList.STONE),
			new BlockAnvil(AnvilMaterialList.COPPER), new BlockAnvil(AnvilMaterialList.BRONZE),
			new BlockAnvil(AnvilMaterialList.IRON), new BlockAnvil(AnvilMaterialList.STEEL) };

	@SubscribeEvent
	public static void register(Register<Block> e) {
		e.getRegistry().registerAll(BLOCK_REGISTRY.list().toArray(new Block[0]));
	}

	public static void registerTileEntity() {
		GameRegistry.registerTileEntity(TEOriginalForge.class, new ResourceLocation(Main.MODID, "original_forge"));
		GameRegistry.registerTileEntity(TEBasin.class, new ResourceLocation(Main.MODID, "basin"));
		GameRegistry.registerTileEntity(TEAnvil.class, new ResourceLocation(Main.MODID, "anvil"));
		GameRegistry.registerTileEntity(TEMould.class, new ResourceLocation(Main.MODID, "mould"));
		GameRegistry.registerTileEntity(MetaTileEntity.class, new ResourceLocation(Main.MODID, "meta_tile_entity"));

		MetaTileEntityRegistry.register(0, new MTEBlower());
		MetaTileEntityRegistry.register(1, new MTEStoneWorkTable());
	}
}