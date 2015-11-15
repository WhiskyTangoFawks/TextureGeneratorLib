package texturegeneratorlib.texturestitching;

import java.util.Iterator;
import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import texturegeneratorlib.TextureGeneratorLib;

public class CustomTextureEnumerator implements IIconRegister{

	@SideOnly(Side.CLIENT)
	public static class OverlaidBlockTextureFoundEvent extends Event {
		public TextureMap blockTextures;
		public TextureAtlasSprite icon;

		public ResourceLocation resourceLocation;

		private OverlaidBlockTextureFoundEvent(TextureMap blockTextures, TextureAtlasSprite icon, ResourceLocation resourceLocation) {
			super();
			this.blockTextures = blockTextures;
			this.icon = icon;
			this.resourceLocation = resourceLocation;
		}
	}

	public TextureMap blockTextures; // texture atlas for block textures used in current run


	@Override
	public IIcon registerIcon(String resourceLocation) {

		ResourceLocation customTexture = new ResourceLocation(resourceLocation);
		customTexture = new ResourceLocation(customTexture.getResourceDomain(), "textures/blocks/"+customTexture.getResourcePath()+".png");
		if (ResourceUtils.resourceExists(customTexture)){//  && ClientProxy.retextureBlock.contains(customTexture.getResourcePath())) {
					return blockTextures.registerIcon(resourceLocation);
				}

		//replaces the domain in the icon it returns to the block
	//	WTFCore.log.info(String.format("CustomTextureEnumerator.registerIcon for" + resourceLocation));

		ResourceLocation resource = new ResourceLocation(resourceLocation);
		String newresourceLocation = TextureGeneratorLib.overlayDomain + ":" +resource.getResourcePath();

		return blockTextures.registerIcon(newresourceLocation);

	/*	Collection<IIcon> ctmIcons = OptifineIntegration.getAllCTMForIcon(original);
		if (!ctmIcons.isEmpty()) {
			WTFCore.log.info(String.format("Found %d CTM variants for texture %s", ctmIcons.size(), original.getIconName()));
			for (IIcon ctmIcon : ctmIcons) {
				MinecraftForge.EVENT_BUS.post(new OverlaidBlockTextureFoundEvent(blockTextures, (TextureAtlasSprite) ctmIcon));
			}
		} */

	}


	@SubscribeEvent(priority=EventPriority.LOWEST)
	@SuppressWarnings("unchecked")
	public void handleTextureReload(TextureStitchEvent.Pre event) {
		if (event.map.getTextureType() != 0) return;
		blockTextures = event.map;

		TextureGeneratorLib.log.info("CustomTextureEnumerator.handleTextureReload:Iterating to find instancesof OverlaidBlock");

		Iterator<Block> iterator = Block.blockRegistry.iterator();
		while (iterator.hasNext()) {
			Block block = iterator.next();
			if (TextureGeneratorLib.retextureBlock.contains(block)) {
				//WTFCore.log.info(String.format("CustomTextureEnumerator.handleTextureReload: Instance of OverlaidBlock found: %s", block.getClass().getName()));
				block.registerBlockIcons(this);
			}
			/* Collection<IIcon> ctmIcons = OptifineIntegration.getAllCTMForBlock(block);
			if (!ctmIcons.isEmpty()) {
				WTFCore.log.info(String.format("Found %d CTM texture variants for block: id=%d class=%s", ctmIcons.size(), Block.getIdFromBlock(block), block.getClass().getName()));
				for (IIcon ctmIcon : ctmIcons) {
					MinecraftForge.EVENT_BUS.post(new LeafTextureFoundEvent(blockTextures, (TextureAtlasSprite) ctmIcon));
				}
			} */
		}
	}

	@SubscribeEvent
	public void endTextureReload(TextureStitchEvent.Post event) {
		if (event.map.getTextureType() != 0) return;
		blockTextures = null;
	}
	


}
