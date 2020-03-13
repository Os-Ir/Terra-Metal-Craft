package com.osir.tmc.api.gui;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

import com.osir.tmc.api.gui.widget.RenderButtonWidget;

import gregtech.api.gui.ModularUI;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.gui.widgets.ClickButtonWidget;
import gregtech.api.util.Position;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class PlanUIHolder implements SimpleUIHolder {
	public static final TextureArea BACKGROUND = TextureHelper.fullImage("textures/gui/plan/background.png");
	public static final TextureArea BUTTON_BACK = TextureHelper.fullImage("textures/gui/plan/button_back.png");
	public static final TextureArea BUTTON = TextureHelper.fullImage("textures/gui/plan/button.png");

	protected TileEntity te;
	protected int size;
	protected Consumer<EntityPlayer> callback;
	protected Consumer<Integer> recipient;
	protected BiConsumer<Position, Integer> renderer;

	public PlanUIHolder(TileEntity te, int size, Consumer<EntityPlayer> callback, Consumer<Integer> recipient,
			BiConsumer<Position, Integer> renderer) {
		if (!(te instanceof PlanUIProvider)) {
			throw new IllegalStateException("This TileEntity is not a PlanUIProvider");
		}
		this.te = te;
		this.size = size;
		this.callback = callback;
		this.recipient = recipient;
		this.renderer = renderer;
	}

	public BlockPos getPos() {
		return this.te.getPos();
	}

	@Override
	public boolean isValid() {
		return !this.te.isInvalid();
	}

	@Override
	public boolean isRemote() {
		return this.te.getWorld().isRemote;
	}

	@Override
	public void markAsDirty() {
		this.te.markDirty();
	}

	@Override
	public ModularUI createUI(EntityPlayer player) {
		ModularUI.Builder builder = ModularUI.builder(BACKGROUND, 168, 114)
				.widget(new ClickButtonWidget(3, 3, 18, 18, "", (data) -> this.callback.accept(player))
						.setButtonTexture(BUTTON_BACK));
		for (int i = 1; i <= this.size; i++) {
			int x = i % 9;
			int y = i / 9;
			builder.widget(
					new RenderButtonWidget(i, x * 18 + 3, y * 18 + 3, 18, 18, "", this.renderer::accept, (data, id) -> {
						this.recipient.accept(id - 1);
						this.callback.accept(player);
					}).setButtonTexture(BUTTON));
		}
		return builder.build(this, player);
	}
}