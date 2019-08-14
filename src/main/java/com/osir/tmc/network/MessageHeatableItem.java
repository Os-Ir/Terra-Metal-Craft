package com.osir.tmc.network;

import com.osir.tmc.api.inter.IHeatable;
import com.osir.tmc.handler.CapabilityHandler;

import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageHeatableItem implements IMessage {
	private static final Capability<IHeatable> CAPABILITY = CapabilityHandler.heatable;
	private static final IStorage<IHeatable> STORAGE = CAPABILITY.getStorage();
	public TIntObjectMap<NBTTagCompound> data = new TIntObjectHashMap<NBTTagCompound>();
	public int window;

	public MessageHeatableItem(int window, int slot, ItemStack stack) {
		this.window = window;
		if (stack.hasCapability(CAPABILITY, null)) {
			NBTTagCompound nbt = (NBTTagCompound) STORAGE.writeNBT(CAPABILITY, stack.getCapability(CAPABILITY, null),
					null);
			if (nbt != null) {
				data.put(slot, nbt);
			}
		}
	}

	public MessageHeatableItem(int window, NonNullList<ItemStack> items) {
		this.window = window;
		int i;
		for (i = 0; i < items.size(); i++) {
			ItemStack stack = items.get(i);
			if (stack.hasCapability(CAPABILITY, null)) {
				NBTTagCompound nbt = (NBTTagCompound) STORAGE.writeNBT(CAPABILITY,
						stack.getCapability(CAPABILITY, null), null);
				if (nbt != null) {
					data.put(i, nbt);
				}
			}
		}
	}

	public boolean hasData() {
		return !data.isEmpty();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.window = buf.readInt();
		int i, len = buf.readInt();
		for (i = 0; i < len; i++) {
			int idx = buf.readInt();
			NBTTagCompound nbt = ByteBufUtils.readTag(buf);
			this.data.put(idx, nbt);
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.window);
		buf.writeInt(this.data.size());
		data.forEachEntry((index, nbt) -> {
			buf.writeInt(index);
			ByteBufUtils.writeTag(buf, nbt);
			return true;
		});
	}

	public static class Handler implements IMessageHandler<MessageHeatableItem, IMessage> {
		@Override
		public IMessage onMessage(MessageHeatableItem message, MessageContext ctx) {
			if (message.hasData()) {
				EntityPlayerMP player = ctx.getServerHandler().player;
				player.getServer().addScheduledTask(() -> {
					Container container;
					if (message.window == 0) {
						container = player.inventoryContainer;
					} else if (message.window == player.openContainer.windowId) {
						container = player.openContainer;
					} else {
						return;
					}
					message.data.forEachEntry((idx, nbt) -> {
						ItemStack stack = container.getSlot(idx).getStack();
						IHeatable cap = stack.getCapability(CapabilityHandler.heatable, null);
						if (cap != null) {
							IStorage<IHeatable> storage = CapabilityHandler.heatable.getStorage();
							storage.readNBT(CapabilityHandler.heatable, cap, null, nbt);
						}
						return true;
					});
				});
			}
			return null;
		}
	}
}