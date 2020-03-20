package com.osir.tmc.api.gui.widget;

import java.util.function.IntSupplier;

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

	protected IntSupplier supplier;
	protected int progress;
	protected MoveType moveType;
	protected TextureArea texture;

	public PointerWidget(IntSupplier progress, int x, int y, int width, int height) {
		super(new Position(x, y), new Size(width, height));
		this.supplier = progress;
	}

	public PointerWidget(IntSupplier progress, int x, int y, int width, int height, TextureArea texture,
			MoveType moveType) {
		this(progress, x, y, width, height);
		this.texture = texture;
		this.moveType = moveType;
	}

	public PointerWidget setPointer(TextureArea texture, MoveType moveType) {
		this.texture = texture;
		this.moveType = moveType;
		return this;
	}

	@Override
	public void drawInBackground(int mouseX, int mouseY, IRenderContext context) {
		if (this.texture == null) {
			return;
		}
		Position pos = this.getPosition();
		Size size = this.getSize();
		switch (this.moveType) {
		case HORIZONTAL:
			this.texture.drawSubArea((int) (pos.x + this.progress), pos.y, size.width, size.height, 0, 0, 1, 1);
			break;
		case HORIZONTAL_INVERTED:
			this.texture.drawSubArea((int) (pos.x - this.progress), pos.y, size.width, size.height, 0, 0, 1, 1);
			break;
		case VERTICAL:
			this.texture.drawSubArea(pos.x, (int) (pos.y + this.progress), size.width, size.height, 0, 0, 1, 1);
			break;
		case VERTICAL_INVERTED:
			this.texture.drawSubArea(pos.x, (int) (pos.y - this.progress), size.width, size.height, 0, 0, 1, 1);
			break;
		default:
			break;
		}
	}

	@Override
	public void detectAndSendChanges() {
		int actual = this.supplier.getAsInt();
		if (actual != this.progress) {
			this.progress = actual;
			this.writeUpdateInfo(0, (buffer) -> buffer.writeInt(actual));
		}
	}

	@Override
	public void readUpdateInfo(int id, PacketBuffer buffer) {
		if (id == 0) {
			this.progress = buffer.readInt();
		}
	}
}