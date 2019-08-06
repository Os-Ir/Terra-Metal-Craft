package com.osir.tmc.gui;

import com.osir.tmc.Main;
import com.osir.tmc.container.ContainerOriginalForge;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;

public class GuiOriginalForge extends GuiContainer {
	private static final ResourceLocation texture = new ResourceLocation(Main.MODID,
			"textures/gui/gui_original_forge.png");
	private ContainerOriginalForge container;

	public GuiOriginalForge(ContainerOriginalForge container) {
		super(container);
		this.container = container;
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
		Minecraft.getMinecraft().renderEngine.bindTexture(texture);
		this.drawTexturedModalRect(this.guiLeft, this.guiTop, 0, 0, this.xSize, this.ySize);
		int height = (int) (container.getBurnTime() / 1600 * 14 + 1);
		this.drawTexturedModalRect(this.guiLeft + 30, this.guiTop + 78 - height, 177, 14 - height, 14, height);
		float temp = container.getTemp();
		int dis = (int) ((temp > 1500 ? 1500 : temp) / 1500 * 75 - 1);
		this.drawTexturedModalRect(this.guiLeft + 48 + dis, this.guiTop + 63, 177, 15, 5, 17);
		this.fontRenderer.drawString(I18n.format("tile.originalForge.name"), this.guiLeft + 5, this.guiTop + 5,
				0x404040);
		this.fontRenderer.drawString(I18n.format("item.heatable.state.temperature", (int) temp), this.guiLeft + 49,
				this.guiTop + 54, 0x404040);
	}
}