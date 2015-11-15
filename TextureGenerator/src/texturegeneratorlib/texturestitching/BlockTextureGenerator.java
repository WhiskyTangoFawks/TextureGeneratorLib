package texturegeneratorlib.texturestitching;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import texturegeneratorlib.TextureGeneratorLib;
import texturegeneratorlib.loader.impl.CodeRefs;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/** Base class for texture generators. Registers itself as a domain resource manager for the duration of block texture stitching.
 * @author octarine-noise
 *
 */
@SideOnly(Side.CLIENT)
public abstract class BlockTextureGenerator implements IResourceManager {

	/** Resource domain name of generated textures */
	public String domainName;

	/** Resource location for fallback texture (if the generation process fails) */
	//public ResourceLocation missingResource;

	public BlockTextureGenerator(String domainName) {
		this.domainName = domainName;
		//this.missingResource = missingResource;
	}

	@SubscribeEvent
	public void handleTextureReload(TextureStitchEvent.Pre event) {
		TextureGeneratorLib.log.info(String.format("Texture Generator added for Domain "+ domainName));
		if (event.map.getTextureType() != 0) return;

		Map<String, IResourceManager> domainManagers = CodeRefs.fDomainResourceManagers.getInstanceField(Minecraft.getMinecraft().getResourceManager());
		if (domainManagers == null) {
			TextureGeneratorLib.log.warn("Failed to inject texture generator");
			return;
		}
		domainManagers.put(domainName, this);
	}

	@SubscribeEvent
	public void endTextureReload(TextureStitchEvent.Post event) {
		if (event.map.getTextureType() != 0) return;

		// don't leave a mess
		Map<String, IResourceManager> domainManagers = CodeRefs.fDomainResourceManagers.getInstanceField(Minecraft.getMinecraft().getResourceManager());
		if (domainManagers != null) domainManagers.remove(domainName);
	}

	@Override
	public Set<String> getResourceDomains() {
		return ImmutableSet.<String>of(domainName);
	}

	@Override
	public List<IResource> getAllResources(ResourceLocation resource) throws IOException {
		return ImmutableList.<IResource>of(getResource(resource));
	}

	//public IResource getMissingResource() throws IOException {
	//	return null;// Minecraft.getMinecraft().getResourceManager().getResource(missingResource);
	//}

	protected static int blendRGB(int rgbOrig, int rgbBlend, int weightOrig, int weightBlend) {
		int r = (((rgbOrig >> 16) & 0xFF) * weightOrig + ((rgbBlend >> 16) & 0xFF) * weightBlend) / (weightOrig + weightBlend);
		int g = (((rgbOrig >> 8) & 0xFF) * weightOrig + ((rgbBlend >> 8) & 0xFF) * weightBlend) / (weightOrig + weightBlend);
		int b = ((rgbOrig & 0xFF) * weightOrig + (rgbBlend & 0xFF) * weightBlend) / (weightOrig + weightBlend);
		int a = (rgbOrig >> 24) & 0xFF;
		int result = a << 24 | r << 16 | g << 8 | b;
		return result;
	}
}