package com.osir.tmc.api.util.registry;

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

	public Iterator<V> iterator() {
		return this.list.iterator();
	}

	public void register(V value) {
		if (this.frozen) {
			throw new IllegalStateException("This registry is already forzen");
		}
		if (this.list.contains(value)) {
			throw new IllegalStateException("This value has been registered");
		}
		this.list.add(value);
	}

	public void delete(V value) {
		if (this.list.contains(value)) {
			this.list.remove(value);
		}
	}
}