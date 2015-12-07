package texturegeneratorlib.proxy;

import net.minecraftforge.common.MinecraftForge;
import texturegeneratorlib.TextureGeneratorLib;
import texturegeneratorlib.texturestitching.CustomTextureEnumerator;
import texturegeneratorlib.texturestitching.CustomTextureGenerator;
import texturegeneratorlib.texturestitching.OptifineIntegration;
import texturegeneratorlib.texturestitching.ShadersModIntegration;

public class ClientProxy extends CommonProxy{
	
	public void TextureGeneration(){

		MinecraftForge.EVENT_BUS.register(new CustomTextureEnumerator());
		MinecraftForge.EVENT_BUS.register(new CustomTextureGenerator(TextureGeneratorLib.overlayDomain, null));
		OptifineIntegration.init();
		ShadersModIntegration.init();
	}

}
