package linwg;

import android.graphics.RectF;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;

/**
 * Created by linwg on 2018/3/3.
 */

class AbsListViewHelper {

    public static ViewRectFInfo measureChild(AbsListView parent, int imageViewId) {
        ViewRectFInfo viewRectFInfo = new ViewRectFInfo();
        final int viewCount = parent.getChildCount();
        int dataCount = parent.getAdapter().getCount();
        int numColumns = 0;
        if (parent instanceof GridView) {
            numColumns = ((GridView) parent).getNumColumns();
        } else if (parent instanceof ListView) {
            numColumns = 1;
        }

        String scaleType = ImageView.ScaleType.MATRIX.name();
        if (imageViewId != 0) {
            //Maybe the imageView is contains by the itemView of ViewGroup ,we need measure offset.
            int[] imgLocate = new int[2];
            int[] itemLocate = new int[2];
            View imageView = parent.getChildAt(0);
            View itemView = parent.getChildAt(0).findViewById(imageViewId);
            if (imageView == null) {
                throw new IllegalStateException("The item of AbsListView#" + parent.getId() + " does not contains Id #" + imageViewId + ".");
            }
            scaleType = ((ImageView) imageView).getScaleType().name();
            imageView.getLocationOnScreen(imgLocate);
            itemView.getLocationOnScreen(itemLocate);

            RectF itemRect = new RectF(itemLocate[0], itemLocate[1], itemLocate[0] + itemView.getWidth(), itemLocate[1] + itemView.getHeight());
            RectF imgRect = new RectF(imgLocate[0], imgLocate[1], imgLocate[0] + imageView.getWidth(), imgLocate[1] + imageView.getHeight());
            viewRectFInfo.leftOffset = itemRect.left - imgRect.left;
            viewRectFInfo.topOffset = itemRect.top - imgRect.top;
            viewRectFInfo.rightOffset = itemRect.right - imgRect.right;
            viewRectFInfo.bottomOffset = itemRect.bottom - imgRect.bottom;
        }

        if (dataCount > viewCount) {
            viewRectFInfo.imgLocations = new ImageRectFInfo[dataCount];

            int firstVisiblePosition = parent.getFirstVisiblePosition();
            int lastVisiblePosition = parent.getLastVisiblePosition();
            if (lastVisiblePosition + 1 - firstVisiblePosition != viewCount) {
                throw new IllegalArgumentException("The parent view is an AbsListView,but the adapter does not make items recycling.");
            }

            RectF firstChildRectF = getRectFByIndex(parent, 0, imageViewId);
            RectF lastChildRectF = getRectFByIndex(parent, viewCount - 1, imageViewId);
//            float itemWidth = firstChildRectF.width() + Math.abs(viewRectFInfo.leftOffset) + Math.abs(viewRectFInfo.rightOffset);
//            float itemHeight = firstChildRectF.height() + Math.abs(viewRectFInfo.topOffset) + Math.abs(viewRectFInfo.bottomOffset);
            //TODO If parent is GridView ,the itemHeight should be calculate correct(Current is not correct really).
            int lineCount = dataCount / numColumns + dataCount % numColumns == 0 ? 0 : 1;
            float itemWidth = Math.max(parent.getWidth() / numColumns, firstChildRectF.width() + Math.abs(viewRectFInfo.leftOffset) + Math.abs(viewRectFInfo.rightOffset));
            float itemHeight = Math.max(firstChildRectF.height() + Math.abs(viewRectFInfo.topOffset) + Math.abs(viewRectFInfo.bottomOffset), 0);

            int overLineCount = firstVisiblePosition / numColumns;
            for (int i = 0; i < firstVisiblePosition; i++) {
                int columnIndex = i % numColumns;
                RectF rectF = new RectF();
                rectF.left = firstChildRectF.left + columnIndex * itemWidth;
                rectF.right = firstChildRectF.right + columnIndex * itemWidth;
                rectF.top = firstChildRectF.top - itemHeight * overLineCount;
                rectF.bottom = firstChildRectF.bottom - itemHeight * overLineCount;
                if ((numColumns > 1 && (i != 0 && columnIndex == 0)) || (numColumns == 1)) {
                    overLineCount--;
                }
                viewRectFInfo.imgLocations[i] = new ImageRectFInfo();
                viewRectFInfo.imgLocations[i].rectF = rectF;
                viewRectFInfo.imgLocations[i].scaleType = scaleType;
            }
            for (int i = firstVisiblePosition; i < lastVisiblePosition + 1; i++) {
                int[] locate = new int[2];
                final View child;
                if (imageViewId != 0) {
                    child = parent.getChildAt(i - firstVisiblePosition).findViewById(imageViewId);
                } else {
                    child = parent.getChildAt(i - firstVisiblePosition);
                }
                child.getLocationOnScreen(locate);
                RectF rectF = new RectF(locate[0], locate[1], locate[0] + child.getWidth(), locate[1] + child.getHeight());
                viewRectFInfo.imgLocations[i] = new ImageRectFInfo();
                viewRectFInfo.imgLocations[i].rectF = rectF;
                viewRectFInfo.imgLocations[i].scaleType = scaleType;
            }

            int futureLineCount = 0;
            for (int i = lastVisiblePosition + 1; i < dataCount; i++) {
                int columnIndex = i % numColumns;
                if (columnIndex == 0) {
                    futureLineCount++;
                }
                RectF rectF = new RectF();
                rectF.left = lastChildRectF.left - (numColumns - columnIndex - 1) * itemWidth;
                rectF.right = lastChildRectF.right - (numColumns - columnIndex - 1) * itemWidth;
                rectF.top = lastChildRectF.top + itemHeight * futureLineCount;
                rectF.bottom = lastChildRectF.bottom + itemHeight * futureLineCount;
                viewRectFInfo.imgLocations[i] = new ImageRectFInfo();
                viewRectFInfo.imgLocations[i].rectF = rectF;
                viewRectFInfo.imgLocations[i].scaleType = scaleType;
            }
        } else {
            viewRectFInfo.imgLocations = new ImageRectFInfo[viewCount];
            for (int i = 0; i < viewCount; i++) {
                int[] locate = new int[2];
                if (imageViewId != 0) {
                    final View child = parent.getChildAt(i).findViewById(imageViewId);
                    child.getLocationOnScreen(locate);
                    viewRectFInfo.imgLocations[i] = new ImageRectFInfo();
                    viewRectFInfo.imgLocations[i].rectF = new RectF(locate[0], locate[1], locate[0] + child.getWidth(), locate[1] + child.getHeight());
                    viewRectFInfo.imgLocations[i].scaleType = scaleType;
                } else {
                    final View child = parent.getChildAt(i);
                    child.getLocationOnScreen(locate);
                    viewRectFInfo.imgLocations[i] = new ImageRectFInfo();
                    viewRectFInfo.imgLocations[i].rectF = new RectF(locate[0], locate[1], locate[0] + child.getWidth(), locate[1] + child.getHeight());
                    viewRectFInfo.imgLocations[i].scaleType = scaleType;
                }
            }
        }
        return viewRectFInfo;
    }

    private static RectF getRectFByIndex(ViewGroup parent, int i, int imageViewId) {
        int[] locate = new int[2];
        final View child;
        if (imageViewId != 0) {
            child = parent.getChildAt(i).findViewById(imageViewId);
        } else {
            child = parent.getChildAt(i);
        }
        child.getLocationOnScreen(locate);
        return new RectF(locate[0], locate[1], locate[0] + child.getWidth(), locate[1] + child.getHeight());
    }

    public static void performScrollToBottom(AbsListView parent, float offset, int position) {
        parent.smoothScrollBy((int) offset, 0);
    }

    public static void performScrollToTop(AbsListView parent, float offset, int position) {
        parent.smoothScrollBy((int) -offset, 0);
    }

    public static View findChildByPosition(AbsListView parent, int imageViewId, int position) {
        int firstVisiblePosition = parent.getFirstVisiblePosition();
        return parent.getChildAt(position - firstVisiblePosition).findViewById(imageViewId);
    }
}
