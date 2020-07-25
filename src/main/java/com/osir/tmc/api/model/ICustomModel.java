package com.osir.tmc.api.model;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;

public interface ICustomModel {
	ModelResourceLocation getBlockModel(ModelRegistryEvent e);

	int getMetaData(ModelRegistryEvent e);
}