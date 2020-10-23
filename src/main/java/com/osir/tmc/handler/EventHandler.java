package com.osir.tmc.handler;

import com.github.zi_jing.cuckoolib.material.Materials;
import com.github.zi_jing.cuckoolib.material.SolidShapes;
import com.osir.tmc.Main;
import com.osir.tmc.api.capability.CapabilityHeat;
import com.osir.tmc.api.capability.CapabilityLiquidContainer;
import com.osir.tmc.api.capability.CapabilityWork;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.capability.ILiquidContainer;
import com.osir.tmc.api.capability.ModCapabilities;
import com.osir.tmc.api.container.ContainerListenerCapability;
import com.osir.tmc.api.heat.HeatMaterialList;
import com.osir.tmc.api.heat.MaterialStack;
import com.osir.tmc.api.recipe.ScalableRecipe;
import com.osir.tmc.api.util.CapabilityUtil;
import com.osir.tmc.item.ItemMould;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.player.PlayerContainerEvent.Open;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;

@EventBusSubscriber(modid = Main.MODID)
public class EventHandler {
	@SubscribeEvent
	public static void onStoneWork(RightClickBlock e) {
		if (e.getWorld().isRemote) {
			return;
		}
		if (e.getFace() != EnumFacing.UP) {
			return;
		}
		BlockPos pos = e.getPos();
		World world = e.getWorld();
		IBlockState state = world.getBlockState(pos);
		if (!(state.isFullBlock() && state.getMaterial() == Material.ROCK)) {
			return;
		}
		ItemStack stack = e.getItemStack();
		EntityPlayer player = e.getEntityPlayer();
		Vec3d vec = e.getHitVec();
		vec = vec.subtract(pos.getX(), pos.getY(), pos.getZ());
		ItemStack result = ItemStack.EMPTY;
		if (stack.getItem() == Items.FLINT) {
			if (vec.x >= 0.25 && vec.x <= 0.75 && vec.z >= 0.25 && vec.z <= 0.75) {
				result = ItemHandler.grindedFlint.getItemStack();
			} else {
				result = ItemHandler.chippedFlint.getItemStack();
			}
		} else if (ItemHandler.grindedFlint.isItemEqual(stack)) {
			if (vec.x >= 0.25 && vec.x <= 0.75 && vec.z >= 0.25 && vec.z <= 0.75) {
				result = ItemHandler.MATERIAL_ITEM.getItemStack(SolidShapes.KNIFE_HEAD, Materials.FLINT);
			} else {
				result = ItemHandler.MATERIAL_ITEM.getItemStack(SolidShapes.CHISEL_HEAD, Materials.FLINT);
			}
		} else if (ItemHandler.chippedFlint.isItemEqual(stack)) {
			if (vec.x >= 0.25 && vec.x <= 0.75 && vec.z >= 0.25 && vec.z <= 0.75) {
				result = ItemHandler.MATERIAL_ITEM.getItemStack(SolidShapes.CHISEL_HEAD, Materials.FLINT);
			} else {
				result = ItemHandler.MATERIAL_ITEM.getItemStack(SolidShapes.HAMMER_HEAD, Materials.FLINT);
			}
		}
		if (!result.isEmpty()) {
			stack.shrink(1);
			player.inventory.addItemStackToInventory(result);
			if (!result.isEmpty()) {
				InventoryHelper.spawnItemStack(e.getWorld(), pos.getX(), pos.getY(), pos.getZ(), result);
			}
		}
	}

	@SubscribeEvent
	public static void registerRecipes(RegistryEvent.Register<IRecipe> event) {
		RecipeHandler.registerHeatRecipe();
		OrePrefixRecipeHandler.register();
	}

	@SubscribeEvent
	public static void onAttachHeat(AttachCapabilitiesEvent<ItemStack> e) {
		ItemStack stack = e.getObject();
		if (stack.hasCapability(ModCapabilities.HEATABLE, null)) {
			return;
		}
		MaterialStack mat = HeatMaterialList.findMaterial(stack);
		ScalableRecipe recipe = HeatMaterialList.findRecipe(stack);
		if (mat != null && recipe != null) {
			e.addCapability(CapabilityHeat.KEY,
					new CapabilityHeat(mat.getMaterial(), mat.getAmount(), (int) recipe.getValue("temp")));
			e.addCapability(CapabilityWork.KEY, new CapabilityWork());
		}
	}

	@SubscribeEvent
	public static void onAttachLiquidContainer(AttachCapabilitiesEvent<ItemStack> e) {
		ItemStack stack = e.getObject();
		if (stack.hasCapability(ModCapabilities.LIQUID_CONTAINER, null)) {
			return;
		}
		if (stack.getItem() instanceof ItemMould) {
			e.addCapability(CapabilityLiquidContainer.KEY, new CapabilityLiquidContainer(144));
		}
	}

	@SubscribeEvent
	public static void onPlayerLoggedIn(PlayerLoggedInEvent e) {
		if (e.player instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) e.player;
			player.inventoryContainer.addListener(new ContainerListenerCapability(player));
		}
	}

	@SubscribeEvent
	public static void onPlayerRespawn(PlayerRespawnEvent e) {
		if (e.player instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) e.player;
			player.inventoryContainer.addListener(new ContainerListenerCapability(player));
		}
	}

	@SubscribeEvent
	public static void onContainerOpen(Open e) {
		if (e.getEntityPlayer() instanceof EntityPlayerMP) {
			EntityPlayerMP player = (EntityPlayerMP) e.getEntityPlayer();
			e.getContainer().addListener(new ContainerListenerCapability(player));
		}
	}

	@SubscribeEvent
	public static void onTempUpdate(LivingUpdateEvent e) {
		if (e.getEntityLiving() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) e.getEntityLiving();
			InventoryPlayer inv = player.inventory;
			for (int i = 0; i < inv.getSizeInventory(); i++) {
				ItemStack stack = inv.getStackInSlot(i);
				if (stack.hasCapability(ModCapabilities.HEATABLE, null)) {
					IHeatable cap = stack.getCapability(ModCapabilities.HEATABLE, null);
					CapabilityUtil.heatExchange(cap, 20, 200);
				}
				if (stack.hasCapability(ModCapabilities.LIQUID_CONTAINER, null)) {
					ILiquidContainer liquid = stack.getCapability(ModCapabilities.LIQUID_CONTAINER, null);
					for (IHeatable cap : liquid.getMaterial()) {
						CapabilityUtil.heatExchange(cap, 20, 400);
					}
				}
			}
		}
	}
}