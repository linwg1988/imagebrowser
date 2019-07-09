package linwg;

import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by linwg on 2018/3/3.
 */

class ViewGroupHelper {

    static ViewRectFInfo measureChild(ViewGroup parent, int imageViewId) {
        int childCount = parent.getChildCount();
        ViewRectFInfo viewRectFInfo = new ViewRectFInfo();
        viewRectFInfo.imgLocations = new ImageRectFInfo[childCount];
        for (int i = 0; i < childCount; i++) {
            int[] locate = new int[2];
            if (imageViewId != 0) {
                final View child = parent.getChildAt(i).findViewById(imageViewId);
                child.getLocationOnScreen(locate);
                viewRectFInfo.imgLocations[i] = new ImageRectFInfo();
                viewRectFInfo.imgLocations[i].rectF = new RectF(locate[0], locate[1], locate[0] + child.getWidth(), locate[1] + child.getHeight());
                viewRectFInfo.imgLocations[i].scaleType = ((ImageView) child).getScaleType().name();
            } else {
                final View child = parent.getChildAt(i);
                child.getLocationOnScreen(locate);
                viewRectFInfo.imgLocations[i] = new ImageRectFInfo();
                viewRectFInfo.imgLocations[i].rectF = new RectF(locate[0], locate[1], locate[0] + child.getWidth(), locate[1] + child.getHeight());
                viewRectFInfo.imgLocations[i].scaleType = ImageView.ScaleType.MATRIX.name();
            }
        }
        return viewRectFInfo;
    }

    static ViewRectFInfo measureChild(ViewGroup parent, int[] imageViewId) {
        int childCount = imageViewId.length;
        ViewRectFInfo viewRectFInfo = new ViewRectFInfo();
        viewRectFInfo.imgLocations = new ImageRectFInfo[childCount];
        for (int i = 0; i < childCount; i++) {
            int[] locate = new int[2];
            final View child = parent.findViewById(imageViewId[i]);
            child.getLocationOnScreen(locate);
            viewRectFInfo.imgLocations[i] = new ImageRectFInfo();
            viewRectFInfo.imgLocations[i].rectF = new RectF(locate[0], locate[1], locate[0] + child.getWidth(), locate[1] + child.getHeight());
            viewRectFInfo.imgLocations[i].scaleType = ((ImageView) child).getScaleType().name();
        }
        return viewRectFInfo;
    }

    static View findChildByPosition(ViewGroup parent, int imageViewId, int position) {
        View child = parent.getChildAt(position);
        return child == null ? null : child.findViewById(imageViewId);
    }

    static View findChildByPosition(ViewGroup parent, int[] imageViewIds, int position) {
        return parent.findViewById(imageViewIds[position]);
    }
}
