package texturegeneratorlib;

import java.util.HashMap;
import java.util.HashSet;

import org.apache.logging.log4j.Logger;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.block.Block;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import texturegeneratorlib.texturestitching.CustomTextureEnumerator;
import texturegeneratorlib.texturestitching.CustomTextureGenerator;
import texturegeneratorlib.texturestitching.OptifineIntegration;
import texturegeneratorlib.texturestitching.ShadersModIntegration;
import texturegeneratorlib.texturestitching.TextureInformation;


@Mod(modid = TextureGeneratorLib.modid, name = "TextureGeneratorLib", version = "0.1")
public class TextureGeneratorLib {

	public static  final String modid = "TextureGeneratorLib";
	public static Logger log;

	public static HashMap<String, TextureInformation> alphaMaskMap = new HashMap<String, TextureInformation>();
	public static final String overlayDomain = "overlays";
	public static HashSet<Object> retextureBlock = new HashSet<Object>();

	@EventHandler
	public void PreInit(FMLPreInitializationEvent preEvent)
	{
		log = preEvent.getModLog();
	}
	@EventHandler public void load(FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new CustomTextureEnumerator());
		MinecraftForge.EVENT_BUS.register(new CustomTextureGenerator(overlayDomain, null));


		OptifineIntegration.init();
		ShadersModIntegration.init();
	}
	@EventHandler
	public void PostInit(FMLPostInitializationEvent postEvent){

	}
	
	/**
	 * Use this method to add blocks for texture generation
	 * 
	 * @param block - an instance of the block to be retextured
	 * @param textureName - texture name for the texture to be created
	 * @param parentTexture - name of the background texture
	 * @param masktype - name of the overlay
	 * @param maskDomain - MODID for the domain in which the overlay can be found
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
