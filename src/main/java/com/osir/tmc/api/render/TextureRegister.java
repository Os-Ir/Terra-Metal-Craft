package com.osir.tmc.api.render;

import com.osir.tmc.api.util.registry.UnorderedRegistry;

import codechicken.lib.texture.TextureUtils;
import codechicken.lib.texture.TextureUtils.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.ResourceLocation;

public class TextureRegister implements IIconRegister {
	public static final UnorderedRegistry<TextureRegister> REGISTRY_TEXTURE = new UnorderedRegistry<TextureRegister>();

	protected ResourceLocation location;
	protected TextureAtlasSprite texture;

	public TextureRegister(String modid, String path) {
		this(new ResourceLocation(modid, path));
	}

	public TextureRegister(ResourceLocation location) {
		TextureUtils.addIconRegister(this);
		this.location = location;
	}

	public TextureAtlasSprite getTexture() {
		return this.texture;
	}

	@Override
	public void registerIcons(TextureMap textureMap) {
		this.texture = textureMap.registerSprite(this.location);
	}
}