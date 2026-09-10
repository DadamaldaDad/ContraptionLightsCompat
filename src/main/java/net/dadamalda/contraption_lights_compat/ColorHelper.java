package net.dadamalda.contraption_lights_compat;

import net.minecraft.util.FastColor;

import java.util.List;

public class ColorHelper {
    public static int mix(List<Integer> colors) {
        List<Integer> filteredColors = colors.stream().filter(color -> color != null && color != -1).toList();
        if(filteredColors.isEmpty()) return -1;
        int colorCount = filteredColors.size();
        int red = 0;
        int green = 0;
        int blue = 0;
        for (Integer color : colors) {
            red += FastColor.ARGB32.red(color);
            green += FastColor.ARGB32.green(color);
            blue += FastColor.ARGB32.blue(color);
        }
        return FastColor.ARGB32.color(0, Math.min(255, red/colorCount), Math.min(255, green/colorCount), Math.min(255, blue/colorCount));
    }

    public static int brighten(int color) {
        if(color == -1) return -1;
        int red = FastColor.ARGB32.red(color);
        int green = FastColor.ARGB32.green(color);
        int blue = FastColor.ARGB32.blue(color);
        int max_val = Math.max(Math.max(red, green), blue);
        if(max_val == 0) return 0x000000;
        float scale = (float) 255 / max_val;
        return FastColor.ARGB32.color(0, Math.min(255, (int)(red*scale)), Math.min(255, (int)(green*scale)), Math.min(255, (int)(blue*scale)));
    }
}
