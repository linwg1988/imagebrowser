package linwg;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.RectF;
import android.support.v4.app.DialogFragment;
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
    public static final int ANIMATION_DURATION = 200;
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
                endAnimation(browser);
            }
        });
    }

    public void startLoading() {
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

    private void startThumbAnimation() {
        hasThumbPlaying = true;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(thumbImageView, "scaleX", origin.width() / thumbImageView.getWidth(), 1f).setDuration(ANIMATION_DURATION);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(thumbImageView, "scaleY", origin.height() / thumbImageView.getHeight(), 1f).setDuration(ANIMATION_DURATION);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(thumbImageView, "translationX", (origin.centerX() - screenWidth / 2), 0f).setDuration(ANIMATION_DURATION);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(thumbImageView, "translationY", (origin.centerY() - screenHeight / 2), 0f).setDuration(ANIMATION_DURATION);
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
    }

    public void startPhotoAnimation() {
        hasPhotoPlaying = true;
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(photoView, "scaleX", scale, 1f).setDuration(ANIMATION_DURATION);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(photoView, "scaleY", scale, 1f).setDuration(ANIMATION_DURATION);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(photoView, "translationX", transX * (origin.centerX() - screenWidth / 2), 0f).setDuration(ANIMATION_DURATION);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(photoView, "translationY", transY * (origin.centerY() - screenHeight / 2), 0f).setDuration(ANIMATION_DURATION);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, translationX, translationY);
        animatorSet.start();
    }

    public void endAnimation(final ImageBrowser fragment) {
        if (imageLoader.isDrawableLoadingCompleted(photoView)) {
            endThumbAnimation(fragment);
        } else {
            endPhotoAnimation(fragment);
        }
        fragment.playDismissAnimation();
    }

    private void endPhotoAnimation(final DialogFragment fragment) {
        photoView.zoomTo(1, 0, 0);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(photoView, "translationX", origin.centerX() - screenWidth / 2).setDuration(ANIMATION_DURATION);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(photoView, "translationY", origin.centerY() - screenHeight / 2).setDuration(ANIMATION_DURATION);
        AnimatorSet animatorSet = new AnimatorSet();
        if (isCenterCrop) {
            photoView.scaleTypeChange(origin);
            animatorSet.playTogether(translationX, translationY);
        } else {
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(photoView, "scaleX", scale).setDuration(ANIMATION_DURATION);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(photoView, "scaleY", scale).setDuration(ANIMATION_DURATION);
            animatorSet.playTogether(scaleX,scaleY, translationX, translationY);
        }

        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fragment.dismissAllowingStateLoss();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }
        });
    }

    private void endThumbAnimation(final DialogFragment fragment) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(thumbImageView, "scaleX", origin.width() / thumbWidth).setDuration(ANIMATION_DURATION);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(thumbImageView, "scaleY", origin.height() / thumbWidth).setDuration(ANIMATION_DURATION);
        ObjectAnimator translationX = ObjectAnimator.ofFloat(thumbImageView, "translationX", origin.left - thumbOriginLeft).setDuration(ANIMATION_DURATION);
        ObjectAnimator translationY = ObjectAnimator.ofFloat(thumbImageView, "translationY", origin.top - thumbOriginTop).setDuration(ANIMATION_DURATION);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY, translationX, translationY);
        animatorSet.start();
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                fragment.dismissAllowingStateLoss();
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
