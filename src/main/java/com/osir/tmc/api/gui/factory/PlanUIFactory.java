package com.osir.tmc.api.gui.factory;

import com.osir.tmc.Main;
import com.osir.tmc.api.gui.PlanUIHolder;
import com.osir.tmc.api.gui.PlanUIProvider;

import gregtech.api.gui.ModularUI;
import gregtech.api.gui.UIFactory;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlanUIFactory extends UIFactory<PlanUIHolder> {
	public static final PlanUIFactory INSTANCE = new PlanUIFactory();

	private PlanUIFactory() {
		UIFactory.FACTORY_REGISTRY.register(12, new ResourceLocation(Main.MODID, "plan_factory"), this);
	}

	@Override
	protected ModularUI createUITemplate(PlanUIHolder holder, EntityPlayer player) {
		return holder.createUI(player);
	}

	@Override
	@SideOnly(Side.CLIENT)
	protected PlanUIHolder readHolderFromSyncData(PacketBuffer syncData) {
		return ((PlanUIProvider) (Minecraft.getMinecraft().world.getTileEntity(syncData.readBlockPos())))
				.createHolder();
	}

	@Override
	protected void writeHolderToSyncData(PacketBuffer syncData, PlanUIHolder holder) {
		syncData.writeBlockPos(holder.getPos());
	}
}