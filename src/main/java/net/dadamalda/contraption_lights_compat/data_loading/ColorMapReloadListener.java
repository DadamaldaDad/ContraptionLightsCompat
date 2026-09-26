package net.dadamalda.contraption_lights_compat.data_loading;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.neoforged.fml.ModList;

import java.util.Map;

public class ColorMapReloadListener extends SimpleJsonResourceReloadListener {
    public ColorMapReloadListener() {
        super(new Gson(), "contraption_lights_compat/color_maps");
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        boolean isMoonlightLoaded = ModList.get().isLoaded("moonlight");
        boolean isCBMultipartLoaded = ModList.get().isLoaded("cb_multipart");
        for (Map.Entry<ResourceLocation, JsonElement> entry : resourceLocationJsonElementMap.entrySet()) {
            if(!entry.getValue().isJsonObject()) continue;
            JsonObject root = entry.getValue().getAsJsonObject();
            if(root.get("fluids") != null && root.get("fluids").isJsonObject()) {
                JsonObject fluids = root.get("fluids").getAsJsonObject();
                for (Map.Entry<String, JsonElement> fluid : fluids.asMap().entrySet()) {
                    if(!fluid.getValue().isJsonPrimitive()) continue;
                    ColorMaps.FLUIDS.put(fluid.getKey(), fluid.getValue().getAsString());
                    if(!isMoonlightLoaded) continue;
                    ColorMaps.SOFT_FLUIDS.put(fluid.getKey(), fluid.getValue().getAsString());
                }
            }
            if(isMoonlightLoaded && root.get("soft_fluids") != null && root.get("soft_fluids").isJsonObject()) {
                JsonObject soft_fluids = root.get("soft_fluids").getAsJsonObject();
                for (Map.Entry<String, JsonElement> soft_fluid : soft_fluids.asMap().entrySet()) {
                    if(!soft_fluid.getValue().isJsonPrimitive()) continue;
                    ColorMaps.SOFT_FLUIDS.put(soft_fluid.getKey(), soft_fluid.getValue().getAsString());
                }
            }
            if(isCBMultipartLoaded && root.get("multiparts") != null && root.get("multiparts").isJsonObject()) {
                JsonObject multiparts = root.get("multiparts").getAsJsonObject();
                for (Map.Entry<String, JsonElement> multipart : multiparts.asMap().entrySet()) {
                    if(!multipart.getValue().isJsonPrimitive()) continue;
                    ColorMaps.MULTIPARTS.put(multipart.getKey(), multipart.getValue().getAsString());
                }
            }
        }
        ColorMaps.FLUIDS.commit();
        ColorMaps.SOFT_FLUIDS.commit();
        ColorMaps.MULTIPARTS.commit();
    }
}
