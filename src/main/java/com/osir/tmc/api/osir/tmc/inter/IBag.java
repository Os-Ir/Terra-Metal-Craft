package com.osir.tmc.api.osir.tmc.inter;

import net.minecraftforge.items.ItemStackHandler;

public interface IBag {
	public void setItems(ItemStackHandler items);

	public ItemStackHandler getItems();

	public boolean isEmpty();
	// public static ItemStackHandler loadItems(ItemStack stack) {
	// NBTTagCompound nbt = stack.getTagCompound();
	// NBTTagList list = nbt.getTagList("items", 10);
	// int size = list.tagCount();
	// ItemStackHandler items = new ItemStackHandler(size);
	// if (stack.isEmpty() || !stack.hasTagCompound()) {
	// return items;
	// }
	// if (!nbt.hasKey("items")) {
	// return items;
	// }
	// int i;
	// for (i = 0; i < size; i++) {
	// NBTTagCompound nbtItem = list.getCompoundTagAt(i);
	// int idx = nbtItem.getInteger("index");
	// items.setStackInSlot(idx, new ItemStack(nbtItem));
	// }
	// return items;
	// }
	//
	// public static ItemStack saveItems(ItemStack stack, ItemStackHandler items) {
	// if (stack.isEmpty()) {
	// return stack;
	// }
	// NBTTagList list = new NBTTagList();
	// int i;
	// for (i = 0; i < items.getSlots(); i++) {
	// ItemStack item = items.getStackInSlot(i);
	// if (item.isEmpty()) {
	// continue;
	// }
	// NBTTagCompound nbtItem = new NBTTagCompound();
	// nbtItem.setInteger("index", i);
	// nbtItem = item.writeToNBT(nbtItem);
	// list.appendTag(nbtItem);
	// }
	// if (!list.hasNoTags()) {
	// NBTTagCompound nbt = new NBTTagCompound();
	// nbt.setTag("items", list);
	// stack.setTagCompound(nbt);
	// } else {
	// stack.setTagCompound(null);
	// return stack;
	// }
	// return stack;
	// }
}