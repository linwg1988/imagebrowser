package uk.co.senab.photoview;

import android.animation.ValueAnimator;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import linwg.ImageBrowser;


/**
 * Created by wengui on 2016/9/8.
 */
public class PhotoViewAttacherCompat extends PhotoViewAttacher {
    public PhotoViewAttacherCompat(ImageView imageView) {
        super(imageView);
    }

    public void fromRectF(RectF target) {
        Drawable d = getImageView().getDrawable();
        int dwidth = d.getIntrinsicWidth();
        int dheight = d.getIntrinsicHeight();
        int vwidth = (int) target.width();
        int vheight = (int) target.height();
        float scale;
        float dx, dy;


        if (dwidth * vheight > vwidth * dheight) {
            scale = (float) vheight / (float) dheight;
        } else {
            scale = (float) vwidth / (float) dwidth;
        }

        //Calculate origin translation distance as target offset.
        dx = target.centerX() - (getImageView().getWidth() >> 1);
        dy = target.centerY() - (getImageView().getHeight() >> 1);
        mSuppMatrix.setScale(scale, scale, getImageView().getWidth() >> 1, getImageView().getHeight() >> 1);
        mSuppMatrix.postTranslate(dx, dy);
        checkAndDisplayMatrixByDrag();

        new InAnimator(scale, scale, 1, -Math.round(dx), -Math.round(dy), getImageView().getWidth() >> 1, getImageView().getHeight() >> 1).start();
    }

    public void toRectF(RectF target, long duration) {
        ImageView imageView = getImageView();
        if (imageView != null) {
            Drawable d = imageView.getDrawable();
            if (null != d) {
                int dwidth = d.getIntrinsicWidth();
                int dheight = d.getIntrinsicHeight();
                int vwidth = (int) target.width();
                int vheight = (int) target.height();
                float scale;
                float dx, dy;


                float value = getScale();

                float curTranslationX = getValue(mSuppMatrix, Matrix.MTRANS_X);
                float curTranslationY = getValue(mSuppMatrix, Matrix.MTRANS_Y);

                if (dwidth * vheight > vwidth * dheight) {
                    scale = (float) vheight / (float) dheight;
                } else {
                    scale = (float) vwidth / (float) dwidth;
                }
                //Calculate origin translation distance as target offset.
                dx = target.centerX() - (getImageView().getWidth() >> 1);
                dy = target.centerY() - (getImageView().getHeight() >> 1);
                mSuppMatrix.setScale(scale, scale, getImageView().getWidth() >> 1, getImageView().getHeight() >> 1);
                mSuppMatrix.postTranslate(dx, dy);

                dx = getValue(mSuppMatrix, Matrix.MTRANS_X);
                dy = getValue(mSuppMatrix, Matrix.MTRANS_Y);

                new OutAnimator(value, value, scale, scale, Math.round(curTranslationX), Math.round(curTranslationY), Math.round(dx), Math.round(dy), duration).start();
            }
        }
    }

    public void fromFitXYRectF(RectF target) {
        Drawable d = getImageView().getDrawable();
        int dwidth = d.getIntrinsicWidth();
        int dheight = d.getIntrinsicHeight();
        int vwidth = (int) target.width();
        int vheight = (int) target.height();
        float scaleX;
        float scaleY;
        //高超出
        if (dwidth * vheight > vwidth * dheight) {
            scaleY = (float) vheight / (float) dheight;
            scaleX = (float) vwidth / (float) dwidth;
        } else {
            //宽超出
            scaleX = (float) vwidth / (float) dwidth;
            scaleY = (float) vheight / (float) dheight;
        }
        mSuppMatrix.setScale(scaleX, scaleY, getImageView().getWidth() >> 1, getImageView().getHeight() >> 1);
        float dx, dy;

        //Calculate origin translation distance as target offset.
        dx = target.centerX() - (getImageView().getWidth() >> 1);
        dy = target.centerY() - (getImageView().getHeight() >> 1);
        mSuppMatrix.postTranslate(dx, dy);
        checkAndDisplayMatrixByDrag();
        new InAnimator(scaleX, scaleY, 1, -dx, -dy, getImageView().getWidth() >> 1, getImageView().getHeight() >> 1).start();
    }

    public void toFitCenterRectF(RectF target, long duration) {
        ImageView imageView = getImageView();
        if (imageView != null) {
            Drawable d = imageView.getDrawable();
            if (null != d) {
                int dwidth = d.getIntrinsicWidth();
                int dheight = d.getIntrinsicHeight();
                int vwidth = (int) target.width();
                int vheight = (int) target.height();

                float scale;
                float dx = 0, dy = 0;

                if (dwidth <= vwidth && dheight <= vheight) {
                    scale = 1.0f;
                } else {
                    scale = Math.min((float) vwidth / (float) dwidth,
                            (float) vheight / (float) dheight);
                }

                float value = getScale();

                float curTranslationX = getValue(mSuppMatrix, Matrix.MTRANS_X);
                float curTranslationY = getValue(mSuppMatrix, Matrix.MTRANS_Y);

                //Calculate origin translation distance as target offset.
                dx = target.centerX() - (getImageView().getWidth() >> 1);
                dy = target.centerY() - (getImageView().getHeight() >> 1);
                mSuppMatrix.setScale(scale, scale, getImageView().getWidth() >> 1, getImageView().getHeight() >> 1);
                mSuppMatrix.postTranslate(dx, dy);

                dx = getValue(mSuppMatrix, Matrix.MTRANS_X);
                dy = getValue(mSuppMatrix, Matrix.MTRANS_Y);

                new OutAnimator(value, value, scale, scale, Math.round(curTranslationX), Math.round(curTranslationY), Math.round(dx), Math.round(dy), duration).start();
            }
        }
    }

    public void fromFitCenterRectF(RectF target) {
        Drawable d = getImageView().getDrawable();
        int dwidth = d.getIntrinsicWidth();
        int dheight = d.getIntrinsicHeight();
        int vwidth = (int) target.width();
        int vheight = (int) target.height();
        float scale;
        float dx, dy;

        if (dwidth <= vwidth && dheight <= vheight) {
            scale = 1.0f;
        } else {
            scale = Math.min((float) vwidth / (float) dwidth,
                    (float) vheight / (float) dheight);
        }

        //Calculate origin translation distance as target offset.
        dx = target.centerX() - (getImageView().getWidth() >> 1);
        dy = target.centerY() - (getImageView().getHeight() >> 1);
        mSuppMatrix.setScale(scale, scale, getImageView().getWidth() >> 1, getImageView().getHeight() >> 1);
        mSuppMatrix.postTranslate(dx, dy);
        checkAndDisplayMatrixByDrag();

        new InAnimator(scale, scale, 1, -Math.round(dx), -Math.round(dy), getImageView().getWidth() >> 1, getImageView().getHeight() >> 1).start();
    }

    public void toFitXYRectF(RectF target, long duration) {
        ImageView imageView = getImageView();
        if (imageView != null) {
            Drawable d = imageView.getDrawable();
            if (null != d) {
                int dwidth = d.getIntrinsicWidth();
                int dheight = d.getIntrinsicHeight();
                int vwidth = (int) target.width();
                int vheight = (int) target.height();
                float scaleX;
                float scaleY;

                float curTranslationX = getValue(mSuppMatrix, Matrix.MTRANS_X);
                float curTranslationY = getValue(mSuppMatrix, Matrix.MTRANS_Y);

                if (dwidth * vheight > vwidth * dheight) {
                    //高超出
                    scaleY = (float) vheight / (float) dheight;
                    scaleX = (float) vwidth / (float) dwidth;
                } else {
                    //宽超出
                    scaleX = (float) vwidth / (float) dwidth;
                    scaleY = (float) vheight / (float) dheight;
                }

                float cx = getScale();
                float cy = getScaleY();

                float dx, dy;


                dx = target.centerX() - (getImageView().getWidth() >> 1);
                dy = target.centerY() - (getImageView().getHeight() >> 1);
                mSuppMatrix.setScale(scaleX, scaleY, getImageView().getWidth() >> 1, getImageView().getHeight() >> 1);
                mSuppMatrix.postTranslate(dx, dy);

                dx = getValue(mSuppMatrix, Matrix.MTRANS_X);
                dy = getValue(mSuppMatrix, Matrix.MTRANS_Y);

                new OutAnimator(cx, cy, scaleX, scaleY, curTranslationX, curTranslationY, dx, dy, duration).start();
            }
        }
    }

    public class InAnimator {
        ValueAnimator valueAnimator;

        public InAnimator(final float currentZoomX, final float currentZoomY, final float targetZoom, final float dx, final float dy, final float centerX, final float centerY) {
            valueAnimator = ValueAnimator.ofInt(0, ImageBrowser.ANIMATION_DURATION);
            valueAnimator.setDuration(ImageBrowser.ANIMATION_DURATION);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int progress = (int) animation.getAnimatedValue();
                    float percent = ((float) progress) / ImageBrowser.ANIMATION_DURATION;
                    mSuppMatrix.setScale(currentZoomX + (percent * (targetZoom - currentZoomX)), currentZoomY + (percent * (targetZoom - currentZoomY)), centerX, centerY);
                    mSuppMatrix.postTranslate(-(1 - percent) * dx, -(1 - percent) * dy);
                    checkAndDisplayMatrixByDrag();
                }
            });
        }

        public void start() {
            valueAnimator.start();
        }
    }

    public class OutAnimator {
        ValueAnimator valueAnimator;

        public OutAnimator(final float currentZoomX, final float currentZoomY, final float targetZoomX, final float targetZoomY,
                           final float currentDx, final float currentDy,
                           final float dx, final float dy, final long duration) {
            valueAnimator = ValueAnimator.ofInt(0, (int) duration);
            valueAnimator.setDuration(duration);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int progress = (int) animation.getAnimatedValue();
                    float percent = ((float) progress) / duration;
                    mSuppMatrix.setTranslate(currentDx + (percent * (dx - currentDx)), currentDy + (percent * (dy - currentDy)));
                    mSuppMatrix.preScale(currentZoomX + (percent * (targetZoomX - currentZoomX)), currentZoomY + (percent * (targetZoomY - currentZoomY)), 0, 0);
                    checkAndDisplayMatrixByDrag();
                }
            });
        }

        public void start() {
            valueAnimator.start();
        }
    }
}
