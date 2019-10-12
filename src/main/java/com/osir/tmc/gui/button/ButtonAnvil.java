package com.osir.tmc.gui.button;

import com.osir.tmc.gui.GuiAnvil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;

public class ButtonAnvil extends GuiButton {
	public int tx, ty;

	public ButtonAnvil(int id, int x, int y, int tx, int ty, int width, int height, String txt) {
		super(id, x, y, width, height, txt);
		this.tx = tx;
		this.ty = ty;
	}

	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
		if (this.visible) {
			GlStateManager.color(1, 1, 1, 1);
			mc.getTextureManager().bindTexture(GuiAnvil.TEXTURE);
			this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
					&& mouseY < this.y + this.height;
			Gui.drawModalRectWithCustomSizedTexture(this.x, this.y, this.tx, this.ty, this.width, this.height, 256,
					256);
			this.mouseDragged(mc, mouseX, mouseY);
		}
	}
}