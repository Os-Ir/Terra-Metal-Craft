package com.osir.tmc.api.gui.factory;

import com.osir.tmc.Main;
import com.osir.tmc.api.gui.SimpleUIHolder;

import gregtech.api.gui.ModularUI;
import gregtech.api.gui.UIFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SimpleUIFactory extends UIFactory<SimpleUIHolder> {
	public static final SimpleUIFactory INSTANCE = new SimpleUIFactory();

	private SimpleUIFactory() {
		UIFactory.FACTORY_REGISTRY.register(10, new ResourceLocation(Main.MODID, "simple_tile_entity_factory"), this);
	}

	@Override
	protected ModularUI createUITemplate(SimpleUIHolder holder, EntityPlayer entityPlayer) {
		return holder.createUI(entityPlayer);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected SimpleUIHolder readHolderFromSyncData(PacketBuffer syncData) {
		return (SimpleUIHolder) Minecraft.getMinecraft().world.getTileEntity(syncData.readBlockPos());
	}

	@Override
	protected void writeHolderToSyncData(PacketBuffer syncData, SimpleUIHolder holder) {
		syncData.writeBlockPos(((TileEntity) holder).getPos());
	}
}