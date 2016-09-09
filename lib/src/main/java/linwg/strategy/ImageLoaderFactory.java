package linwg.strategy;

import linwg.GlideLoaderStrategy;

/**
 * Created by wengui on 2016/9/8.
 */
public class ImageLoaderFactory {
    private ImageLoaderFactory() {
    }

    public static void set(IImageLoader loader) {
        ImageLoaderFactory.loader = loader;
    }

    private static IImageLoader loader = null;

    public static IImageLoader get() {
        if (ImageLoaderFactory.loader == null)
            loader = new GlideLoaderStrategy();
        return loader;
    }
}
