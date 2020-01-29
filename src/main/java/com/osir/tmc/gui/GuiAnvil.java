package com.osir.tmc.gui;

import java.io.IOException;

import com.osir.tmc.Main;
import com.osir.tmc.container.ContainerAnvil;
import com.osir.tmc.gui.button.ButtonAnvil;
import com.osir.tmc.handler.NetworkHandler;
import com.osir.tmc.network.MessageAnvilButton;
import com.osir.tmc.te.TEAnvil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

public class GuiAnvil extends GuiContainer {
	public static final ResourceLocation TEXTURE = new ResourceLocation(Main.MODID, "textures/gui/gui_anvil.png");
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
		this.buttonList.add(new ButtonAnvil(0, this.guiLeft + 8, this.guiTop + 30, 0, 214, 16, 16, ""));
		this.buttonList.add(new ButtonAnvil(1, this.guiLeft + 30, this.guiTop + 30, 16, 214, 38, 16, ""));
		this.buttonList.add(new ButtonAnvil(2, this.guiLeft + 74, this.guiTop + 30, 54, 214, 16, 16, ""));
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

	@Override
	protected void actionPerformed(GuiButton button) throws IOException {
		if (button instanceof ButtonAnvil) {
			NetworkHandler.NETWORK.sendToServer(new MessageAnvilButton(button.id));
			TEAnvil te = (TEAnvil) this.container.getTE();
			IItemHandlerModifiable cap = (IItemHandlerModifiable) te
					.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null);
		}
		super.actionPerformed(button);
	}
}