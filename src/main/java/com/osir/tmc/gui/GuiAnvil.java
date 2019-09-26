package com.osir.tmc.gui;

import com.osir.tmc.Main;
import com.osir.tmc.container.ContainerAnvil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GuiAnvil extends GuiContainer {
	private static final ResourceLocation TEXTURE = new ResourceLocation(Main.MODID, "textures/gui/gui_anvil.png");
	private ContainerAnvil container;

	public GuiAnvil(ContainerAnvil container) {
		super(container);
		this.container = container;
		this.xSize = 208;
		this.ySize = 200;
	}

	@Override
	public void initGui() {
		super.initGui();
		this.buttonList.add(new GuiButton(0, this.guiLeft + 8, this.guiTop + 30, 16, 16, "") {
			@Override
			public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
				if (this.visible) {
					GlStateManager.color(1, 1, 1, 1);
					mc.getTextureManager().bindTexture(TEXTURE);
					this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width
							&& mouseY < this.y + this.height;
					Gui.drawModalRectWithCustomSizedTexture(this.x, this.y, 0, 214, this.width, this.height, 256, 256);
					this.mouseDragged(mc, mouseX, mouseY);
				}
			}
		});
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.drawDefaultBackground();
		super.drawScreen(mouseX, mouseY, partialTicks);
		this.renderHoveredToolTip(mouseX, mouseY);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY) {
		GlStateManager.color(1, 1, 1, 1);
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
	}
}