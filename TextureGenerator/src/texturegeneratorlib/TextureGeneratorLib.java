package texturegeneratorlib;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.logging.log4j.Logger;


import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import texturegeneratorlib.proxy.CommonProxy;
import texturegeneratorlib.texturestitching.TextureInformation;


@Mod(modid = TextureGeneratorLib.modid, name = "TextureGeneratorLib", version = "0.2")
public class TextureGeneratorLib {

	public static  final String modid = "TextureGeneratorLib";
	public static Logger log;

	public static HashMap<String, TextureInformation> alphaMaskMap = new HashMap<String, TextureInformation>();
	public static final String overlayDomain = "overlays";
	public static HashSet<Object> retextureBlock = new HashSet<Object>();
	
	@SidedProxy(clientSide="texturegeneratorlib.proxy.ClientProxy", serverSide="texturegeneratorlib.proxy.CommonProxy")
	public static CommonProxy proxy;


	@EventHandler
	public void PreInit(FMLPreInitializationEvent preEvent)
	{
		log = preEvent.getModLog();
	}
	@EventHandler public void load(FMLInitializationEvent event)
	{
		proxy.TextureGeneration();
	}
	@EventHandler
	public void PostInit(FMLPostInitializationEvent postEvent){

	}
	
	/**
	 * Use this method to add blocks for texture generation
	 * 
	 * @param block - an instance of the block to be retextured
	 * @param textureName - texture name for the texture to be created (without a domain)
	 * @param parentTexture - name and domain of the background texture (e.g. "minecraft:stone")
	 * @param masktype - name of the overlay (without the size indicator, and without the file extension)
	 * @param maskDomain - MODID for the domain in which the overlay can be found, e.g wtfores:textures/blocks/overlays/
	 * @param alphaMask - set this to true for an alpha mask cutout (stalactites), set it to false for an overlay composite (ores)
	 */
	
	public static void registerBlockOverlay(Block block, String textureName, String parentTexture, String masktype, String maskDomain, boolean alphaMask){

		ResourceLocation parent = new ResourceLocation(parentTexture);
		parent = new ResourceLocation(parent.getResourceDomain()+":textures/blocks/"+parent.getResourcePath()+".png");
		alphaMaskMap.put(textureName, new TextureInformation(parent, masktype, maskDomain, alphaMask));

		String textureNameN = textureName + "_n";
		String parentTextureN = parentTexture + "_n";

		parent = new ResourceLocation(parentTextureN);
		parent = new ResourceLocation(parent.getResourceDomain()+":textures/blocks/"+parent.getResourcePath()+".png");
		alphaMaskMap.put(textureNameN, new TextureInformation(parent, masktype, maskDomain, alphaMask));

		String textureNameS = textureName + "_s";
		String parentTextureS = parentTexture + "_s";

		parent = new ResourceLocation(parentTextureS);
		parent = new ResourceLocation(parent.getResourceDomain()+":textures/blocks/"+parent.getResourcePath()+".png");
		alphaMaskMap.put(textureNameS, new TextureInformation(parent, masktype, maskDomain, alphaMask));
		retextureBlock.add(block);

	}


/**
 * This method is used to override an existing block texture with an existing texture
 * @param oreBlock
 * @param textureName
 * @param parentTexture
 * @param masktype
 * @param maskDomain
 * @param alphaMask
 */
	public static void registerBlockOverlayOverride(Block oreBlock, String textureName, String parentTexture, String masktype, String maskDomain, boolean alphaMask){
		registerBlockOverlay(oreBlock, textureName, parentTexture, masktype, maskDomain, alphaMask);
		//retextureBlock.add(oreBlock);
		retextureBlock.add(textureName);
	}

}
