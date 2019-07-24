package linwg.strategy;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

/**
 *
 * @author wengui
 * @date 2016/9/8
 */
public interface IImageLoader {
    /**
     * Load Image
     * @param context context
     * @param url url
     * @param imageView imageView
     * @param callback callback
     */
    void loadImage(Context context, String url, ImageView imageView,IResourceReadyCallback callback);

    /**
     * Return whether the image is loading completed.
     * @param view view
     * @return b
     */
    boolean isDrawableLoadingCompleted(ImageView view);

//    /**
//     * Load thumb image.
//     * @param context context
//     * @param originUrl originUrl
//     * @param thumbUrl thumbUrl
//     * @param imageView imageView
//     * @param callback callback
//     * @return b if false, the WrapImageView will call {@link #loadImage(Context, String, ImageView, IResourceReadyCallback)} to download thumb image.
//     */
//    boolean loadThumb(Context context,String originUrl,String thumbUrl,ImageView imageView,IResourceReadyCallback callback);

    /**
     * Get a bitmap from target view.
     * @param view target
     * @return bitmap
     */
    Bitmap getBitmapFromImageView(ImageView view);
}
