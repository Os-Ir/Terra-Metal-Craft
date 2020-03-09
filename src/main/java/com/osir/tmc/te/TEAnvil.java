package com.osir.tmc.te;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import com.osir.tmc.api.capability.CapabilityList;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.gui.PlanUIHolder;
import com.osir.tmc.api.gui.PlanUIProvider;
import com.osir.tmc.api.gui.SimpleUIHolder;
import com.osir.tmc.api.gui.TextureHelper;
import com.osir.tmc.api.gui.factory.CapabilitySyncedUIFactory;
import com.osir.tmc.api.gui.factory.PlanUIFactory;
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
import gregtech.api.util.Position;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.ItemStackHandler;

public class TEAnvil extends TileEntity implements ITickable, SimpleUIHolder, PlanUIProvider {
	public static final float COOLING_RATE = 0.02F;

	public static final TextureArea BACKGROUND = TextureHelper.fullImage("textures/gui/anvil/background.png");
	public static final TextureArea BUTTON_WELD = TextureHelper.fullImage("textures/gui/anvil/button_weld.png");
	public static final TextureArea BUTTON_TWINE = TextureHelper.fullImage("textures/gui/anvil/button_twine.png");
	public static final TextureArea BUTTON_BEND = TextureHelper.fullImage("textures/gui/anvil/button_bend.png");
	public static final TextureArea BUTTON_PLAN = TextureHelper.fullImage("textures/gui/anvil/button_plan.png");

	public static final RenderItem ITEM_RENDERER = Minecraft.getMinecraft().getRenderItem();

	protected ItemStackHandler inventory;
	protected int level, plan;

	public TEAnvil() {
		this(0);
	}

	public TEAnvil(int level) {
		this.level = level;
		this.plan = -1;
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

	public void onTwine(ClickData data) {
		if (!this.inventory.getStackInSlot(0).isEmpty()) {
			if (!this.inventory.getStackInSlot(0).hasCapability(CapabilityList.HEATABLE, null)) {
				return;
			}
			if (!this.inventory.getStackInSlot(0).getCapability(CapabilityList.HEATABLE, null).isWorkable()) {
				return;
			}
		}
		List<Recipe> recipes = ModRecipeMap.MAP_ANVIL.getRecipeList().stream()
				.filter((recipe) -> ((ScalableRecipe) recipe).getValue("type") == AnvilRecipeType.TWINE)
				.filter((recipe) -> recipe.matches(false, Arrays.asList(this.inventory.getStackInSlot(0)),
						new ArrayList<FluidStack>()))
				.collect(Collectors.toList());
		if (recipes.isEmpty()) {
			System.out.println(ModRecipeMap.MAP_ANVIL.getRecipeList().size());
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
		recipe.matches(true, Arrays.asList(this.inventory.getStackInSlot(0)), new ArrayList<FluidStack>());
		for (int i = 0; i < output.size(); i++) {
			for (int j = 4; i < 8; j++) {
				if (this.inventory.getStackInSlot(j).isEmpty()) {
					this.inventory.setStackInSlot(j, output.get(i).copy());
					break;
				}
			}
		}
	}

	public void onBend(ClickData data) {
		if (!this.inventory.getStackInSlot(3).isEmpty()) {
			if (!this.inventory.getStackInSlot(3).hasCapability(CapabilityList.HEATABLE, null)) {
				return;
			}
			if (!this.inventory.getStackInSlot(3).getCapability(CapabilityList.HEATABLE, null).isWorkable()) {
				return;
			}
		}
		List<Recipe> recipes = ModRecipeMap.MAP_ANVIL.getRecipeList().stream()
				.filter((recipe) -> ((ScalableRecipe) recipe).getValue("type") == AnvilRecipeType.BEND)
				.filter((recipe) -> recipe.matches(false, Arrays.asList(this.inventory.getStackInSlot(3)),
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
		recipe.matches(true, Arrays.asList(this.inventory.getStackInSlot(3)), new ArrayList<FluidStack>());
		for (int i = 0; i < output.size(); i++) {
			for (int j = 4; i < 8; j++) {
				if (this.inventory.getStackInSlot(j).isEmpty()) {
					this.inventory.setStackInSlot(j, output.get(i).copy());
					break;
				}
			}
		}
	}

	public List<Recipe> findWorkRecipes() {
		if (!this.inventory.getStackInSlot(1).isEmpty()) {
			if (!this.inventory.getStackInSlot(1).hasCapability(CapabilityList.HEATABLE, null)) {
				return new ArrayList<Recipe>();
			}
			if (!this.inventory.getStackInSlot(1).getCapability(CapabilityList.HEATABLE, null).isWorkable()) {
				return new ArrayList<Recipe>();
			}
		}
		if (!this.inventory.getStackInSlot(2).isEmpty()) {
			if (!this.inventory.getStackInSlot(2).hasCapability(CapabilityList.HEATABLE, null)) {
				return new ArrayList<Recipe>();
			}
			if (!this.inventory.getStackInSlot(2).getCapability(CapabilityList.HEATABLE, null).isWorkable()) {
				return new ArrayList<Recipe>();
			}
		}
		return ModRecipeMap.MAP_ANVIL.getRecipeList().stream()
				.filter((recipe) -> ((ScalableRecipe) recipe).getValue("type") == AnvilRecipeType.WORK)
				.filter((recipe) -> recipe.matches(false,
						Arrays.asList(this.inventory.getStackInSlot(1), this.inventory.getStackInSlot(2)),
						new ArrayList<FluidStack>()))
				.collect(Collectors.toList());
	}

	public void openPlanUI(ClickData data, EntityPlayer player) {
		PlanUIFactory.INSTANCE.openUI(this.createHolder(), (EntityPlayerMP) player);
	}

	public void receivePlan(int id) {
		List<Recipe> recipes = this.findWorkRecipes();
		if (recipes.size() <= id) {
			this.plan = -1;
		} else {
			this.plan = id;
		}
	}

	public void onWorkSlotChanged() {
		this.plan = -1;
	}

	public void onPlanCallback(EntityPlayer player) {
		CapabilitySyncedUIFactory.INSTANCE.openSyncedUI((SimpleUIHolder) this.world.getTileEntity(this.pos),
				(EntityPlayerMP) player);
	}

	public void renderPlanButton(Position pos, int id) {

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
				.widget(new SlotWidget(this.inventory, 0, 8, 8).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 1, 30, 8).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 2, 52, 8).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 3, 74, 8).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 4, 8, 52).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 5, 30, 52).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 6, 52, 52).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 7, 74, 52).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new ClickButtonWidget(31, 31, 38, 16, "", this::onWeld).setButtonTexture(BUTTON_WELD))
				.widget(new ClickButtonWidget(9, 31, 16, 16, "", this::onTwine).setButtonTexture(BUTTON_TWINE))
				.widget(new ClickButtonWidget(75, 31, 16, 16, "", this::onBend).setButtonTexture(BUTTON_BEND))
				.widget(new ClickButtonWidget(168, 45, 16, 16, "", (data) -> this.openPlanUI(data, player))
						.setButtonTexture(BUTTON_PLAN))
				.bindPlayerInventory(player.inventory, GuiTextures.SLOT, 24, 118).build(this, player);
	}

	@Override
	public PlanUIHolder createHolder() {
		return new PlanUIHolder(this, this.findWorkRecipes().size(), this::onPlanCallback, this::receivePlan,
				this::renderPlanButton);
	}
}