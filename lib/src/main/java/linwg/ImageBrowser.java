package linwg;

import android.animation.ObjectAnimator;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;

import linwg.org.lib.R;
import linwg.strategy.ImageLoaderFactory;
import linwg.view.CirclePageIndicator;
import linwg.view.HackyViewPager;
import uk.co.senab.photoview.PhotoView;


/**
 * ImageBrowser Created by wengui on 2016/3/9.
 */
public class ImageBrowser extends DialogFragment {
    /**
     * The key of the {@link #mode}
     */
    private static final String IB_MODE = "ib_mode";
    /**
     * The key of the {@link #thumbSize}
     */
    private static final String IB_THUMB_SIZE = "ib_thumb_size";
    /**
     * The key of the {@link #imageUrls}
     */
    private static final String IB_URLS = "ib_urls";
    /**
     * The key of the {@link #position}
     */
    private static final String IB_POSITION = "ib_position";
    /**
     * The key of the {@link #thumbUrls}
     */
    private static final String IB_THUMB_URLS = "ib_thumb_urls";
    /**
     * The key of the {@link #rectFs}
     */
    private static final String IB_LOCATIONS = "ib_locations";
    /**
     * The key of the {@link #customImgRes}
     */
    private static final String IB_CUSTOM_IMG_RES = "ib_custom_img_res";
    /**
     * The key of the {@link #customTextRes}
     */
    private static final String IB_CUSTOM_TEXT_RES = "ib_custom_text_res";
    /**
     * The key of the {@link #customChar}
     */
    private static final String IB_CUSTOM_TEXT = "ib_custom_text";
    /**
     * The key of the {@link #isCenterCrop}
     */
    private static final String IB_IS_ORIGIN_CENTER_CROP = "ib_is_origin_center_crop";

    private static final int DEFAULT_THUMB_SIZE = 100;
    /**
     * There is nothing to explain!
     */
    int screenWidth;
    /**
     * There is nothing to explain,too!
     */
    int screenHeight;
    /**
     * The showing position.
     */
    private int position;
    private ArrayList<String> imageUrls;
    private ArrayList<String> thumbUrls;
    /**
     * The origin viewGroup provide this,if is not null, this array's length is always equals imageView's size.
     */
    private RectF[] rectFs;
    private int customImgRes;
    private int customTextRes;
    /**
     * This is always setting by the origin view's size.The default size is 100dp{@link #DEFAULT_THUMB_SIZE}.
     */
    private int thumbSize;
    /**
     * If true,the {@link WrapImageView} will run matrix animation,otherwise scale the photoView.
     */
    boolean isCenterCrop;
    private CharSequence customChar;
    /**
     * There are four {@link Mode} use for this param,use {@link Mode#DOWNLOAD} or{@link Mode#DELETE}
     * mode will show you the corresponding implementation,you can set {@link Builder#setOnDownloadClickListener(OnDownloadClickListener)}
     * and {@link #setOnDownloadClickListener(OnDownloadClickListener)} if you want to do something after that happens.
     * Except the above modes,if you want modify you own image button or text button and it's click listener,just call
     * {@link Builder#customImg(int, View.OnClickListener)} or {@link Builder#customText(CharSequence, View.OnClickListener)}.
     */
    private int mode = Mode.NONE;

    private ArrayList<WrapImageView> viewList = new ArrayList<WrapImageView>();

    private ImageView ivDownLoad;
    private ImagePagerAdapter imagePagerAdapter;
    private CirclePageIndicator circlePageIndicator;
    private View shadowView;
    private ImageView ivDelete;
    private ImageView ivCustom;
    private TextView tvCustom;

    public ImageBrowser() {
        setStyle(STYLE_NO_FRAME, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mode = savedInstanceState.getInt(IB_MODE);
            thumbSize = savedInstanceState.getInt(IB_THUMB_SIZE);
            imageUrls = savedInstanceState.getStringArrayList(IB_URLS);
            position = savedInstanceState.getInt(IB_POSITION);
            thumbUrls = savedInstanceState.getStringArrayList(IB_THUMB_URLS);
            rectFs = (RectF[]) savedInstanceState.getParcelableArray(IB_LOCATIONS);
            customImgRes = savedInstanceState.getInt(IB_CUSTOM_IMG_RES);
            customTextRes = savedInstanceState.getInt(IB_CUSTOM_TEXT_RES);
            customChar = savedInstanceState.getCharSequence(IB_CUSTOM_TEXT);
            isCenterCrop = savedInstanceState.getBoolean(IB_IS_ORIGIN_CENTER_CROP);
        } else {
            final Bundle arguments = getArguments();
            mode = arguments.getInt(IB_MODE);
            thumbSize = arguments.getInt(IB_THUMB_SIZE);
            imageUrls = arguments.getStringArrayList(IB_URLS);
            position = arguments.getInt(IB_POSITION);
            thumbUrls = arguments.getStringArrayList(IB_THUMB_URLS);
            rectFs = (RectF[]) arguments.getParcelableArray(IB_LOCATIONS);
            customImgRes = arguments.getInt(IB_CUSTOM_IMG_RES);
            customTextRes = arguments.getInt(IB_CUSTOM_TEXT_RES);
            customChar = arguments.getCharSequence(IB_CUSTOM_TEXT);
            isCenterCrop = arguments.getBoolean(IB_IS_ORIGIN_CENTER_CROP);
        }
        if (thumbSize == 0) {
            thumbSize = (int) Util.dp2px(getActivity(), DEFAULT_THUMB_SIZE);
        }
        screenWidth = Util.getScreenWidth(getActivity());
        screenHeight = Util.getScreenHeight(getActivity());
        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    viewList.get(position).endAnimation(ImageBrowser.this);
                    return true;
                }
                return false;
            }
        });
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return inflater.inflate(R.layout.fragment_iamge_browser, null);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(IB_URLS, imageUrls);
        outState.putStringArrayList(IB_THUMB_URLS, thumbUrls);
        outState.putInt(IB_POSITION, position);
        outState.putInt(IB_MODE, mode);
        outState.putInt(IB_THUMB_SIZE, thumbSize);
        outState.putInt(IB_CUSTOM_IMG_RES, customImgRes);
        outState.putInt(IB_CUSTOM_TEXT_RES, customTextRes);
        outState.putCharSequence(IB_CUSTOM_TEXT, customChar);
        outState.putParcelableArray(IB_LOCATIONS, rectFs);
        outState.putBoolean(IB_IS_ORIGIN_CENTER_CROP, isCenterCrop);
    }

    public void playDismissAnimation() {
        ObjectAnimator.ofFloat(shadowView, "alpha", 0.2f).setDuration(200).start();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        shadowView = view.findViewById(R.id.shadow);
        ivDownLoad = (ImageView) view.findViewById(R.id.ivDownLoad);
        ivDelete = (ImageView) view.findViewById(R.id.ivDelete);
        ivCustom = (ImageView) view.findViewById(R.id.ivCustom);
        tvCustom = (TextView) view.findViewById(R.id.tvCustom);
        ObjectAnimator.ofFloat(shadowView, "alpha", 1f).setDuration(200).start();

        viewList.clear();
        for (int i = 0; i < imageUrls.size(); i++) {
            View imageLayout = getActivity().getLayoutInflater().inflate(R.layout.item_image_browser, null);
            final View iv_thumbnail = imageLayout.findViewById(R.id.iv_thumbnail);
            final ViewGroup.LayoutParams params = iv_thumbnail.getLayoutParams();
            params.height = thumbSize;
            params.width = thumbSize;
            iv_thumbnail.setLayoutParams(params);
            WrapImageView wrapImageView = new WrapImageView(this, imageLayout, imageUrls.get(i), thumbUrls == null ? null : thumbUrls.get(i),
                    rectFs == null ? null : rectFs[i], i == position, screenWidth, screenHeight, isCenterCrop);
            viewList.add(wrapImageView);
        }

        switch (mode) {
            case Mode.CUSTOM:
                if (customImgRes != 0) {
                    ivCustom.setVisibility(View.VISIBLE);
                    ivCustom.setOnClickListener(mCustomImgClickListener);
                }
                if (customTextRes != 0) {
                    tvCustom.setVisibility(View.VISIBLE);
                    tvCustom.setText(customTextRes);
                    tvCustom.setOnClickListener(mCustomTxtClickListener);
                }
                if (!TextUtils.isEmpty(customChar)) {
                    tvCustom.setVisibility(View.VISIBLE);
                    tvCustom.setText(customChar);
                    tvCustom.setOnClickListener(mCustomTxtClickListener);
                }
                break;
            case Mode.NONE:
                ivDownLoad.setVisibility(View.GONE);
                ivDelete.setVisibility(View.GONE);
                ivCustom.setVisibility(View.GONE);
                tvCustom.setVisibility(View.GONE);
                break;
            case Mode.DELETE:
                ivDelete.setVisibility(View.VISIBLE);
                ivDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnDeleteClickListener != null) {
                            new AlertDialog.Builder(getActivity()).setTitle(R.string.delete_alert)
                                    .setMessage(R.string.delete_message).setPositiveButton(R.string.sure, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    removeCurrent();
                                    if (imageUrls.size() == 0) {
                                        ImageBrowser.this.dismiss();
                                    }
                                    dialog.cancel();
                                }
                            }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            }).create().show();
                        }
                    }
                });
                break;
            case Mode.DOWNLOAD:
                ivDownLoad.setVisibility(View.VISIBLE);
                ivDownLoad.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (mOnDownloadClickListener != null) {
                            final PhotoView view = viewList.get(position).getPhotoView();
                            if (ImageLoaderFactory.get().isDrawableLoadingCompleted(view)) {
                                mOnDownloadClickListener.onDownloadBtnClick(ImageLoaderFactory.get().getBitmapFromImageView(view));
                            }
                        }
                    }
                });
                break;
        }

        HackyViewPager mViewPager = (HackyViewPager) view.findViewById(R.id.vp_image);
        mViewPager.setOffscreenPageLimit(1);
        imagePagerAdapter = new ImagePagerAdapter();
        mViewPager.setAdapter(imagePagerAdapter);
        mViewPager.setCurrentItem(position);

        circlePageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(mViewPager);
        circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                ImageBrowser.this.position = position;
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    /**
     * Remove the current showing imageView and url.
     */
    public void removeCurrent() {
        final String url = imageUrls.get(position);
        mOnDeleteClickListener.onClick(position, url);
        imagePagerAdapter.notifyDataSetChanged();
    }

    public static class Mode {
        public static final int NONE = -1;
        public static final int DOWNLOAD = -2;
        public static final int DELETE = -3;
        public static final int CUSTOM = -4;
    }

    public static class Builder {
        private int mode = Mode.NONE;
        private ArrayList<String> urls;
        private int position;
        private ArrayList<String> thumbUrls;
        private int thumbSize;
        RectF[] locations;
        ViewGroup parent;
        private OnDownloadClickListener downloadListener;
        private OnDeleteClickListener deleteListener;
        private View.OnClickListener customImgListener;
        private View.OnClickListener customTxtListener;
        private int customImgRes;
        private int customTextRes;
        private CharSequence customChar;
        private boolean isCenterCrop;
        private View child;

        public Builder() {
        }

        public Builder mode(int mode) {
            this.mode = mode;
            return this;
        }

        public Builder url(String url) {
            if (urls == null)
                urls = new ArrayList<>();
            urls.clear();
            urls.add(url);
            position = 0;
            return this;
        }

        public Builder thumbUrl(String thumbUrl) {
            if (thumbUrls == null)
                thumbUrls = new ArrayList<>();
            thumbUrls.clear();
            thumbUrls.add(thumbUrl);
            return this;
        }

        public Builder urls(ArrayList<String> urls) {
            this.urls = urls;
            return this;
        }

        public Builder thumbUrls(ArrayList<String> thumbUrls) {
            this.thumbUrls = thumbUrls;
            return this;
        }

        public Builder position(int index) {
            this.position = index;
            return this;
        }

        public Builder thumbSize(int thumbSize) {
            this.thumbSize = thumbSize;
            return this;
        }

        public Builder targetParent(ViewGroup parent) {
            this.parent = parent;
            return this;
        }

        /**
         * Once I call this is want to set the origin view's alpha so the animation will be playing like the origin view is
         * really moving.But it works bad,so just ignore this method. (ps:HaHa...I just suppose one day I'll make it work,so I didn't delete it.)
         */
        @Deprecated
        public Builder target(View child) {
            this.child = child;
            return this;
        }

        public Builder setDownloadListener(OnDownloadClickListener listener) {
            mode = Mode.DOWNLOAD;
            this.downloadListener = listener;
            return this;
        }

        public Builder setDeleteListener(OnDeleteClickListener listener) {
            mode = Mode.DELETE;
            this.deleteListener = listener;
            return this;
        }

        public Builder customText(int resId, View.OnClickListener clickListener) {
            mode = Mode.CUSTOM;
            this.customTextRes = resId;
            this.customTxtListener = clickListener;
            return this;
        }

        public Builder customImg(int resId, View.OnClickListener clickListener) {
            mode = Mode.CUSTOM;
            this.customImgRes = resId;
            this.customImgListener = clickListener;
            return this;
        }

        public Builder customText(CharSequence c, View.OnClickListener clickListener) {
            mode = Mode.CUSTOM;
            this.customChar = c;
            this.customTxtListener = clickListener;
            return this;
        }

        /**
         * @param isCenterCrop If the origin imageView's ScaleType is CENTER_CROP, set this true;
         */
        public Builder originIsCenterCrop(boolean isCenterCrop) {
            this.isCenterCrop = isCenterCrop;
            return this;
        }

        public ImageBrowser build() {
            if (parent != null) {
                final int childCount = parent.getChildCount();
                if (parent instanceof AbsListView) {
                    int count = ((AbsListView) parent).getAdapter().getCount();
                    int numColumns = 0;
                    if (parent instanceof GridView) {
                        numColumns = ((GridView) parent).getNumColumns();
                    } else if (parent instanceof ListView) {
                        numColumns = 1;
                    }

                    if (count > childCount) {
                        this.locations = new RectF[count];
                        int firstVisiblePosition = ((AbsListView) parent).getFirstVisiblePosition();
                        int lastVisiblePosition = ((AbsListView) parent).getLastVisiblePosition();
                        if (lastVisiblePosition + 1 - firstVisiblePosition != childCount) {
                            throw new IllegalArgumentException("The parent view is an AbsListView,but the adapter does not make items recycling.");
                        }

                        RectF firstChildRectF = getRectFByIndex(parent, 0);
                        RectF lastChildRectF = getRectFByIndex(parent, childCount - 1);
                        int overLineCount = firstVisiblePosition / numColumns;
                        for (int i = 0; i < firstVisiblePosition; i++) {
                            int columnIndex = i % numColumns;
                            RectF rectF = new RectF();
                            rectF.left = firstChildRectF.left + columnIndex * firstChildRectF.width();
                            rectF.right = firstChildRectF.right + columnIndex * firstChildRectF.width();
                            rectF.top = firstChildRectF.top - firstChildRectF.height() * overLineCount;
                            rectF.bottom = firstChildRectF.bottom - firstChildRectF.height() * overLineCount;
                            if (i != 0 && columnIndex == 0) {
                                overLineCount--;
                            }
                            locations[i] = rectF;
                        }
                        for (int i = firstVisiblePosition; i < lastVisiblePosition + 1; i++) {
                            int[] locate = new int[2];
                            final View child = parent.getChildAt(i - firstVisiblePosition);
                            child.getLocationOnScreen(locate);
                            RectF rectF = new RectF(locate[0], locate[1], locate[0] + child.getWidth(), locate[1] + child.getHeight());
                            locations[i] = rectF;
                        }

                        int futureLineCount = 0;
                        for (int i = lastVisiblePosition + 1; i < count; i++) {
                            int columnIndex = i % numColumns;
                            if (columnIndex == 0) {
                                futureLineCount++;
                            }
                            RectF rectF = new RectF();
                            rectF.left = lastChildRectF.left - (numColumns - columnIndex - 1) * firstChildRectF.width();
                            rectF.right = lastChildRectF.right - (numColumns - columnIndex - 1) * firstChildRectF.width();
                            rectF.top = lastChildRectF.top + lastChildRectF.height() * futureLineCount;
                            rectF.bottom = lastChildRectF.bottom + lastChildRectF.height() * futureLineCount;
                            locations[i] = rectF;
                        }
                    } else {
                        generateLocations(childCount);
                    }
                } else {
                    generateLocations(childCount);
                }
            }
            if (thumbSize == 0 && locations != null) {
                thumbSize = (int) locations[0].width();
            }

            final ImageBrowser imageBrowser = new ImageBrowser();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(IB_URLS, urls);
            bundle.putStringArrayList(IB_THUMB_URLS, thumbUrls);
            bundle.putInt(IB_POSITION, position);
            bundle.putInt(IB_MODE, mode);
            bundle.putInt(IB_THUMB_SIZE, thumbSize);
            bundle.putInt(IB_CUSTOM_IMG_RES, customImgRes);
            bundle.putInt(IB_CUSTOM_TEXT_RES, customTextRes);
            bundle.putBoolean(IB_IS_ORIGIN_CENTER_CROP, isCenterCrop);
            bundle.putCharSequence(IB_CUSTOM_TEXT, customChar);
            bundle.putParcelableArray(IB_LOCATIONS, locations);
            imageBrowser.setArguments(bundle);
            imageBrowser.setOnDownloadClickListener(this.downloadListener);
            imageBrowser.setOnDeleteClickListener(this.deleteListener);
            imageBrowser.setOnCustomImgClickListener(this.customImgListener);
            imageBrowser.setOnCustomTxtClickListener(this.customTxtListener);
            if (child != null) {
                child.setAlpha(0.2f);
                imageBrowser.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        child.setAlpha(1);
                    }
                });
            }
            return imageBrowser;
        }

        private void generateLocations(int childCount){
            this.locations = new RectF[childCount];
            for (int i = 0; i < childCount; i++) {
                int[] locate = new int[2];
                final View child = parent.getChildAt(i);
                child.getLocationOnScreen(locate);
                locations[i] = new RectF(locate[0], locate[1], locate[0] + child.getWidth(), locate[1] + child.getHeight());
            }
        }

        private RectF getRectFByIndex(ViewGroup parent, int i) {
            int[] locate = new int[2];
            final View child = parent.getChildAt(i);
            child.getLocationOnScreen(locate);
            return new RectF(locate[0], locate[1], locate[0] + child.getWidth(), locate[1] + child.getHeight());
        }

    }

    OnDownloadClickListener mOnDownloadClickListener;
    OnDeleteClickListener mOnDeleteClickListener;
    View.OnClickListener mCustomImgClickListener;
    View.OnClickListener mCustomTxtClickListener;
    OnDismissListener mOnDismissListener;

    public void setOnCustomImgClickListener(View.OnClickListener listener) {
        this.mCustomImgClickListener = listener;
    }

    public void setOnCustomTxtClickListener(View.OnClickListener listener) {
        this.mCustomTxtClickListener = listener;
    }

    public void setOnDownloadClickListener(OnDownloadClickListener mOnDownloadClickListener) {
        this.mOnDownloadClickListener = mOnDownloadClickListener;
    }

    public void setOnDeleteClickListener(OnDeleteClickListener mOnDeleteClickListener) {
        this.mOnDeleteClickListener = mOnDeleteClickListener;
    }

    public interface OnDownloadClickListener {
        void onDownloadBtnClick(Bitmap bitmap);
    }

    public interface OnDeleteClickListener {
        void onClick(int index, String url);
    }

    public interface OnDismissListener {
        void onDismiss();
    }

    public void setOnDismissListener(OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }

    private class ImagePagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(((WrapImageView) object).targetView);
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            WrapImageView view = viewList.get(position);
            view.startLoading();
            container.addView(view.targetView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return view;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((WrapImageView) object).targetView;
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        for (WrapImageView view : viewList) {
            view.clean();
        }
    }

    @Override
    public void dismissAllowingStateLoss() {
        super.dismissAllowingStateLoss();
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
    }
}
