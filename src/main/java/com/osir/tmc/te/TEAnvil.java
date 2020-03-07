package com.osir.tmc.te;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.osir.tmc.api.capability.CapabilityList;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.gui.SimpleUIHolder;
import com.osir.tmc.api.gui.TextureHelper;
import com.osir.tmc.api.recipe.AnvilRecipeType;
import com.osir.tmc.api.recipe.ModRecipeMap;
import com.osir.tmc.api.recipe.ScalableRecipe;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.gui.widgets.ClickButtonWidget;
import gregtech.api.gui.widgets.ClickButtonWidget.ClickData;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.recipes.Recipe;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;

public class TEAnvil extends TileEntity implements ITickable, SimpleUIHolder {
	public static final float COOLING_RATE = 0.02F;

	public static final TextureArea BACKGROUND = TextureHelper.fullImage("textures/gui/anvil/background.png");
	public static final TextureArea BUTTON_WELD = TextureHelper.fullImage("textures/gui/anvil/button_weld.png");

	protected ItemStackHandler inventory;
	protected int level;

	public TEAnvil() {
		this(0);
	}

	public TEAnvil(int level) {
		this.level = level;
		this.inventory = new ItemStackHandler(10);
	}

	@Override
	public void update() {
		for (int i = 0; i < 8; i++) {
			ItemStack stack = this.inventory.getStackInSlot(i);
			if (!stack.hasCapability(CapabilityList.HEATABLE, null)) {
				continue;
			}
			IHeatable cap = stack.getCapability(CapabilityList.HEATABLE, null);
			float exchange = (cap.getTemp() - 20) * 0.02F;
			exchange = Math.max(exchange, 5);
			cap.increaseEnergy(-exchange);
		}
	}

	public void onWeld(ClickData data) {
		if (!this.inventory.getStackInSlot(1).isEmpty()) {
			if (!this.inventory.getStackInSlot(1).hasCapability(CapabilityList.HEATABLE, null)) {
				return;
			}
			if (!this.inventory.getStackInSlot(1).getCapability(CapabilityList.HEATABLE, null).isWeldable()) {
				return;
			}
		}
		if (!this.inventory.getStackInSlot(2).isEmpty()) {
			if (!this.inventory.getStackInSlot(2).hasCapability(CapabilityList.HEATABLE, null)) {
				return;
			}
			if (!this.inventory.getStackInSlot(2).getCapability(CapabilityList.HEATABLE, null).isWeldable()) {
				return;
			}
		}
		List<Recipe> recipes = ModRecipeMap.MAP_ANVIL.getRecipeList().stream()
				.filter((recipe) -> ((ScalableRecipe) recipe).getValue("type") == AnvilRecipeType.WELD)
				.filter((recipe) -> recipe.matches(false,
						Arrays.asList(this.inventory.getStackInSlot(1), this.inventory.getStackInSlot(2)),
						new ArrayList<FluidStack>()))
				.collect(Collectors.toList());
		if (recipes.isEmpty()) {
			return;
		}
		ScalableRecipe recipe = (ScalableRecipe) recipes.get(0);
		List<ItemStack> output = recipe.getOutputs();
		int surplusSlot = 0;
		for (int i = 4; i < 8; i++) {
			if (this.inventory.getStackInSlot(i).isEmpty()) {
				surplusSlot++;
			}
		}
		if (surplusSlot < output.size()) {
			return;
		}
		recipe.matches(true, Arrays.asList(this.inventory.getStackInSlot(1), this.inventory.getStackInSlot(2)),
				new ArrayList<FluidStack>());
		for (int i = 0; i < output.size(); i++) {
			for (int j = 4; i < 8; j++) {
				if (this.inventory.getStackInSlot(j).isEmpty()) {
					this.inventory.setStackInSlot(j, output.get(i).copy());
					break;
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		this.inventory.deserializeNBT((NBTTagCompound) nbt.getTag("inventory"));
		super.readFromNBT(nbt);

	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("inventory", this.inventory.serializeNBT());
		return super.writeToNBT(nbt);
	}

	@Override
	public boolean isValid() {
		return !super.isInvalid();
	}

	@Override
	public boolean isRemote() {
		return this.world.isRemote;
	}

	@Override
	public void markAsDirty() {
		this.markDirty();
	}

	@Override
	public ModularUI createUI(EntityPlayer player) {
		return ModularUI.builder(BACKGROUND, 208, 200)
				.widget(new SlotWidget(this.inventory, 1, 30, 8).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 2, 52, 8).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 4, 8, 52).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 5, 30, 52).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 6, 52, 52).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 7, 74, 52).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new ClickButtonWidget(31, 31, 38, 16, "", this::onWeld).setButtonTexture(BUTTON_WELD))
				.bindPlayerInventory(player.inventory, GuiTextures.SLOT, 24, 118).build(this, player);
	}
}