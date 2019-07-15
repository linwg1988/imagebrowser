package linwg;

import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;

public class ViewHelper {
    static ViewRectFInfo measureChild(View child) {
        ViewRectFInfo viewRectFInfo = new ViewRectFInfo();
        viewRectFInfo.imgLocations = new ImageRectFInfo[1];
        int[] locate = new int[2];
        child.getLocationOnScreen(locate);
        viewRectFInfo.imgLocations[0] = new ImageRectFInfo();
        viewRectFInfo.imgLocations[0].rectF = new RectF(locate[0], locate[1], locate[0] + child.getWidth(), locate[1] + child.getHeight());
        viewRectFInfo.imgLocations[0].scaleType = ImageView.ScaleType.MATRIX.name();
        return viewRectFInfo;
    }
}
