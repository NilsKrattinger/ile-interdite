package ileinterdite.util;

import java.awt.image.BufferedImage;
import java.util.HashMap;

public class ImageUtils {

    private static HashMap<String, BufferedImage> cachedImages = new HashMap<>();

    public static void cacheImage(String path, BufferedImage image) {
        cachedImages.put(path, image);
    }

    public static BufferedImage getCachedImage(String path) {
        return cachedImages.get(path);
    }

}
