package com.osir.tmc.handler;

import com.osir.tmc.Main;
import com.osir.tmc.api.render.ICustomModel;
import com.osir.tmc.api.render.IStateMapperModel;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = Main.MODID, value = Side.CLIENT)
@SideOnly(Side.CLIENT)
public class ModelHandler {
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelRegister(ModelRegistryEvent e) {
		BlockHandler.BLOCK_REGISTRY.stream().filter((block) -> block instanceof ICustomModel)
				.forEach((block) -> ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block),
						((ICustomModel) block).getMetaData(e), ((ICustomModel) block).getBlockModel(e)));
		BlockHandler.BLOCK_REGISTRY.stream().filter((block) -> block instanceof IStateMapperModel).forEach(
				(block) -> ModelLoader.setCustomStateMapper(block, ((IStateMapperModel) block).getStateMapper(e)));
	}
}