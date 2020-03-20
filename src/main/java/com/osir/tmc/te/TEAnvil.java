package com.osir.tmc.te;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.osir.tmc.api.capability.CapabilityList;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.gui.PlanUIHolder;
import com.osir.tmc.api.gui.PlanUIProvider;
import com.osir.tmc.api.gui.SimpleUIHolder;
import com.osir.tmc.api.gui.TextureHelper;
import com.osir.tmc.api.gui.factory.CapabilitySyncedUIFactory;
import com.osir.tmc.api.gui.factory.PlanUIFactory;
import com.osir.tmc.api.gui.widget.PointerWidget;
import com.osir.tmc.api.gui.widget.RenderButtonWidget;
import com.osir.tmc.api.recipe.AnvilRecipeHelper;
import com.osir.tmc.api.recipe.AnvilRecipeType;
import com.osir.tmc.api.recipe.AnvilWorkType;
import com.osir.tmc.api.recipe.ModRecipeMap;
import com.osir.tmc.api.recipe.ScalableRecipe;
import com.osir.tmc.api.util.ItemIndex;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.gui.widgets.ClickButtonWidget;
import gregtech.api.gui.widgets.ClickButtonWidget.ClickData;
import gregtech.api.gui.widgets.ImageWidget;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.metatileentity.SyncedTileEntityBase;
import gregtech.api.recipes.Recipe;
import gregtech.api.util.Position;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TEAnvil extends SyncedTileEntityBase implements ITickable, SimpleUIHolder, PlanUIProvider {
	public static final float COOLING_RATE = 0.02F;

	public static final TextureArea BACKGROUND = TextureHelper.fullImage("textures/gui/anvil/background.png");
	public static final TextureArea BUTTON_WELD = TextureHelper.fullImage("textures/gui/anvil/button_weld.png");
	public static final TextureArea BUTTON_TWINE = TextureHelper.fullImage("textures/gui/anvil/button_twine.png");
	public static final TextureArea BUTTON_BEND = TextureHelper.fullImage("textures/gui/anvil/button_bend.png");
	public static final TextureArea BUTTON_PLAN = TextureHelper.fullImage("textures/gui/anvil/button_plan.png");
	public static final TextureArea BUTTON_PLAN_EMPTY = TextureHelper
			.fullImage("textures/gui/anvil/button_plan_empty.png");
	public static final TextureArea WORK_PROGRESS_BAR = TextureHelper
			.fullImage("textures/gui/anvil/work_progress_bar.png");
	public static final TextureArea WORK_POINTER_TARGET = TextureHelper
			.fullImage("textures/gui/anvil/work_pointer_target.png");
	public static final TextureArea WORK_POINTER_GREEN = TextureHelper
			.fullImage("textures/gui/anvil/work_pointer_green.png");
	public static final TextureArea WORK_BUTTON_GREEN = TextureHelper
			.fullImage("textures/gui/anvil/work_button_green.png");
	public static final TextureArea WORK_BUTTON_RED = TextureHelper.fullImage("textures/gui/anvil/work_button_red.png");
	public static final TextureArea WORK_BUTTON_OVERLAY = TextureHelper
			.fullImage("textures/gui/anvil/work_button_overlay.png");

	public static final RenderItem ITEM_RENDERER = Minecraft.getMinecraft().getRenderItem();

	protected Cache<Pair<ItemIndex, ItemIndex>, List<Recipe>> cacheWorkRecipe;
	protected Cache<Pair<ItemIndex, ItemIndex>, Integer> cacheBlackList;
	protected ItemStackHandler inventory;
	protected int level, plan;

	public TEAnvil() {
		this(0);
	}

	public TEAnvil(int level) {
		this.level = level;
		this.plan = -1;
		this.inventory = new ItemStackHandler(10) {
			@Override
			public void onContentsChanged(int slot) {
				if (slot == 1 || slot == 2) {
					TEAnvil.this.onWorkSlotChanged();
				}
			}
		};
		this.cacheWorkRecipe = CacheBuilder.newBuilder().maximumSize(8).build();
		this.cacheBlackList = CacheBuilder.newBuilder().maximumSize(32).build();
	}

	@Override
	public void update() {
		if (this.world.isRemote) {
			return;
		}
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
		if (this.world != null) {
			this.world.markChunkDirty(this.pos, this);
		}
	}

	public void onWeld(ClickData data) {
		if (!this.inventory.getStackInSlot(1).hasCapability(CapabilityList.HEATABLE, null)) {
			return;
		}
		if (!this.inventory.getStackInSlot(1).getCapability(CapabilityList.HEATABLE, null).isWeldable()) {
			return;
		}
		if (!this.inventory.getStackInSlot(2).hasCapability(CapabilityList.HEATABLE, null)) {
			return;
		}
		if (!this.inventory.getStackInSlot(2).getCapability(CapabilityList.HEATABLE, null).isWeldable()) {
			return;
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
		for (ItemStack result : output) {
			for (int j = 4; j < 8; j++) {
				if (this.inventory.getStackInSlot(j).isEmpty()) {
					this.inventory.setStackInSlot(j, result.copy());
					break;
				}
			}
		}
	}

	public void onTwine(ClickData data) {
		if (!this.inventory.getStackInSlot(0).hasCapability(CapabilityList.HEATABLE, null)) {
			return;
		}
		if (!this.inventory.getStackInSlot(0).getCapability(CapabilityList.HEATABLE, null).isWorkable()) {
			return;
		}
		List<Recipe> recipes = ModRecipeMap.MAP_ANVIL.getRecipeList().stream()
				.filter((recipe) -> ((ScalableRecipe) recipe).getValue("type") == AnvilRecipeType.TWINE)
				.filter((recipe) -> recipe.matches(false, Arrays.asList(this.inventory.getStackInSlot(0)),
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
		recipe.matches(true, Arrays.asList(this.inventory.getStackInSlot(0)), new ArrayList<FluidStack>());
		for (ItemStack result : output) {
			for (int j = 4; j < 8; j++) {
				if (this.inventory.getStackInSlot(j).isEmpty()) {
					this.inventory.setStackInSlot(j, result.copy());
					break;
				}
			}
		}
	}

	public void onBend(ClickData data) {
		if (!this.inventory.getStackInSlot(3).hasCapability(CapabilityList.HEATABLE, null)) {
			return;
		}
		if (!this.inventory.getStackInSlot(3).getCapability(CapabilityList.HEATABLE, null).isWorkable()) {
			return;
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
		for (ItemStack result : output) {
			for (int j = 4; j < 8; j++) {
				if (this.inventory.getStackInSlot(j).isEmpty()) {
					this.inventory.setStackInSlot(j, result.copy());
					break;
				}
			}
		}
	}

	public Pair<ItemIndex, ItemIndex> getRecipeKey() {
		ItemIndex indexA = new ItemIndex(this.inventory.getStackInSlot(1));
		ItemIndex indexB = new ItemIndex(this.inventory.getStackInSlot(2));
		if (indexA.hashCode() < indexB.hashCode()) {
			ItemIndex tmp = indexB;
			indexB = indexA;
			indexA = tmp;
		}
		return Pair.of(indexA, indexB);
	}

	public List<Recipe> findWorkRecipes() {
		return ModRecipeMap.MAP_ANVIL_WORK.getRecipeList().stream()
				.filter((recipe) -> recipe.matches(false,
						Arrays.asList(this.inventory.getStackInSlot(1), this.inventory.getStackInSlot(2)),
						new ArrayList<FluidStack>()))
				.collect(Collectors.toList());
	}

	public List<Recipe> getWorkRecipes() {
		Pair<ItemIndex, ItemIndex> key = this.getRecipeKey();
		if (!this.cacheBlackList.asMap().containsKey(key) && !this.cacheWorkRecipe.asMap().containsKey(key)) {
			List<Recipe> recipes = this.findWorkRecipes();
			if (recipes.isEmpty()) {
				this.cacheBlackList.put(key, (int) this.cacheWorkRecipe.size());
			} else {
				this.cacheWorkRecipe.asMap().putIfAbsent(key, recipes);
			}
			return recipes;
		}
		return this.cacheWorkRecipe.asMap().containsKey(key) ? this.cacheWorkRecipe.getIfPresent(key)
				: new ArrayList<Recipe>();
	}

	public void openPlanUI(ClickData data, EntityPlayer player) {
		PlanUIFactory.INSTANCE.openUI(this.createHolder(), (EntityPlayerMP) player);
	}

	public void receivePlan(EntityPlayer player, int id) {
		List<Recipe> recipes = this.getWorkRecipes();
		if (recipes.size() <= id) {
			this.setPlan(-1);
		} else {
			this.onWork(AnvilWorkType.NONE, player);
			this.setPlan(id);
		}
	}

	public void onWorkSlotChanged() {
		if (!this.world.isRemote) {
			this.setPlan(-1);
		}
	}

	public void setPlan(int plan) {
		this.plan = plan;
		this.writeCustomData(100, (buf) -> buf.writeInt(plan));
		this.world.markChunkDirty(this.pos, this);
	}

	public void onPlanCallback(EntityPlayer player) {
		CapabilitySyncedUIFactory.INSTANCE.openSyncedUI((SimpleUIHolder) this.world.getTileEntity(this.pos),
				(EntityPlayerMP) player);
	}

	public void renderPlanWindowButton(Position pos, int id) {
		List<Recipe> recipes = this.getWorkRecipes();
		id--;
		if (id < recipes.size()) {
			ItemStack output = recipes.get(id).getOutputs().get(0);
			ITEM_RENDERER.renderItemAndEffectIntoGUI(output, pos.x + 1, pos.y + 1);
		}
	}

	public void renderPlanButton(Position pos, int id) {
		List<Recipe> recipes = this.getWorkRecipes();
		if (this.plan >= 0 && this.plan < recipes.size()) {
			ItemStack output = recipes.get(this.plan).getOutputs().get(0);
			if (!output.isEmpty()) {
				ITEM_RENDERER.renderItemAndEffectIntoGUI(output, pos.x + 1, pos.y + 1);
			}
		} else {
			BUTTON_PLAN_EMPTY.draw(pos.x + 1, pos.y + 1, 16, 16);
		}
	}

	public void renderWorkButton(Position pos, int id) {
		if (id <= 4) {
			WORK_BUTTON_OVERLAY.drawSubArea(pos.x, pos.y, 16, 16, 0.25 * id - 0.25, 0, 0.25, 0.5);
		} else {
			WORK_BUTTON_OVERLAY.drawSubArea(pos.x, pos.y, 16, 16, 0.25 * id - 1.25, 0.5, 0.25, 0.5);
		}
	}

	public int getTargetPointer() {
		List<Recipe> recipes = this.getWorkRecipes();
		if (this.plan >= 0 && this.plan < recipes.size()) {
			return AnvilRecipeHelper.getProgress((int) ((ScalableRecipe) recipes.get(this.plan)).getValue("info"));
		}
		return 0;
	}

	public int getProgressPointer() {
		int progressA = 0, progressB = 0;
		ItemStack stackA = this.inventory.getStackInSlot(1);
		ItemStack stackB = this.inventory.getStackInSlot(2);
		boolean flagA = stackA.hasCapability(CapabilityList.WORKABLE, null);
		boolean flagB = stackB.hasCapability(CapabilityList.WORKABLE, null);
		if (flagA) {
			progressA = stackA.getCapability(CapabilityList.WORKABLE, null).getWorkProgress();
		}
		if (flagB) {
			progressB = stackB.getCapability(CapabilityList.WORKABLE, null).getWorkProgress();
		}
		if (flagA) {
			if (flagB) {
				return progressA == progressB ? progressA : 0;
			} else {
				return progressA;
			}
		} else if (flagB) {
			return progressB;
		}
		return 0;
	}

	public void onWork(AnvilWorkType type, EntityPlayer player) {
		ItemStack stackA = this.inventory.getStackInSlot(1);
		ItemStack stackB = this.inventory.getStackInSlot(2);
		boolean flagA = stackA.hasCapability(CapabilityList.WORKABLE, null);
		boolean flagB = stackB.hasCapability(CapabilityList.WORKABLE, null);
		if (flagA && flagB && stackA.getCapability(CapabilityList.WORKABLE, null).getWorkProgress() != stackB
				.getCapability(CapabilityList.WORKABLE, null).getWorkProgress()) {
			return;
		}
		int progress = 0;
		if (flagA) {
			stackA.getCapability(CapabilityList.WORKABLE, null).addWorkProgress(type.getProgress());
			progress = stackA.getCapability(CapabilityList.WORKABLE, null).getWorkProgress();
		}
		if (flagB) {
			stackB.getCapability(CapabilityList.WORKABLE, null).addWorkProgress(type.getProgress());
			progress = stackB.getCapability(CapabilityList.WORKABLE, null).getWorkProgress();
		}
		List<Recipe> recipes = this.getWorkRecipes();
		if (this.plan != -1 && progress == this.getTargetPointer()) {
			if (recipes.get(this.plan).matches(true, Arrays.asList(stackA, stackB), new ArrayList<FluidStack>())) {
				List<ItemStack> output = recipes.get(this.plan).getOutputs();
				for (ItemStack result : output) {
					boolean putted = false;
					ItemStack copy = result.copy();
					for (int j = 4; j < 8; j++) {
						if (this.inventory.getStackInSlot(j).isEmpty()) {
							this.inventory.setStackInSlot(j, copy);
							putted = true;
							break;
						}
					}
					if (!putted) {
						player.addItemStackToInventory(copy);
						if (!copy.isEmpty()) {
							player.dropItem(copy, false, false);
						}
					}
				}
			}
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("inventory")) {
			this.inventory.deserializeNBT((NBTTagCompound) nbt.getTag("inventory"));
		}
		if (nbt.hasKey("plan")) {
			this.plan = nbt.getInteger("plan");
		}
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setTag("inventory", this.inventory.serializeNBT());
		nbt.setInteger("plan", this.plan);
		return super.writeToNBT(nbt);
	}

	@Override
	public void writeInitialSyncData(PacketBuffer buf) {
		buf.writeInt(this.plan);
	}

	@Override
	public void receiveInitialSyncData(PacketBuffer buf) {
		this.plan = buf.readInt();
	}

	@Override
	public void receiveCustomData(int discriminator, PacketBuffer buf) {
		if (discriminator == 100) {
			this.plan = buf.readInt();
		}
	}

	@Override
	public boolean hasCapability(Capability capability, EnumFacing facing) {
		return super.hasCapability(capability, facing) || capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (super.hasCapability(capability, facing)) {
			return super.getCapability(capability, facing);
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) this.inventory;
		}
		return null;
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
				.widget(new SlotWidget(this.inventory, 0, 7, 7).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 1, 29, 7).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 2, 51, 7).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 1, 123, 44).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 2, 145, 44).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 3, 73, 7).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 4, 7, 51).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 5, 29, 51).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 6, 51, 51).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 7, 73, 51).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new ClickButtonWidget(30, 30, 38, 16, "", this::onWeld).setButtonTexture(BUTTON_WELD))
				.widget(new ClickButtonWidget(8, 30, 16, 16, "", this::onTwine).setButtonTexture(BUTTON_TWINE))
				.widget(new ClickButtonWidget(74, 30, 16, 16, "", this::onBend).setButtonTexture(BUTTON_BEND))
				.widget(new RenderButtonWidget(0, 167, 44, 18, 18, "", this::renderPlanButton,
						(data, id) -> this.openPlanUI(data, player)).setButtonTexture(BUTTON_PLAN))
				.widget(new ImageWidget(29, 103, 153, 7, WORK_PROGRESS_BAR))
				.widget(new PointerWidget(this::getTargetPointer, 28, 99, 5, 4).setPointer(WORK_POINTER_TARGET,
						PointerWidget.MoveType.HORIZONTAL))
				.widget(new PointerWidget(this::getProgressPointer, 28, 110, 5, 4).setPointer(WORK_POINTER_GREEN,
						PointerWidget.MoveType.HORIZONTAL))
				.widget(new RenderButtonWidget(1, 119, 64, 16, 16, "", this::renderWorkButton,
						(data, id) -> this.onWork(AnvilWorkType.LIGTH_HIT, player)).setButtonTexture(WORK_BUTTON_RED))
				.widget(new RenderButtonWidget(2, 137, 64, 16, 16, "", this::renderWorkButton,
						(data, id) -> this.onWork(AnvilWorkType.MEDIUM_HIT, player)).setButtonTexture(WORK_BUTTON_RED))
				.widget(new RenderButtonWidget(3, 119, 82, 16, 16, "", this::renderWorkButton,
						(data, id) -> this.onWork(AnvilWorkType.HEAVY_HIT, player)).setButtonTexture(WORK_BUTTON_RED))
				.widget(new RenderButtonWidget(4, 137, 82, 16, 16, "", this::renderWorkButton,
						(data, id) -> this.onWork(AnvilWorkType.DRAW, player)).setButtonTexture(WORK_BUTTON_RED))
				.widget(new RenderButtonWidget(5, 155, 64, 16, 16, "", this::renderWorkButton,
						(data, id) -> this.onWork(AnvilWorkType.PUNCH, player)).setButtonTexture(WORK_BUTTON_GREEN))
				.widget(new RenderButtonWidget(6, 173, 64, 16, 16, "", this::renderWorkButton,
						(data, id) -> this.onWork(AnvilWorkType.BEND, player)).setButtonTexture(WORK_BUTTON_GREEN))
				.widget(new RenderButtonWidget(7, 155, 82, 16, 16, "", this::renderWorkButton,
						(data, id) -> this.onWork(AnvilWorkType.UPSET, player)).setButtonTexture(WORK_BUTTON_GREEN))
				.widget(new RenderButtonWidget(8, 173, 82, 16, 16, "", this::renderWorkButton,
						(data, id) -> this.onWork(AnvilWorkType.SHRINK, player)).setButtonTexture(WORK_BUTTON_GREEN))
				.bindPlayerInventory(player.inventory, GuiTextures.SLOT, 23, 117).build(this, player);
	}

	@Override
	public PlanUIHolder createHolder() {
		return new PlanUIHolder(this, this.getWorkRecipes().size(), this::onPlanCallback, this::receivePlan,
				this::renderPlanWindowButton);
	}
}