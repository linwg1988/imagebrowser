package linwg;

import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by linwg on 2018/3/3.
 */

class ViewGroupHelper {

    public static ViewRectFInfo measureChild(ViewGroup parent, int imageViewId) {
        int childCount = parent.getChildCount();
        ViewRectFInfo viewRectFInfo = new ViewRectFInfo();
        viewRectFInfo.imgLocations = new RectF[childCount];
        for (int i = 0; i < childCount; i++) {
            int[] locate = new int[2];
            if (imageViewId != 0) {
                final View child = parent.getChildAt(i).findViewById(imageViewId);
                child.getLocationOnScreen(locate);
                viewRectFInfo.imgLocations[i] = new RectF(locate[0], locate[1], locate[0] + child.getWidth(), locate[1] + child.getHeight());
            } else {
                final View child = parent.getChildAt(i);
                child.getLocationOnScreen(locate);
                viewRectFInfo.imgLocations[i] = new RectF(locate[0], locate[1], locate[0] + child.getWidth(), locate[1] + child.getHeight());
            }
        }
        return viewRectFInfo;
    }

    public static View findChildByPosition(ViewGroup parent, int imageViewId, int position) {
        View child = parent.getChildAt(position);
        return child == null ? null : child.findViewById(imageViewId);
    }
}
