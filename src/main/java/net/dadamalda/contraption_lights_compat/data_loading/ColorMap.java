package net.dadamalda.contraption_lights_compat.data_loading;

import com.google.common.collect.ImmutableMap;
import net.dadamalda.contraption_lights_compat.CLCLightColors;
import net.minecraft.resources.ResourceLocation;
import xyz.atmerek.contraptionlights.api.LightColorProvider;

import java.util.HashMap;
import java.util.Objects;

public class ColorMap {
    private ImmutableMap<ResourceLocation, Integer> data;
    private final HashMap<ResourceLocation, Integer> uncommitedData = new HashMap<>();

    public void put(ResourceLocation id, int color) {
        uncommitedData.put(id, color);
    }
    public void put(String id, int color) {
        uncommitedData.put(ResourceLocation.parse(id), color);
    }
    public void put(ResourceLocation id, String color) {
        uncommitedData.put(id, parseColor(color));
    }
    public void put(String id, String color) {
        uncommitedData.put(ResourceLocation.parse(id), parseColor(color));
    }

    public void commit() {
        data = ImmutableMap.copyOf(uncommitedData);
        uncommitedData.clear();
    }

    public int getOrPass(ResourceLocation id) {
        if(data == null) return LightColorProvider.PASS;
        Integer found = data.getOrDefault(id, LightColorProvider.PASS);
        if(found == null) return LightColorProvider.PASS;
        return found;
    }

    private int parseColor(String color) {
        if(Objects.equals(color, "white")) return CLCLightColors.WHITE;
        if(Objects.equals(color, "warm")) return CLCLightColors.WARM;
        if(!color.startsWith("#")) return LightColorProvider.PASS;
        String colorHex = color.substring(1);
        if(colorHex.length() == 3) {
            StringBuilder fullColorHex = new StringBuilder();
            for (String hexDigit : colorHex.split("")) {
                fullColorHex.append(hexDigit.repeat(2));
            }
            return Integer.parseInt(fullColorHex.toString(), 16);
        } else if(colorHex.length() == 6) {
            return Integer.parseInt(colorHex, 16);
        } else {
            return LightColorProvider.PASS;
        }
    }
}
