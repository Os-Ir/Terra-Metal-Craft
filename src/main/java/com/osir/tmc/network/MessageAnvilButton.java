package com.osir.tmc.network;

import com.osir.tmc.Main;
import com.osir.tmc.container.ContainerAnvil;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class MessageAnvilButton implements IMessage {
	private int idx;

	public MessageAnvilButton() {

	}

	public MessageAnvilButton(int idx) {
		this.idx = idx;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		this.idx = buf.readInt();
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeInt(this.idx);
	}

	public static class Handler implements IMessageHandler<MessageAnvilButton, IMessage> {
		@Override
		public IMessage onMessage(MessageAnvilButton message, MessageContext ctx) {
			if (ctx.side == Side.SERVER) {
				EntityPlayer player = Main.proxy.getPlayer(ctx);
				if (player.openContainer instanceof ContainerAnvil) {
					ContainerAnvil container = (ContainerAnvil) player.openContainer;
					Main.proxy.getThreadListener(ctx).addScheduledTask(() -> container.receiveMessage(message.idx));
				}
			}
			return null;
		}
	}
}