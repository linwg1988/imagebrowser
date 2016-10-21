package uk.co.senab.photoview;

import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.ImageView;

import linwg.ImageBrowser;


/**
 * Created by wengui on 2016/9/8.
 */
public class PhotoViewAttacherCompat extends PhotoViewAttacher {
    public PhotoViewAttacherCompat(ImageView imageView) {
        super(imageView);
    }

    public void toRectF(RectF target) {
        ImageView imageView = getImageView();
        if (imageView != null) {
            Drawable d = imageView.getDrawable();
            if (null != d) {
                int intrinsicHeight = d.getIntrinsicHeight();
                int intrinsicWidth = d.getIntrinsicWidth();

                int dwidth = intrinsicHeight;
                int dheight = intrinsicWidth;
                int vwidth = (int) target.width();
                int vheight = (int) target.height();
                float scale;
                float dx = 0, dy = 0;

                if (dwidth * vheight > vwidth * dheight) {
                    scale = (float) vheight / (float) dheight;
                    dx = (vwidth - dwidth * scale) * 0.5f;
                } else {
                    scale = (float) vwidth / (float) dwidth;
                    dy = (vheight - dheight * scale) * 0.5f;
                }

                float value = getScale();

                float curTranslationX = getValue(mSuppMatrix, Matrix.MTRANS_X);
                float curTranslationY = getValue(mSuppMatrix, Matrix.MTRANS_Y);

                new CropAnimator(value, scale, Math.round(curTranslationX), Math.round(curTranslationY), Math.round(dx), Math.round(dy)).start();
                ((PhotoView) imageView).clipTo(vwidth, vheight);
            }
        }
    }

    public class CropAnimator {
        ValueAnimator valueAnimator;

        public CropAnimator(final float currentZoom, final float targetZoom, final float currentDx, final float currentDy, final float dx, final float dy) {
            valueAnimator = ValueAnimator.ofInt(0, ImageBrowser.ANIMATION_DURATION);
            valueAnimator.setDuration(ImageBrowser.ANIMATION_DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int progress = (int) animation.getAnimatedValue();
                    float percent = ((float) progress) / ImageBrowser.ANIMATION_DURATION;
                    mSuppMatrix.setScale(currentZoom + (percent * (targetZoom - currentZoom)), currentZoom + (percent * (targetZoom - currentZoom)));
                    mSuppMatrix.postTranslate(currentDx + (percent * (dx - currentDx)), currentDy + (percent * (dy - currentDy)));
                    checkAndDisplayMatrix();
                }
            });
        }

        public void start() {
            valueAnimator.start();
        }
    }

    public void fromRectF(RectF target) {
        Drawable d = getImageView().getDrawable();
        int intrinsicHeight = d.getIntrinsicHeight();
        int intrinsicWidth = d.getIntrinsicWidth();

        int dwidth = intrinsicHeight;
        int dheight = intrinsicWidth;
        int vwidth = (int) target.width();
        int vheight = (int) target.height();
        float scale;
        float dx = 0, dy = 0;

        if (dwidth * vheight > vwidth * dheight) {
            scale = (float) vheight / (float) dheight;
            dx = (vwidth - dwidth * scale) * 0.5f;
        } else {
            scale = (float) vwidth / (float) dwidth;
            dy = (vheight - dheight * scale) * 0.5f;
        }

        float value = getScale();

        mSuppMatrix.setScale(scale, scale);
        mSuppMatrix.setTranslate(dx, dy);

        new CenterInsideAnimator(scale, 1, -Math.round(dx), -Math.round(dy)).start();
    }

    public class CenterInsideAnimator {
        ValueAnimator valueAnimator;

        public CenterInsideAnimator(final float currentZoom, final float targetZoom, final float dx, final float dy) {
            valueAnimator = ValueAnimator.ofInt(0, ImageBrowser.ANIMATION_DURATION);
            valueAnimator.setDuration(ImageBrowser.ANIMATION_DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int progress = (int) animation.getAnimatedValue();
                    float percent = ((float) progress) / ImageBrowser.ANIMATION_DURATION;
                    mSuppMatrix.setScale(currentZoom + (percent * (targetZoom - currentZoom)), currentZoom + (percent * (targetZoom - currentZoom)));
                    mSuppMatrix.postTranslate(percent * dx, percent * dy);
                    checkAndDisplayMatrix();
                }
            });
        }

        public void start() {
            valueAnimator.start();
        }
    }
}
