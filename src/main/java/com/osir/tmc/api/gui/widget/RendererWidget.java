package com.osir.tmc.api.gui.widget;

import java.util.function.BiConsumer;

import gregtech.api.gui.IRenderContext;
import gregtech.api.gui.Widget;
import gregtech.api.util.Position;
import gregtech.api.util.Size;

public class RendererWidget extends Widget {
	protected int id;
	protected BiConsumer<Position, Integer> renderer;

	public RendererWidget(int id, int xPosition, int yPosition, int width, int height,
			BiConsumer<Position, Integer> renderer) {
		super(new Position(xPosition, yPosition), new Size(width, height));
		this.id = id;
		this.renderer = renderer;
	}

	@Override
	public void drawInBackground(int mouseX, int mouseY, IRenderContext context) {
		super.drawInBackground(mouseX, mouseY, context);
		Position position = this.getPosition();
		if (this.renderer != null) {
			this.renderer.accept(position, this.id);
		}
	}
}