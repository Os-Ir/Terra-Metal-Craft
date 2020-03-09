package com.osir.tmc.api.gui.widget;

import java.util.function.BiConsumer;

import gregtech.api.gui.IRenderContext;
import gregtech.api.gui.widgets.ClickButtonWidget;
import gregtech.api.util.Position;

public class RenderButtonWidget extends ClickButtonWidget {
	protected int id;
	protected BiConsumer<Position, Integer> renderer;

	public RenderButtonWidget(int id, int xPosition, int yPosition, int width, int height, String displayText,
			BiConsumer<Position, Integer> renderer, BiConsumer<ClickData, Integer> onPressed) {
		super(xPosition, yPosition, width, height, displayText, (data) -> onPressed.accept(data, id));
		this.id = id;
		this.renderer = renderer;
	}

	@Override
	public void drawInBackground(int mouseX, int mouseY, IRenderContext context) {
		super.drawInBackground(mouseX, mouseY, context);
		Position position = this.getPosition();
		this.renderer.accept(position, this.id);
	}
}