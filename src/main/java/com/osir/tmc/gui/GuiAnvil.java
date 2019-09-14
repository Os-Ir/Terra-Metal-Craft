package com.osir.tmc.gui;

import com.osir.tmc.Main;
import com.osir.tmc.container.ContainerAnvil;
import com.osir.tmc.container.ContainerOriginalForge;

import net.minecraft.client.Minecraft;
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