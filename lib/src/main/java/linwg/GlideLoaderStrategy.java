package linwg;

import android.content.Context;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GlideBitmapDrawable;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import linwg.strategy.IImageLoader;
import linwg.strategy.IResourceReadyCallback;

/**
 * Created by wengui on 2016/9/8.
 */
public class GlideLoaderStrategy implements IImageLoader{

    @Override
    public boolean isDrawableLoadingCompleted(ImageView view) {
        return isLoadingCompleted(view);
    }

    @Override
    public boolean loadThumb(Context context, String originUrl, String thumbUrl, ImageView imageView, final IResourceReadyCallback callback) {
        if(thumbUrl == null){
            DrawableRequestBuilder<String> request = Glide.with(context).load(originUrl).thumbnail(0.1f);
            request.listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                    callback.onResourceReady();
                    return false;
                }
            }).into(imageView);
        }else{
            loadImage(context,thumbUrl,imageView,callback);
        }
        return true;
    }

    @Override
    public Bitmap getBitmapFromImageView(ImageView view) {
        return ((GlideBitmapDrawable) view.getDrawable()).getBitmap();
    }

    private static boolean isLoadingCompleted(ImageView view){
        return view.getDrawable() == null || !(view.getDrawable() instanceof GlideDrawable);
    }

    @Override
    public void loadImage(Context context, String url, ImageView imageView, final IResourceReadyCallback callback) {
        DrawableTypeRequest<String> request = Glide.with(context).load(url);
        request.listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String s, Target<GlideDrawable> target, boolean b) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable glideDrawable, String s, Target<GlideDrawable> target, boolean b, boolean b1) {
                callback.onResourceReady();
                return false;
            }
        }).into(imageView);
    }
}
