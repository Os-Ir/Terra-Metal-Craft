package com.osir.tmc.network;

import com.osir.tmc.Main;
import com.osir.tmc.api.container.ContainerListenerCapability;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageCapabilityUpdate implements IMessage {
	private TIntObjectMap<NBTTagCompound> data = new TIntObjectHashMap<NBTTagCompound>();
	private int window;

	public MessageCapabilityUpdate() {

	}

	public MessageCapabilityUpdate(int window, int slot, ItemStack stack) {
		this.window = window;
		NBTTagCompound nbt = ContainerListenerCapability.readCapabilityData(stack);
		if (!nbt.hasNoTags()) {
			data.put(slot, nbt);
		}
	}

	public MessageCapabilityUpdate(int window, NonNullList<ItemStack> items) {
		this.window = window;
		for (int i = 0; i < items.size(); i++) {
			ItemStack stack = items.get(i);
			NBTTagCompound nbt = ContainerListenerCapability.readCapabilityData(stack);
			if (!nbt.hasNoTags()) {
				data.put(i, nbt);
			}
		}
	}

	public boolean hasData() {
		return !this.data.isEmpty();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.window = buf.readInt();
		int size = buf.readInt();
		for (int i = 0; i < size; i++) {
			int slot = buf.readInt();
			NBTTagCompound nbt = ByteBufUtils.readTag(buf);
			this.data.put(slot, nbt);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.window);
		buf.writeInt(this.data.size());
		this.data.forEachEntry((slot, nbt) -> {
			buf.writeInt(slot);
			ByteBufUtils.writeTag(buf, nbt);
			return true;
		});
	}

	public static class Handler implements IMessageHandler<MessageCapabilityUpdate, IMessage> {
		@Override
		public IMessage onMessage(MessageCapabilityUpdate msg, MessageContext ctx) {
			if (!msg.hasData()) {
				return null;
			}
			Main.proxy.getThreadListener(ctx).addScheduledTask(() -> {
				EntityPlayer player = Main.proxy.getPlayer(ctx);
				Container container;
				if (player != null) {
					if (msg.window == 0) {
						container = player.inventoryContainer;
					} else if (msg.window == player.openContainer.windowId) {
						container = player.openContainer;
					} else {
						return;
					}
					msg.data.forEachEntry((slot, nbt) -> {
						ContainerListenerCapability.applyCapabilityData(container.getSlot(slot).getStack(), nbt);
						return true;
					});
				}
			});
			return null;
		}
	}
}