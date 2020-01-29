package com.osir.tmc.api.gui;

import java.util.function.Supplier;

import gregtech.api.gui.IRenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.util.Position;
import gregtech.api.util.Size;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UpdatableTextWidget extends Widget {
	protected Supplier<String> textSupplier;
	protected Supplier<Integer> colorSupplier;
	protected boolean xCentered;
	protected int color;
	protected String text;

	public UpdatableTextWidget(Supplier<String> text, Supplier<Integer> color, int x, int y, boolean xCentered) {
		this(text, color, x, y);
		this.xCentered = xCentered;
	}

	public UpdatableTextWidget(Supplier<String> text, Supplier<Integer> color, int x, int y) {
		super(new Position(x, y), Size.ZERO);
		this.textSupplier = text;
		this.colorSupplier = color;
		this.recomputeSize(text.get());
	}

	public UpdatableTextWidget setXCentered(boolean xCentered) {
		this.xCentered = xCentered;
		return this;
	}

	protected void recomputeSize(String str) {
		if (isClientSide()) {
			FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
			this.setSize(new Size(renderer.getStringWidth(str), renderer.FONT_HEIGHT));
			if (this.uiAccess != null) {
				this.uiAccess.notifySizeChange();
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void drawInBackground(int mouseX, int mouseY, IRenderContext context) {
		FontRenderer renderer = Minecraft.getMinecraft().fontRenderer;
		Position pos = this.getPosition();
		if (this.xCentered) {
			renderer.drawString(this.text, pos.x - renderer.getStringWidth(this.text) / 2, pos.y, this.color);
		} else {
			renderer.drawString(this.text, pos.x, pos.y, this.color);
		}
		GlStateManager.color(1, 1, 1);
	}

	@Override
	public void detectAndSendChanges() {
		String actualText = this.textSupplier.get();
		int actualColor = this.colorSupplier.get();
		if (!actualText.equals(this.text)) {
			this.text = actualText;
			this.writeUpdateInfo(0, (buffer) -> buffer.writeString(this.text));
			this.recomputeSize(this.text);
		}
		if (actualColor != this.color) {
			this.color = actualColor;
			this.writeUpdateInfo(1, (buffer) -> buffer.writeInt(this.color));
		}
	}

	@Override
	public void readUpdateInfo(int id, PacketBuffer buffer) {
		if (id == 0) {
			this.text = buffer.readString(Short.MAX_VALUE);
			this.recomputeSize(this.text);
		}
		if (id == 1) {
			this.color = buffer.readInt();
		}
	}
}