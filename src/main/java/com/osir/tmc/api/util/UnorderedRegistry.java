package com.osir.tmc.api.util;

import java.util.Iterator;
import java.util.List;

import net.minecraft.util.NonNullList;

public class UnorderedRegistry<V> {
	private List<V> list;
	private boolean frozen;

	public UnorderedRegistry() {
		this.list = NonNullList.create();
	}

	public List<V> getList() {
		return this.list;
	}

	public boolean isFrozen() {
		return this.frozen;
	}

	public void freeze() {
		if (this.frozen) {
			throw new IllegalStateException("This registry is already forzen");
		}
		this.frozen = true;
	}

	public Iterator iterator() {
		return list.iterator();
	}

	public void register(V value) {
		if (this.frozen) {
			throw new IllegalStateException("This registry is already forzen");
		}
		if (list.contains(value)) {
			throw new IllegalStateException("This value has been registered");
		}
		list.add(value);
	}

	public void delete(V value) {
		if (list.contains(value)) {
			list.remove(value);
		}
	}
}