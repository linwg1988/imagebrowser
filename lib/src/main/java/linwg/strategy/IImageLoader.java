package linwg.strategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 * Created by wengui on 2016/9/8.
 */
public interface IImageLoader {
    void loadImage(Context context, String url, ImageView imageView,IResourceReadyCallback callback);
    boolean isDrawableLoadingCompleted(ImageView view);
    boolean loadThumb(Context context,String originUrl,String thumbUrl,ImageView imageView,IResourceReadyCallback callback);
    Bitmap getBitmapFromImageView(ImageView view);
}
