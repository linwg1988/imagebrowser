package linwg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
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

    /**
     * @return if false, the WrapImageView will call {@link #loadThumb(Context, String, String, ImageView, IResourceReadyCallback)} to download thumb image.
     * */
    @Override
    public boolean loadThumb(final Context context, String originUrl, String thumbUrl, final ImageView imageView, final IResourceReadyCallback callback) {
        if(thumbUrl == null){
//            BitmapRequestBuilder<String, Bitmap> request = Glide.with(context).load(originUrl).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).thumbnail(0.1f);
//            request.listener(new RequestListener<String, Bitmap>() {
//                @Override
//                public boolean onException(Exception e, String s, Target<Bitmap> target, boolean b) {
//                    return false;
//                }
//
//                @Override
//                public boolean onResourceReady(Bitmap bitmap, String s, Target<Bitmap> target, boolean b, boolean b1) {
//                    imageView.setImageDrawable(new BitmapDrawable(context.getResources(),bitmap));
//                    callback.onResourceReady();
//                    return true;
//                }
//            }).into(imageView);

            RequestOptions options = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL);

            Glide.with(context).asBitmap().load(originUrl).apply(options).listener(new RequestListener<Bitmap>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                    imageView.setImageDrawable(new BitmapDrawable(context.getResources(),resource));
                    callback.onResourceReady();
                    return true;
                }
            }).into(imageView);


        }else{
            loadImage(context,thumbUrl,imageView,callback);
        }
        return true;
    }

    @Override
    public Bitmap getBitmapFromImageView(ImageView view) {
        return ((BitmapDrawable) view.getDrawable()).getBitmap();
    }

    private static boolean isLoadingCompleted(ImageView view){
        return view.getDrawable() != null;
    }

    @Override
    public void loadImage(final Context context, String url, final ImageView imageView, final IResourceReadyCallback callback) {
//        BitmapRequestBuilder<String, Bitmap> request = Glide.with(context).load(url).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL);
//        request.listener(new RequestListener<String, Bitmap>() {
//            @Override
//            public boolean onException(Exception e, String s, Target<Bitmap> target, boolean b) {
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(final Bitmap bitmap, String s, Target<Bitmap> target, boolean b, boolean b1) {
//                imageView.setImageDrawable(new BitmapDrawable(context.getResources(),bitmap));
//                callback.onResourceReady();
//                return true;
//            }
//        }).into(imageView);

        RequestOptions options = RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL);
        Glide.with(context).asBitmap().load(url).apply(options).listener(new RequestListener<Bitmap>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                imageView.setImageDrawable(new BitmapDrawable(context.getResources(),resource));
                callback.onResourceReady();
                return true;
            }
        }).into(imageView);
    }
}
