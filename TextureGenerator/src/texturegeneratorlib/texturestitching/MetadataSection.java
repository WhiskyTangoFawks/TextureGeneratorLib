package texturegeneratorlib.texturestitching;

import java.lang.reflect.Type;



import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.IMetadataSectionSerializer;
import net.minecraft.util.JsonUtils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
/**
 * @author OctarineNoise
 */
public class MetadataSection implements IMetadataSection {

	public boolean rotation;

	public static class BetterFoliageMetadataSerializer implements IMetadataSectionSerializer {

		@Override
		public String getSectionName() {
			return "betterfoliage";
		}

		@Override
		public MetadataSection deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
			MetadataSection result = new MetadataSection();
			result.rotation = JsonUtils.getJsonObjectBooleanFieldValueOrDefault(json.getAsJsonObject(), "rotation", true);
			return result;
		}

	}
}
