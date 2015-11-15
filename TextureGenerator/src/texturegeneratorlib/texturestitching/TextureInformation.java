package texturegeneratorlib.texturestitching;

import net.minecraft.util.ResourceLocation;

public class TextureInformation {

	public ResourceLocation parentTexture;
	public String alphaMaskTexture;
	public String overlayDomain;
	public boolean alphaMask;

	public  TextureInformation(ResourceLocation parentTexture, String alphaMaskTexture, String overlayDomain, boolean alphaMask){
		this.parentTexture = parentTexture;
		this.alphaMaskTexture = alphaMaskTexture;
		this.overlayDomain = overlayDomain;
		this.alphaMask = alphaMask;
	}

}
