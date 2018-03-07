/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package uk.co.senab.photoview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.ImageView;

import linwg.ImageBrowser;
import uk.co.senab.photoview.PhotoViewAttacher.OnMatrixChangedListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnPhotoTapListener;
import uk.co.senab.photoview.PhotoViewAttacher.OnViewTapListener;

public class PhotoView extends AppCompatImageView implements IPhotoView {

    private final PhotoViewAttacherCompat mAttacher;

    private ScaleType mPendingScaleType;

    public PhotoView(Context context) {
        this(context, null);
    }

    public PhotoView(Context context, AttributeSet attr) {
        this(context, attr, 0);
    }

    public PhotoView(Context context, AttributeSet attr, int defStyle) {
        super(context, attr, defStyle);
        super.setScaleType(ScaleType.MATRIX);
        mAttacher = new PhotoViewAttacherCompat(this);

        if (null != mPendingScaleType) {
            setScaleType(mPendingScaleType);
            mPendingScaleType = null;
        }
    }

    @Override
    public boolean canZoom() {
        return mAttacher.canZoom();
    }

    @Override
    public RectF getDisplayRect() {
        return mAttacher.getDisplayRect();
    }

    @Override
    public float getMinScale() {
        return mAttacher.getMinScale();
    }

    @Override
    public float getMidScale() {
        return mAttacher.getMidScale();
    }

    @Override
    public float getMaxScale() {
        return mAttacher.getMaxScale();
    }

    @Override
    public float getScale() {
        return mAttacher.getScale();
    }

    @Override
    public ScaleType getScaleType() {
        return mAttacher.getScaleType();
    }

    @Override
    public void setAllowParentInterceptOnEdge(boolean allow) {
        mAttacher.setAllowParentInterceptOnEdge(allow);
    }

    @Override
    public void setMinScale(float minScale) {
        mAttacher.setMinScale(minScale);
    }

    @Override
    public void setMidScale(float midScale) {
        mAttacher.setMidScale(midScale);
    }

    @Override
    public void setMaxScale(float maxScale) {
        mAttacher.setMaxScale(maxScale);
    }

    @Override
    // setImageBitmap calls through to this method
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }

    @Override
    public void setImageURI(Uri uri) {
        super.setImageURI(uri);
        if (null != mAttacher) {
            mAttacher.update();
        }
    }

    @Override
    public void setOnMatrixChangeListener(OnMatrixChangedListener listener) {
        mAttacher.setOnMatrixChangeListener(listener);
    }

    @Override
    public void setOnLongClickListener(OnLongClickListener l) {
        mAttacher.setOnLongClickListener(l);
    }

    @Override
    public void setOnPhotoTapListener(OnPhotoTapListener listener) {
        mAttacher.setOnPhotoTapListener(listener);
    }

    @Override
    public void setOnViewTapListener(OnViewTapListener listener) {
        mAttacher.setOnViewTapListener(listener);
    }

    @Override
    public void setScaleType(ScaleType scaleType) {
        if (null != mAttacher) {
            mAttacher.setScaleType(scaleType);
        } else {
            mPendingScaleType = scaleType;
        }
    }

    @Override

    public void setZoomable(boolean zoomable) {
        mAttacher.setZoomable(zoomable);
    }

    @Override
    public void zoomTo(float scale, float focalX, float focalY) {
        mAttacher.zoomTo(scale, focalX, focalY);
    }

    @Override
    protected void onDetachedFromWindow() {
        mAttacher.cleanup();
        super.onDetachedFromWindow();
    }

    public void toRectF(RectF rectF) {
        mAttacher.toRectF(rectF);
    }

    public void toFitXYRectF(RectF rectF) {
        mAttacher.toFitXYRectF(rectF);
    }

    public void toFitCenterRectF(RectF rectF) {
        mAttacher.toFitCenterRectF(rectF);
    }

    public void fromRectF(RectF rectF) {
        mAttacher.fromRectF(rectF);
    }

    public void fromFitXYRectF(RectF rectF) {
        mAttacher.fromFitXYRectF(rectF);
    }

    public void fromFitCenterRectF(RectF rectF) {
        mAttacher.fromFitCenterRectF(rectF);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        Matrix imageMatrix = getImageMatrix();
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();
        int scrollX = getScrollX();
        int scrollY = getScrollY();

        if (drawable == null) {
            return;
        }
        if (imageMatrix == null && paddingLeft == 0 && paddingTop == 0) {
            drawable.draw(canvas);
        } else {
            final int saveCount = canvas.getSaveCount();
            canvas.save();

            if (rect != null) {
                canvas.clipRect(rect);
            }


            canvas.translate(paddingLeft, paddingTop);
            if (imageMatrix != null) {
                canvas.concat(imageMatrix);
            }
            drawable.draw(canvas);
            canvas.restoreToCount(saveCount);
        }
    }

    RectF rect;

    public void clipTo(final int width, final int height) {
        final int vWidth = getWidth();
        final int vHeight = getHeight();
        rect = new RectF();
        rect.right = vWidth;
        rect.bottom = vHeight;
        final int widthOffset = (vWidth - width) / 2;
        final int heightOffset = (vHeight - height) / 2;


        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(ImageBrowser.ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (float) animation.getAnimatedValue();
                rect.left = (int) (widthOffset * f);
                rect.top = (int) (heightOffset * f);
                rect.right = vWidth - rect.left;
                rect.bottom = vHeight - rect.top;
                invalidate();
            }
        });
        valueAnimator.start();
    }

    public void clipFrom(final float width, final float height) {
        final int vWidth = getWidth();
        final int vHeight = getHeight();
        rect = new RectF();
        rect.left = (vWidth - width) / 2;
        rect.right = rect.left + width;
        rect.top = (vHeight - height) / 2;
        rect.bottom = rect.top + height;
        final float widthOffset = (vWidth - width) / 2;
        final float heightOffset = (vHeight - height) / 2;


        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration(ImageBrowser.ANIMATION_DURATION);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float f = (float) animation.getAnimatedValue();
                rect.left -= (int) (widthOffset * f);
                rect.top -= (int) (heightOffset * f);
                rect.right += (widthOffset * f);
                rect.bottom += (heightOffset * f);
                invalidate();
            }
        });
        valueAnimator.start();
    }

    public void preventOnTouchEvent() {
        mAttacher.preventOnTouchEvent();
    }

    public void resumeOnTouchEvent() {
        mAttacher.resumeOnTouchEvent();
    }
}