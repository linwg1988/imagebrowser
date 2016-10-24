package linwg;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.RectF;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import linwg.org.lib.R;
import linwg.strategy.IImageLoader;
import linwg.strategy.IResourceReadyCallback;
import linwg.strategy.ImageLoaderFactory;
import uk.co.senab.photoview.PhotoView;
import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by wengui on 2016/9/7.
 */
public class WrapImageView {
    IImageLoader imageLoader;
    private final ImageView thumbImageView;
    private final PhotoView photoView;
    private final String url;
    private final String thumbUrl;
    private final RectF origin;
    private final int screenWidth;
    private final float thumbWidth;
    private final int screenHeight;
    private final float scale;
    private final boolean isStartTargetView;
    private final float thumbOriginLeft;
    private final float thumbOriginTop;
    private final ProgressBar mProgressBar;
    View targetView;
    private boolean hasThumbPlaying;
    private boolean hasPhotoPlaying;
    boolean isCenterCrop;

    WrapImageView(final ImageBrowser browser, View targetView, String url, String thumbUrl, RectF origin,
                  boolean isStartView, int screenWidth, int screenHeight, boolean isCenterCrop) {
        imageLoader = ImageLoaderFactory.get();
        this.isCenterCrop = isCenterCrop;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.isStartTargetView = isStartView;
        this.targetView = targetView;
        mProgressBar = (ProgressBar) targetView.findViewById(R.id.pb_img_detail);
        mProgressBar.setVisibility(View.GONE);
        this.url = url;
        this.thumbUrl = thumbUrl;
        if (origin == null) {
            origin = new RectF(screenWidth / 2, screenHeight / 2, screenWidth / 2, screenHeight / 2);
        }
        this.origin = origin;
        this.thumbImageView = (ImageView) targetView.findViewById(R.id.iv_thumbnail);
        this.photoView = (PhotoView) targetView.findViewById(R.id.photo_view);
        thumbWidth = thumbImageView.getLayoutParams().width;

        scale = thumbWidth / screenWidth;
        thumbOriginLeft = (screenWidth - thumbWidth) / 2;
        thumbOriginTop = (screenHeight - thumbWidth) / 2;
        photoView.setOnViewTapListener(new PhotoViewAttacher.OnViewTapListener() {
            @Override
            public void onViewTap(View view, float x, float y) {
                browser.dismiss();
            }
        });
    }

    public void startLoading() {
        if (isCenterCrop) {
            thumbImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        }
        imageLoader.loadImage(targetView.getContext(), url, photoView, new IResourceReadyCallback() {
            @Override
            public void onResourceReady() {
                if (!hasPhotoPlaying && isStartTargetView) {
                    startPhotoAnimation();
                }
                thumbImageView.clearAnimation();
                thumbImageView.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
            }
        });

        IResourceReadyCallback thumbCallback = new IResourceReadyCallback() {
            @Override
            public void onResourceReady() {
                if (!hasThumbPlaying && isStartTargetView && !hasPhotoPlaying) {
                    startThumbAnimation();
                }
            }
        };
        if (!imageLoader.loadThumb(targetView.getContext(), url, thumbUrl, thumbImageView, thumbCallback)) {
            imageLoader.loadImage(targetView.getContext(), thumbUrl, thumbImageView, thumbCallback);
        }
    }

    float transX = 1;
    float transY = 1;

    /**
     * The thumb image has download completed,now we can play enterAnimation.
     * Usually thumb image is display faster than whole image,so we call loadThumb by imageLoader
     * after load whole image that if the whole image is already in cache and we can read it soon.
     * After the whole image display on photoView,this will cancel.
     * */
    private void startThumbAnimation() {
        hasThumbPlaying = true;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(thumbImageView, "scaleX", origin.width() / thumbWidth, 1f).setDuration(ImageBrowser.ANIMATION_DURATION);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(thumbImageView, "scaleY", origin.height() / thumbWidth, 1f).setDuration(ImageBrowser.ANIMATION_DURATION);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(thumbImageView, "translationX", (origin.centerX() - screenWidth / 2), 0f).setDuration(ImageBrowser.ANIMATION_DURATION);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(thumbImageView, "translationY", (origin.centerY() - screenHeight / 2), 0f).setDuration(ImageBrowser.ANIMATION_DURATION);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, translationX, translationY);
        animatorSet.start();
        translationX.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                if ((origin.centerX() - screenWidth / 2) == 0) {
                    transX = 0;
                } else {
                    transX = ViewCompat.getTranslationX(thumbImageView) / (origin.centerX() - screenWidth / 2);
                }
                if ((origin.centerY() - screenHeight / 2) == 0) {
                    transY = 0;
                } else {
                    transY = ViewCompat.getTranslationY(thumbImageView) / (origin.centerY() - screenHeight / 2);
                }
            }
        });
        translationX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressBar.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * The image has download completed,now we can play enterAnimation.
     * At first,photoView play translation animation,the distance decide by {@link #transX } and {@link #transY}
     * which means the thumb imageView has already played a little translation, it will start from where thumb view
     * cancel the animation.
     * And Second,we judge the origin view's ScaleType, if {@link #isCenterCrop} is true,the photo view will reset
     * it's Matrix and clipBounds,otherwise the photo view play scale animation together.
     * */
    public void startPhotoAnimation() {
        hasPhotoPlaying = true;
        photoView.preventOnTouchEvent();
        ObjectAnimator translationX = ObjectAnimator.ofFloat(photoView, "translationX", transX * (origin.centerX() - screenWidth / 2), 0f).setDuration(ImageBrowser.ANIMATION_DURATION);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(photoView, "translationY", transY * (origin.centerY() - screenHeight / 2), 0f).setDuration(ImageBrowser.ANIMATION_DURATION);
        AnimatorSet animatorSet = new AnimatorSet();
        if (isCenterCrop) {
            photoView.fromRectF(origin);
            photoView.clipFrom(origin.width(), origin.height());
            animatorSet.playTogether(translationX, translationY);
        } else {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(photoView, "scaleX", scale, 1f).setDuration(ImageBrowser.ANIMATION_DURATION);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(photoView, "scaleY", scale, 1f).setDuration(ImageBrowser.ANIMATION_DURATION);
            animatorSet.playTogether(scaleX, scaleY, translationX, translationY);
        }
        animatorSet.start();
        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mProgressBar.setVisibility(View.GONE);
                photoView.resumeOnTouchEvent();
            }
        });
    }

    /**
     * Dismiss ImageBrowser with animation.
     * */
    void endAnimation(final ImageBrowser fragment) {
        photoView.preventOnTouchEvent();
        if (!imageLoader.isDrawableLoadingCompleted(photoView)) {
            endThumbAnimation(fragment);
        } else {
            endPhotoAnimation(fragment);
        }

        fragment.playDismissAnimation();
    }

    /**
     * If the image loader loads the Image from net or cache completed,play photoView animation.
     * We'll judge the ScaleType of the origin ImageView by {@link #isCenterCrop},if the origin ScaleType is CENTER_CROP ,
     * photoView will reset it's Matrix and clipBounds and also play translation animation,otherwise we
     * only play translation and scale animation assuming that the origin ScaleType is CENTER_INSIDE.
     */
    private void endPhotoAnimation(final ImageBrowser fragment) {
        ObjectAnimator translationX = ObjectAnimator.ofFloat(photoView, "translationX", origin.centerX() - screenWidth / 2).setDuration(ImageBrowser.ANIMATION_DURATION);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(photoView, "translationY", origin.centerY() - screenHeight / 2).setDuration(ImageBrowser.ANIMATION_DURATION);
        AnimatorSet animatorSet = new AnimatorSet();
        if (isCenterCrop) {
            photoView.toRectF(origin);
            animatorSet.playTogether(translationX, translationY);
        } else {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(photoView, "scaleX", scale).setDuration(ImageBrowser.ANIMATION_DURATION);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(photoView, "scaleY", scale).setDuration(ImageBrowser.ANIMATION_DURATION);
            animatorSet.playTogether(scaleX, scaleY, translationX, translationY);
        }

        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fragment.onDismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    /**
     * If the image loader does not load the Image from net or cache yet,play thumb animation.
     * This animation just plays translation and scale.
     */
    private void endThumbAnimation(final ImageBrowser fragment) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(thumbImageView, "scaleX", origin.width() / thumbWidth).setDuration(ImageBrowser.ANIMATION_DURATION);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(thumbImageView, "scaleY", origin.height() / thumbWidth).setDuration(ImageBrowser.ANIMATION_DURATION);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(thumbImageView, "translationX", origin.left - thumbOriginLeft).setDuration(ImageBrowser.ANIMATION_DURATION);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(thumbImageView, "translationY", origin.top - thumbOriginTop).setDuration(ImageBrowser.ANIMATION_DURATION);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, translationX, translationY);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fragment.onDismiss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    public PhotoView getPhotoView() {
        return photoView;
    }

    public void clean() {
        this.imageLoader = null;
    }
}
