package com.osir.tmc.api.gui;

import java.util.function.DoubleSupplier;

import gregtech.api.gui.IRenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.util.Position;
import gregtech.api.util.Size;
import net.minecraft.network.PacketBuffer;

public class PointerWidget extends Widget {
	public enum MoveType {
		HORIZONTAL, HORIZONTAL_INVERTED, VERTICAL, VERTICAL_INVERTED
	}

	private DoubleSupplier supplier;
	private int length;
	private double progress;
	private MoveType moveType;
	private TextureArea texture;

	public PointerWidget(DoubleSupplier progress, int x, int y, int width, int height) {
		super(new Position(x, y), new Size(width, height));
		this.supplier = progress;
	}

	public PointerWidget(DoubleSupplier progress, int x, int y, int width, int height, int length, TextureArea texture,
			MoveType moveType) {
		this(progress, x, y, width, height);
		this.length = length;
		this.texture = texture;
		this.moveType = moveType;
	}

	public PointerWidget setPointer(int length, TextureArea texture, MoveType moveType) {
		this.length = length;
		this.texture = texture;
		this.moveType = moveType;
		return this;
	}

	@Override
	public void drawInBackground(int mouseX, int mouseY, IRenderContext context) {
		if (this.texture == null) {
			return;
		}
		Position pos = getPosition();
		Size size = getSize();
		switch (this.moveType) {
		case HORIZONTAL:
			this.texture.drawSubArea((int) (pos.x + this.progress * this.length), pos.y, size.width, size.height, 0, 0,
					1, 1);
			break;
		case HORIZONTAL_INVERTED:
			this.texture.drawSubArea((int) (pos.x - this.progress * this.length), pos.y, size.width, size.height, 0, 0,
					1, 1);
			break;
		case VERTICAL:
			this.texture.drawSubArea(pos.x, (int) (pos.y + this.progress * this.length), size.width, size.height, 0, 0,
					1, 1);
			break;
		case VERTICAL_INVERTED:
			this.texture.drawSubArea(pos.x, (int) (pos.y - this.progress * this.length), size.width, size.height, 0, 0,
					1, 1);
			break;
		default:
			break;
		}
	}

	@Override
	public void detectAndSendChanges() {
		double actual = this.supplier.getAsDouble();
		if (Math.abs(this.progress - actual) > 0.005) {
			this.progress = actual;
			writeUpdateInfo(0, (buffer) -> buffer.writeDouble(actual));
		}
	}

	@Override
	public void readUpdateInfo(int id, PacketBuffer buffer) {
		if (id == 0) {
			this.progress = buffer.readDouble();
		}
	}
}