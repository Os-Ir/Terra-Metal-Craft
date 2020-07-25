package com.osir.tmc.block;

import java.util.List;

import com.osir.tmc.ModCreativeTab;
import com.osir.tmc.Main;
import com.osir.tmc.api.capability.IHeatable;
import com.osir.tmc.api.capability.ILiquidContainer;
import com.osir.tmc.api.capability.ModCapabilities;
import com.osir.tmc.api.heat.HeatMaterial;
import com.osir.tmc.api.render.ICustomModel;
import com.osir.tmc.handler.BlockHandler;
import com.osir.tmc.handler.ItemHandler;
import com.osir.tmc.item.ItemMould;
import com.osir.tmc.te.TEMould;

import gregtech.api.unification.OreDictUnifier;
import gregtech.api.unification.ore.OrePrefix;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.capabilities.Capability.IStorage;

public class BlockMould extends BlockContainer implements ICustomModel {
	public static final AxisAlignedBB MOULD_AABB = new AxisAlignedBB(0.125, 0, 0.125, 0.875, 0.25, 0.875);

	public BlockMould() {
		super(Material.CLAY);
		this.setUnlocalizedName("mould");
		this.setRegistryName(Main.MODID, "mould");
		this.setHardness(0.5F);
		this.setSoundType(SoundType.STONE);
		this.setCreativeTab(ModCreativeTab.tabEquipment);
		BlockHandler.BLOCK_REGISTRY.register(this);
		ItemHandler.ITEM_REGISTRY.register(new ItemMould(this));
	}

	@Override
	public void getDrops(NonNullList<ItemStack> drops, IBlockAccess world, BlockPos pos, IBlockState state,
			int fortune) {
		ItemStack stack = new ItemStack(BlockHandler.MOULD);
		TileEntity te = world.getTileEntity(pos);
		if (te.hasCapability(ModCapabilities.LIQUID_CONTAINER, null)
				&& stack.hasCapability(ModCapabilities.LIQUID_CONTAINER, null)) {
			IStorage<ILiquidContainer> storage = ModCapabilities.LIQUID_CONTAINER.getStorage();
			NBTBase nbt = storage.writeNBT(ModCapabilities.LIQUID_CONTAINER,
					te.getCapability(ModCapabilities.LIQUID_CONTAINER, null), null);
			storage.readNBT(ModCapabilities.LIQUID_CONTAINER,
					stack.getCapability(ModCapabilities.LIQUID_CONTAINER, null), null, nbt);
		}
		drops.add(stack);
	}

	@Override
	public void onBlockPlacedBy(World world, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (stack.hasCapability(ModCapabilities.LIQUID_CONTAINER, null)) {
			world.setTileEntity(pos, new TEMould(stack.getCapability(ModCapabilities.LIQUID_CONTAINER, null)));
		}
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player,
			boolean willHarvest) {
		if (willHarvest) {
			return true;
		}
		return super.removedByPlayer(state, world, pos, player, willHarvest);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te,
			ItemStack stack) {
		super.harvestBlock(world, player, pos, state, te, stack);
		world.setBlockToAir(pos);
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		if (world.isRemote) {
			return true;
		}
		TileEntity te = world.getTileEntity(pos);
		if (te.hasCapability(ModCapabilities.LIQUID_CONTAINER, null)) {
			ILiquidContainer cap = te.getCapability(ModCapabilities.LIQUID_CONTAINER, null);
			List<IHeatable> list = cap.getMaterial();
			if (!list.isEmpty()) {
				IHeatable heat = list.get(0);
				HeatMaterial material = heat.getMaterial();
				ItemStack stack = OreDictUnifier.get(OrePrefix.ingot, material.getMaterial());
				if (heat.getUnit() >= 144 && !heat.isMelt() && !stack.isEmpty()) {
					heat.increaseUnit(-144, true);
					if (heat.getUnit() == 0) {
						list.remove(0);
					}
					player.addItemStackToInventory(stack);
					if (!stack.isEmpty()) {
						player.dropItem(stack, false, false);
					}
				}
			}
		}
		return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return MOULD_AABB;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isFullCube(IBlockState state) {
		return false;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState state) {
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta) {
		return new TEMould();
	}

	@Override
	public ModelResourceLocation getBlockModel(ModelRegistryEvent e) {
		return new ModelResourceLocation(Main.MODID + ":mould", "inventory");
	}

	@Override
	public int getMetaData(ModelRegistryEvent e) {
		return 0;
	}
}