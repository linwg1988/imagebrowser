package linwg;

import android.graphics.RectF;
import android.view.View;
import android.widget.ImageView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by linwg on 2018/3/3.
 */

class RecyclerViewHelper {

    public static ViewRectFInfo measureChild(RecyclerView recyclerView, int imageViewId) {
        ViewRectFInfo viewRectFInfo = new ViewRectFInfo();
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager == null) {
            return null;
        }
        int viewCount = layoutManager.getChildCount();
        if (viewCount == 0) {
            return null;
        }
        int dataCount = layoutManager.getItemCount();
        int numColumns = 1;
        if (layoutManager instanceof GridLayoutManager) {
            numColumns = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof LinearLayoutManager) {
            numColumns = 1;
        }
        int firstVisiblePosition;
        int lastVisiblePosition;
        String scaleType = ImageView.ScaleType.MATRIX.name();
        if (imageViewId != 0) {
            //Maybe the imageView is contains by the itemView of ViewGroup ,we need measure offset.
            int[] imgLocate = new int[2];
            int[] itemLocate = new int[2];
            View itemView = layoutManager.getChildAt(0);
            View imageView = itemView.findViewById(imageViewId);
            if (imageView == null) {
                throw new IllegalStateException("The item of RecyclerView#" + recyclerView.getId() + " does not contains Id #" + imageViewId + ".");
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
            //The calculate result of these two method may be not equals viewCount,so wo just user the attached view's index replace visible position.
//            firstVisiblePosition = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
//            lastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();

            firstVisiblePosition = layoutManager.getPosition(layoutManager.getChildAt(0));
            lastVisiblePosition = layoutManager.getPosition(layoutManager.getChildAt(viewCount - 1));

            RectF firstChildRectF = getRecyclerItemRectFByIndex(layoutManager, 0, imageViewId);
            RectF lastChildRectF = getRecyclerItemRectFByIndex(layoutManager, viewCount - 1, imageViewId);
            int overLineCount = firstVisiblePosition / numColumns;
            float itemWidth = firstChildRectF.width() + Math.abs(viewRectFInfo.leftOffset) + Math.abs(viewRectFInfo.rightOffset);
            float itemHeight = firstChildRectF.height() + Math.abs(viewRectFInfo.topOffset) + Math.abs(viewRectFInfo.bottomOffset);
            for (int i = 0; i < firstVisiblePosition; i++) {
                int columnIndex = i % numColumns;
                RectF rectF = new RectF();
                rectF.left = firstChildRectF.left + columnIndex * itemHeight;
                rectF.right = firstChildRectF.right + columnIndex * itemHeight;
                rectF.top = firstChildRectF.top - overLineCount * itemWidth;
                rectF.bottom = firstChildRectF.bottom - overLineCount * itemWidth;
//                if (i != 0 && columnIndex == 0) {
//                    overLineCount--;
//                }
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
                    child = layoutManager.getChildAt(i - firstVisiblePosition).findViewById(imageViewId);
                } else {
                    child = layoutManager.getChildAt(i - firstVisiblePosition);
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
                    final View child = layoutManager.getChildAt(i).findViewById(imageViewId);
                    child.getLocationOnScreen(locate);
                    viewRectFInfo.imgLocations[i] = new ImageRectFInfo();
                    viewRectFInfo.imgLocations[i].rectF = new RectF(locate[0], locate[1], locate[0] + child.getWidth(), locate[1] + child.getHeight());
                    viewRectFInfo.imgLocations[i].scaleType = scaleType;
                } else {
                    final View child = layoutManager.getChildAt(i);
                    child.getLocationOnScreen(locate);
                    viewRectFInfo.imgLocations[i] = new ImageRectFInfo();
                    viewRectFInfo.imgLocations[i].rectF = new RectF(locate[0], locate[1], locate[0] + child.getWidth(), locate[1] + child.getHeight());
                    viewRectFInfo.imgLocations[i].scaleType = scaleType;
                }
            }
        }

        return viewRectFInfo;
    }

    private static RectF getRecyclerItemRectFByIndex(RecyclerView.LayoutManager parent, int i, int imageViewId) {
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

    public static void performScrollToBottom(RecyclerView parent, float offset, int position) {
        parent.smoothScrollToPosition(position);
    }

    public static void performScrollToTop(RecyclerView parent, float offset, int position) {
        parent.scrollToPosition(position);
    }

    public static View findChildByPosition(RecyclerView parent, int imageViewId, int position) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager == null) {
            return null;
        }
        View view = layoutManager.findViewByPosition(position);
        return view == null ? null : view.findViewById(imageViewId);
    }
}
