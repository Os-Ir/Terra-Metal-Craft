package com.osir.tmc.api.gui.factory;

import java.util.ArrayList;

import com.osir.tmc.Main;
import com.osir.tmc.api.container.CapabilitySyncedModularUIContainer;
import com.osir.tmc.api.gui.SimpleUIHolder;

import gregtech.api.gui.ModularUI;
import gregtech.api.gui.UIFactory;
import gregtech.api.gui.Widget;
import gregtech.api.net.NetworkHandler;
import gregtech.api.net.PacketUIOpen;
import gregtech.api.net.PacketUIWidgetUpdate;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CapabilitySyncedUIFactory extends UIFactory<SimpleUIHolder> {
	public static final CapabilitySyncedUIFactory INSTANCE = new CapabilitySyncedUIFactory();

	private CapabilitySyncedUIFactory() {
		UIFactory.FACTORY_REGISTRY.register(11, new ResourceLocation(Main.MODID, "synced_tile_entity_factory"), this);
	}

	public void openSyncedUI(SimpleUIHolder holder, EntityPlayerMP player) {
		if (player instanceof FakePlayer) {
			return;
		}
		ModularUI uiTemplate = this.createUITemplate(holder, player);
		uiTemplate.initWidgets();
		player.getNextWindowId();
		player.closeContainer();
		int currentWindowId = player.currentWindowId;
		PacketBuffer serializedHolder = new PacketBuffer(Unpooled.buffer());
		this.writeHolderToSyncData(serializedHolder, holder);
		int uiFactoryId = FACTORY_REGISTRY.getIDForObject(this);
		CapabilitySyncedModularUIContainer container = new CapabilitySyncedModularUIContainer(uiTemplate);
		container.windowId = currentWindowId;
		container.accumulateWidgetUpdateData = true;
		uiTemplate.guiWidgets.values().forEach(Widget::detectAndSendChanges);
		container.accumulateWidgetUpdateData = false;
		ArrayList<PacketUIWidgetUpdate> updateData = new ArrayList<>(container.accumulatedUpdates);
		container.accumulatedUpdates.clear();
		PacketUIOpen packet = new PacketUIOpen(uiFactoryId, serializedHolder, currentWindowId, updateData);
		NetworkHandler.channel.sendTo(NetworkHandler.packet2proxy(packet), player);
		container.addListener(player);
		player.openContainer = container;
		MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, container));
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