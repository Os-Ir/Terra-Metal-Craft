package com.osir.tmc.te;

import java.util.ArrayList;
import java.util.Arrays;

import com.osir.tmc.api.capability.CapabilityHeat;
import com.osir.tmc.api.capability.CapabilityList;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.gui.SimpleUIHolder;
import com.osir.tmc.api.gui.TextureHelper;
import com.osir.tmc.api.gui.widget.PointerWidget;
import com.osir.tmc.api.gui.widget.UpdatableTextWidget;
import com.osir.tmc.api.heat.HeatMaterialList;
import com.osir.tmc.api.recipe.ModRecipeMap;
import com.osir.tmc.api.recipe.ScalableRecipe;
import com.osir.tmc.block.BlockOriginalForge;
import com.osir.tmc.handler.BlockHandler;

import gregtech.api.gui.GuiTextures;
import gregtech.api.gui.ModularUI;
import gregtech.api.gui.resources.TextureArea;
import gregtech.api.gui.widgets.ImageWidget;
import gregtech.api.gui.widgets.ProgressWidget;
import gregtech.api.gui.widgets.SlotWidget;
import gregtech.api.metatileentity.SyncedTileEntityBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class TEOriginalForge extends SyncedTileEntityBase implements ITickable, SimpleUIHolder {
	public static final int COAL_BURN_TIME = 1600;
	public static final int POWER = 200;
	public static final float RATE = 0.5F;
	public static final float COOLING_RATE = 0.05F;
	public static final int MAX_TEMP = 900;

	public static final TextureArea OVERLAY_INGOT = TextureHelper.fullImage("textures/gui/heat/overlay_ingot.png");
	public static final TextureArea OVERLAY_COAL = TextureHelper.fullImage("textures/gui/heat/overlay_coal.png");
	public static final TextureArea FUEL = TextureHelper.fullImage("textures/gui/heat/fuel.png");
	public static final TextureArea FUEL_FULL = TextureHelper.fullImage("textures/gui/heat/fuel_full.png");
	public static final TextureArea TEMPERATURE_PROGRESS = TextureHelper
			.fullImage("textures/gui/heat/temperature_progress.png");
	public static final TextureArea POINTER = TextureHelper.fullImage("textures/gui/heat/pointer.png");

	protected ItemStackHandler inventory;
	protected CapabilityHeat cap;
	protected int burnTime, maxTemp;
	protected boolean burning;

	public TEOriginalForge() {
		this.inventory = new ItemStackHandler(9) {
			@Override
			public int getSlotLimit(int slot) {
				if (slot > 0 && slot < 6) {
					return 1;
				}
				return 64;
			}
		};
		this.cap = new CapabilityHeat(HeatMaterialList.GLASS, 500);
		this.maxTemp = MAX_TEMP;
	}

	@Override
	public void update() {
		if (this.world.isRemote) {
			return;
		}
		this.burnTime = Math.max(this.burnTime, 0);
		if (this.burnTime == 0) {
			this.consumeCoal();
		}
		this.updateBuring(this.burnTime > 0 || this.cap.getTemp() >= 500);
		this.cooling();
		if (this.burnTime > 0) {
			this.burnTime--;
			this.increaseHeat(POWER);
		}
		for (int i = 3; i < 6; i++) {
			ItemStack stack = this.inventory.getStackInSlot(i);
			if (stack.hasCapability(CapabilityList.HEATABLE, null)) {
				IHeatable heat = stack.getCapability(CapabilityList.HEATABLE, null);
				this.heatExchange(heat);
				stack = this.getRecipeResult(stack);
				this.inventory.setStackInSlot(i, stack);
			}
		}
		this.updateBlockState();
		if (this.world != null) {
			this.world.markChunkDirty(this.pos, this);
		}
	}

	public ItemStack getRecipeResult(ItemStack stack) {
		if (!stack.hasCapability(CapabilityList.HEATABLE, null)) {
			return stack;
		}
		IHeatable cap = stack.getCapability(CapabilityList.HEATABLE, null);
		if (cap.getProgress() < 1) {
			return stack;
		}
		ScalableRecipe recipe = (ScalableRecipe) ModRecipeMap.MAP_HEAT.findRecipe(1, Arrays.asList(stack),
				new ArrayList<FluidStack>(), 0);
		if (recipe.getOutputs().isEmpty()) {
			return ItemStack.EMPTY;
		}
		ItemStack output = recipe.getOutputs().get(0);
		if (output.isEmpty()) {
			return ItemStack.EMPTY;
		}
		ItemStack result = output.copy();
		return result;
	}

	public void cooling() {
		float exchange = (this.cap.getTemp() - 20) * COOLING_RATE;
		exchange = Math.max(exchange, 5);
		this.increaseHeat(-exchange);
	}

	public boolean consumeCoal() {
		ItemStack coal = this.inventory.getStackInSlot(0);
		if (coal.getItem() == Items.COAL) {
			coal.shrink(1);
			this.burnTime += COAL_BURN_TIME;
			return true;
		}
		return false;
	}

	public void updateBlockState() {
		IBlockState state = this.getBlockState();
		TileEntity te = this.world.getTileEntity(this.pos);
		this.world.setBlockState(this.pos,
				BlockHandler.ORIGINAL_FORGE.getDefaultState()
						.withProperty(BlockOriginalForge.FACING, state.getValue(BlockOriginalForge.FACING))
						.withProperty(BlockOriginalForge.BURN, this.burning));
		if (te != null) {
			te.validate();
			this.world.setTileEntity(this.pos, te);
		}
	}

	public void increaseHeat(float energy) {
		float maxEnergy = this.cap.getMaterial().getSpecificHeat() * this.cap.getUnit() * (this.maxTemp - 20);
		if (maxEnergy < this.cap.getEnergy() + energy) {
			this.cap.setEnergy(maxEnergy);
		} else {
			this.cap.increaseEnergy(energy);
		}
	}

	public void heatExchange(IHeatable cap) {
		if (cap == null) {
			return;
		}
		float exchange = (this.cap.getTemp() - cap.getTemp()) * RATE;
		this.increaseHeat(-exchange);
		cap.increaseEnergy(exchange);
	}

	public double getBurnProgress() {
		return ((double) this.burnTime) / COAL_BURN_TIME;
	}

	public int getTemperaturePointer() {
		return Math.min(this.cap.getTemp(), 1500) / 20;
	}

	public String getTemperatureString() {
		return this.cap.getTemp() + "\u2103";
	}

	public boolean isBurning() {
		return this.burning;
	}

	public void updateBuring(boolean update) {
		if (this.burning ^ update) {
			this.burning = update;
			this.writeCustomData(100, (buf) -> buf.writeBoolean(update));
		}
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		if (nbt.hasKey("burnTime")) {
			this.burnTime = nbt.getInteger("burnTime");
		}
		if (nbt.hasKey("capability")) {
			this.cap.deserializeNBT(nbt.getCompoundTag("capability"));
		}
		if (nbt.hasKey("inventory")) {
			this.inventory.deserializeNBT((NBTTagCompound) nbt.getTag("inventory"));
		}
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setFloat("burnTime", this.burnTime);
		nbt.setTag("capability", this.cap.serializeNBT());
		nbt.setTag("inventory", this.inventory.serializeNBT());
		return super.writeToNBT(nbt);
	}

	@Override
	public void writeInitialSyncData(PacketBuffer buf) {
		buf.writeBoolean(this.burning);
	}

	@Override
	public void receiveInitialSyncData(PacketBuffer buf) {
		this.burning = buf.readBoolean();
	}

	@Override
	public void receiveCustomData(int discriminator, PacketBuffer buf) {
		if (discriminator == 100) {
			this.burning = buf.readBoolean();
			this.scheduleChunkForRenderUpdate();
		}
	}

	@Override
	public boolean hasCapability(Capability capability, EnumFacing facing) {
		return super.hasCapability(capability, facing) || capability == CapabilityList.HEATABLE
				|| capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		if (super.hasCapability(capability, facing)) {
			return super.getCapability(capability, facing);
		}
		if (capability == CapabilityList.HEATABLE) {
			return (T) this.cap;
		}
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return (T) this.inventory;
		}
		return null;
	}

	protected void scheduleChunkForRenderUpdate() {
		BlockPos pos = this.getPos();
		this.getWorld().markBlockRangeForRenderUpdate(pos.getX() - 1, pos.getY() - 1, pos.getZ() - 1, pos.getX() + 1,
				pos.getY() + 1, pos.getZ() + 1);
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
		return ModularUI.builder(GuiTextures.BACKGROUND, 176, 166)
				.widget(new SlotWidget(this.inventory, 0, 7, 62).setBackgroundTexture(GuiTextures.SLOT, OVERLAY_COAL))
				.widget(new SlotWidget(this.inventory, 3, 49, 20).setBackgroundTexture(GuiTextures.SLOT, OVERLAY_INGOT))
				.widget(new SlotWidget(this.inventory, 4, 83, 20).setBackgroundTexture(GuiTextures.SLOT, OVERLAY_INGOT))
				.widget(new SlotWidget(this.inventory, 5, 117, 20).setBackgroundTexture(GuiTextures.SLOT,
						OVERLAY_INGOT))
				.widget(new SlotWidget(this.inventory, 6, 151, 20).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 7, 151, 41).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new SlotWidget(this.inventory, 8, 151, 62).setBackgroundTexture(GuiTextures.SLOT))
				.widget(new ImageWidget(49, 67, 77, 9, TEMPERATURE_PROGRESS))
				.widget(new PointerWidget(this::getTemperaturePointer, 47, 63, 5, 17).setPointer(POINTER,
						PointerWidget.MoveType.HORIZONTAL))
				.widget(new ProgressWidget(this::getBurnProgress, 31, 65, 14, 14).setProgressBar(FUEL, FUEL_FULL,
						ProgressWidget.MoveType.VERTICAL))
				.widget(new UpdatableTextWidget(this::getTemperatureString, () -> 0x404040, 49, 54))
				.bindPlayerInventory(player.inventory, GuiTextures.SLOT, 7, 83).build(this, player);
	}
}