package uk.co.senab.photoview;

import android.animation.ValueAnimator;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.widget.ImageView;


/**
 * Created by wengui on 2016/9/8.
 */
public class PhotoViewAttacherCompat extends PhotoViewAttacher {
    public PhotoViewAttacherCompat(ImageView imageView) {
        super(imageView);
    }

    public void postScale(RectF target) {
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
                new CropAnimator(value, scale, Math.round(dx), Math.round(dy)).start();
            }
        }
    }

    public class CropAnimator {
        ValueAnimator valueAnimator;
        private final static int DURATION = 200;

        public CropAnimator(final float currentZoom, final float targetZoom, final float dx, final float dy) {
            valueAnimator = ValueAnimator.ofInt(0, DURATION);
            valueAnimator.setDuration(DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int progress = (int) animation.getAnimatedValue();
                    float percent = ((float) progress) / DURATION;
                    mSuppMatrix.setScale(currentZoom + (percent * (targetZoom-currentZoom)), currentZoom+(percent * (targetZoom-currentZoom)));
                    mSuppMatrix.postTranslate(percent * dx, percent * dy);
                    checkAndDisplayMatrix();
                }
            });
        }

        public void start(){
            valueAnimator.start();
        }
    }
}
