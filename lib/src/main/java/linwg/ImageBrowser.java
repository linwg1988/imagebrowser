package linwg;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.RectF;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.Arrays;

import linwg.org.lib.R;
import linwg.strategy.ImageLoaderFactory;
import linwg.view.CirclePageIndicator;
import linwg.view.HackyViewPager;
import uk.co.senab.photoview.PhotoView;


/**
 * @author wengui
 * @date 2016/3/9
 */
public class ImageBrowser extends Fragment {
    public static int RESUME_ANIMATION_DURATION = 200;
    public static int ANIMATION_DURATION = 400;
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
     * The key of the {@link #viewDescriptios}
     */
    private static final String IB_DES = "ib_des";
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
     * The key of the {@link #scaleTypeName}
     */
    private static final String IMG_SCALE_TYPE = "img_scale_type";
    /**
     * The key of the {@link #showTitle}
     */
    private static final String IB_SHOW_TITLE = "ib_show_title";

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
    private ArrayList<String> viewDescriptios;
    private boolean showTitle;
    /**
     * The origin viewGroup provide this,if is not null, this array's length is always equals imageView's size.
     */
    private ImageRectFInfo[] rectFs;
    private int customImgRes;
    private int customTextRes;
    /**
     * This is always setting by the origin view's size.The default size is 100dp{@link #DEFAULT_THUMB_SIZE}.
     */
    private int thumbSize;
    String scaleTypeName = ImageView.ScaleType.MATRIX.name();
    private CharSequence customChar;
    /**
     * There are four {@link Mode} use for this param,use {@link Mode#DOWNLOAD} or{@link Mode#DELETE}
     * mode will show you the corresponding implementation,you can set {@link Builder#setOnDownloadClickListener(OnDownloadClickListener)}
     * and {@link #setOnDownloadClickListener(OnDownloadClickListener)} if you want to do something after that happens.
     * Except the above modes,if you want modify you own image button or text button and it's click listener,just call
     * {@link Builder#customImg(int, View.OnClickListener)} or {@link Builder#customText(CharSequence, View.OnClickListener)}.
     */
    private int mode = Mode.NONE;

    private final ArrayList<WrapImageView> viewList = new ArrayList<>();

    private ImageView ivDownLoad;
    private ImagePagerAdapter imagePagerAdapter;
    private CirclePageIndicator circlePageIndicator;
    private View shadowView;
    private ImageView ivDelete;
    private ImageView ivCustom;
    private TextView tvCustom;
    private View contentView;
    private ViewGroup decorView;
    private TextView tvIndicator;
    private TextView tvTitle;
    private TextView tvDescriptions;
    private Builder builder;
    private OnShowingClickListener mOnShowingClickListener;


    @SuppressLint("InflateParams")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        FragmentActivity activity = requireActivity();
        if (savedInstanceState != null) {
            mode = savedInstanceState.getInt(IB_MODE);
            thumbSize = savedInstanceState.getInt(IB_THUMB_SIZE);
            imageUrls = savedInstanceState.getStringArrayList(IB_URLS);
            viewDescriptios = savedInstanceState.getStringArrayList(IB_DES);
            position = savedInstanceState.getInt(IB_POSITION);
            thumbUrls = savedInstanceState.getStringArrayList(IB_THUMB_URLS);
            rectFs = (ImageRectFInfo[]) savedInstanceState.getParcelableArray(IB_LOCATIONS);
            customImgRes = savedInstanceState.getInt(IB_CUSTOM_IMG_RES);
            customTextRes = savedInstanceState.getInt(IB_CUSTOM_TEXT_RES);
            customChar = savedInstanceState.getCharSequence(IB_CUSTOM_TEXT);
            scaleTypeName = savedInstanceState.getString(IMG_SCALE_TYPE);
            showTitle = savedInstanceState.getBoolean(IB_SHOW_TITLE);
        } else {
            final Bundle arguments = getArguments();
            if (arguments != null) {
                mode = arguments.getInt(IB_MODE);
                thumbSize = arguments.getInt(IB_THUMB_SIZE);
                imageUrls = arguments.getStringArrayList(IB_URLS);
                viewDescriptios = arguments.getStringArrayList(IB_DES);
                position = arguments.getInt(IB_POSITION);
                thumbUrls = arguments.getStringArrayList(IB_THUMB_URLS);
                rectFs = (ImageRectFInfo[]) arguments.getParcelableArray(IB_LOCATIONS);
                customImgRes = arguments.getInt(IB_CUSTOM_IMG_RES);
                customTextRes = arguments.getInt(IB_CUSTOM_TEXT_RES);
                customChar = arguments.getCharSequence(IB_CUSTOM_TEXT);
                scaleTypeName = arguments.getString(IMG_SCALE_TYPE);
                showTitle = arguments.getBoolean(IB_SHOW_TITLE);
            }
        }
        if (thumbSize == 0) {
            thumbSize = (int) Util.dp2px(activity, DEFAULT_THUMB_SIZE);
        }
        screenWidth = Util.getScreenWidth(activity);
        screenHeight = Util.getScreenHeight(activity);

        contentView = inflater.inflate(R.layout.fragment_iamge_browser, null);
        initView(contentView);
        decorView = (ViewGroup) activity.getWindow().getDecorView();
        decorView.addView(contentView);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        decorView.removeView(contentView);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(IB_URLS, imageUrls);
        outState.putStringArrayList(IB_DES, viewDescriptios);
        outState.putStringArrayList(IB_THUMB_URLS, thumbUrls);
        outState.putInt(IB_POSITION, position);
        outState.putInt(IB_MODE, mode);
        outState.putInt(IB_THUMB_SIZE, thumbSize);
        outState.putInt(IB_CUSTOM_IMG_RES, customImgRes);
        outState.putInt(IB_CUSTOM_TEXT_RES, customTextRes);
        outState.putCharSequence(IB_CUSTOM_TEXT, customChar);
        outState.putParcelableArray(IB_LOCATIONS, rectFs);
        outState.putString(IMG_SCALE_TYPE, scaleTypeName);
        outState.putBoolean(IB_SHOW_TITLE, showTitle);
    }

    /**
     * To make background shadow transparent.
     */
    public void playDismissAnimation(long duration) {
        ivCustom.setVisibility(View.GONE);
        ivDelete.setVisibility(View.GONE);
        ivDownLoad.setVisibility(View.GONE);
        tvCustom.setVisibility(View.GONE);
        ObjectAnimator.ofFloat(shadowView, "alpha", 0.1f).setDuration(duration).start();
    }

    private void initView(View view) {
        FragmentActivity activity = requireActivity();
        shadowView = view.findViewById(R.id.shadow);
        ivDownLoad = view.findViewById(R.id.ivDownLoad);
        ivDelete = view.findViewById(R.id.ivDelete);
        ivCustom = view.findViewById(R.id.ivCustom);
        tvCustom = view.findViewById(R.id.tvCustom);
        ObjectAnimator.ofFloat(shadowView, "alpha", 1f).setDuration(ANIMATION_DURATION).start();
        fillingViewList();
        customButtonStyle();

        HackyViewPager mViewPager = view.findViewById(R.id.vp_image);
        mViewPager.setOffscreenPageLimit(1);
        imagePagerAdapter = new ImagePagerAdapter();
        mViewPager.setAdapter(imagePagerAdapter);
        mViewPager.setCurrentItem(position);

        tvIndicator = view.findViewById(R.id.tvIndicator);
        tvTitle = view.findViewById(R.id.tvTitle);
        tvDescriptions = view.findViewById(R.id.tvDescriptions);
        circlePageIndicator = view.findViewById(R.id.indicator);

        tvTitle.setText(getDes(position));

        if (showTitle) {
            tvTitle.setVisibility(View.VISIBLE);
        } else {
            tvTitle.setVisibility(View.GONE);
        }

        int top = dp2px(activity, 25);
        ViewGroup.LayoutParams lp = tvTitle.getLayoutParams();
        lp.height += top;
        tvTitle.setPadding(0, top, 0, 0);
        tvTitle.requestLayout();

        if (viewDescriptios != null && viewDescriptios.size() > 0) {
            tvDescriptions.setVisibility(View.VISIBLE);
            tvIndicator.setVisibility(View.GONE);
            circlePageIndicator.setVisibility(View.GONE);
        } else {
            tvDescriptions.setVisibility(View.GONE);
            if (imageUrls.size() > 10) {
                tvIndicator.setVisibility(View.VISIBLE);
                circlePageIndicator.setVisibility(View.GONE);
            } else {
                tvIndicator.setVisibility(View.GONE);
                circlePageIndicator.setVisibility(View.VISIBLE);
            }
        }
        if (showTitle) {
            tvDescriptions.setVisibility(View.GONE);
        }

        String c = String.valueOf(position + 1);
        String all = c + "/" + imageUrls.size();
        SpannableStringBuilder sb = new SpannableStringBuilder(all);
        tvIndicator.setText(all);
        int px = sp2px(activity, 10);
        sb.setSpan(new AbsoluteSizeSpan(px), c.length(), all.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.append("  ");
        sb.append(getDes(position));
        tvDescriptions.setText(sb);

        circlePageIndicator.setViewPager(mViewPager);
        circlePageIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(final int position) {
                tvTitle.setText(getDes(position));
                String c = String.valueOf(position + 1);
                String all = c + "/" + imageUrls.size();
                SpannableStringBuilder sb = new SpannableStringBuilder(all);
                tvIndicator.setText(all);
                int px = sp2px(activity, 10);
                sb.setSpan(new AbsoluteSizeSpan(px), c.length(), all.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                sb.append("  ");
                sb.append(getDes(position));
                tvDescriptions.setText(sb);

                ImageBrowser.this.position = position;
                if (builder != null && builder.linkage && rectFs != null) {
                    RectF rectF = rectFs[position].rectF;
                    int[] parentLocation = new int[2];
                    builder.parent.getLocationOnScreen(parentLocation);
                    RectF parentRectF = new RectF(parentLocation[0], parentLocation[1], parentLocation[0] + builder.parent.getWidth(), parentLocation[1] + builder.parent.getHeight());
                    if (rectF.bottom > parentRectF.bottom) {
                        float offset = rectF.bottom + builder.viewRectFInfo.bottomOffset - parentRectF.bottom;
                        for (ImageRectFInfo f : rectFs) {
                            f.rectF.offset(0, -offset);
                        }
                        performScrollToBottom(builder.parent, offset, position);
                    }
                    if (rectF.top < parentRectF.top) {
                        float offset = parentRectF.top - builder.viewRectFInfo.topOffset - rectF.top;
                        for (ImageRectFInfo f : rectFs) {
                            f.rectF.offset(0, offset);
                        }
                        performScrollToTop(builder.parent, offset, position);
                    }
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
    }

    private void fillingViewList() {
        viewList.clear();
        for (int i = 0; i < imageUrls.size(); i++) {
            @SuppressLint("InflateParams") View imageLayout = requireActivity().getLayoutInflater().inflate(R.layout.item_image_browser, null);
            final View ivThumbnail = imageLayout.findViewById(R.id.iv_thumbnail);
            final ViewGroup.LayoutParams params = ivThumbnail.getLayoutParams();
            //init thumb size of thumb imageView
            if (thumbSize == 0) {
                params.height = (int) rectFs[i].rectF.height();
                params.width = (int) rectFs[i].rectF.width();
            } else {
                params.height = thumbSize;
                params.width = thumbSize;
            }

            ivThumbnail.setLayoutParams(params);
            WrapImageView wrapImageView = new WrapImageView(this, imageLayout, imageUrls.get(i), thumbUrls == null ? null : thumbUrls.get(i),
                    rectFs == null ? null : rectFs[i].rectF, i == position, screenWidth, screenHeight, ImageView.ScaleType.valueOf(rectFs == null ? scaleTypeName : rectFs[i].scaleType));
            viewList.add(wrapImageView);
        }
    }

    private void customButtonStyle() {
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
                ivDelete.setOnClickListener(v -> {
                    if (mOnDeleteClickListener != null) {
                        new AlertDialog.Builder(requireActivity()).setTitle(R.string.delete_alert)
                                .setMessage(R.string.delete_message).setPositiveButton(R.string.sure, (dialog, which) -> {
                                    removeCurrent();
                                    if (imageUrls.size() == 0) {
                                        ImageBrowser.this.dismissWithoutAnimation();
                                    }
                                    dialog.cancel();
                                }).setNegativeButton(R.string.cancel, (dialog, which) -> {

                                }).create().show();
                    }
                });
                break;
            case Mode.DOWNLOAD:
                ivDownLoad.setVisibility(View.VISIBLE);
                ivDownLoad.setOnClickListener(v -> {
                    if (mOnDownloadClickListener != null) {
                        final PhotoView view = viewList.get(position).getPhotoView();
                        if (ImageLoaderFactory.get().isDrawableLoadingCompleted(view)) {
                            mOnDownloadClickListener.onDownloadBtnClick(ImageLoaderFactory.get().getBitmapFromImageView(view));
                        }
                    }
                });
                break;
            default:
        }
    }

    public void setIndicatorPaddingBottom(final int paddingBottom) {
        circlePageIndicator.post(() -> {
            int dp6 = (int) Util.dp2px(requireActivity(), 6);
            int dp12 = dp6 * 2;
            circlePageIndicator.setPadding(0, 0, 0, paddingBottom);
            tvDescriptions.setPadding(0, 0, 0, paddingBottom);
            tvDescriptions.setPadding(dp12, dp12, dp12, dp12 + paddingBottom);
            tvIndicator.setPadding(dp6, dp6, dp6, dp6 + paddingBottom);
        });
    }

    private void performScrollToBottom(ViewGroup parent, float offset, int position) {
        if (parent instanceof AbsListView) {
            AbsListViewHelper.performScrollToBottom((AbsListView) parent, offset, position);
        } else if (parent instanceof RecyclerView) {
            RecyclerViewHelper.performScrollToBottom((RecyclerView) parent, offset, position);
        } else {

        }
    }

    private void performScrollToTop(ViewGroup parent, float offset, int position) {
        if (parent instanceof AbsListView) {
            AbsListViewHelper.performScrollToTop((AbsListView) parent, offset, position);
        } else if (parent instanceof RecyclerView) {
            RecyclerViewHelper.performScrollToTop((RecyclerView) parent, offset, position);
        }
    }

    public static int dp2px(Context context, float dpVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpVal, context.getResources().getDisplayMetrics());
    }

    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, spVal, context.getResources().getDisplayMetrics());
    }

    private String getDes(int position) {
        if (viewDescriptios != null) {
            if (viewDescriptios.size() > position) {
                return viewDescriptios.get(position);
            }
        }
        return "";
    }

    /**
     * Remove the current showing imageView and url.
     */
    public void removeCurrent() {
        final String url = imageUrls.get(position);
        mOnDeleteClickListener.onClick(position, url);
        imagePagerAdapter.notifyDataSetChanged();
    }

    public static boolean onBackPressed(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentByTag(TAG);
        if (fragment instanceof ImageBrowser) {
            return ((ImageBrowser) fragment).onBackPressed();
        }
        return false;
    }

    private boolean onBackPressed() {
        dismiss();
        return true;
    }

    public boolean onLongClick(WrapImageView wrapImageView) {
        if (l != null) {
            int position = viewList.indexOf(wrapImageView);
            l.handlerLongClick(this, position);
            return true;
        }
        return false;
    }

    OnPhotoLongClickListener l;

    public void setBuilder(Builder builder) {
        this.builder = builder;
    }

    public void onResourceReady() {
        if (mOnShowingClickListener != null) {
            mOnShowingClickListener.onShowing();
        }
    }

    public void setShadowAlpha(double targetScale) {
        shadowView.setAlpha((float) targetScale);
        if (mOnShowingClickListener != null) {
            mOnShowingClickListener.onChildChange(position);
        }
    }

    public interface OnPhotoLongClickListener {
        /**
         * Long press click event callback
         *
         * @param browser  Browser object
         * @param position Click index
         */
        void handlerLongClick(ImageBrowser browser, int position);
    }

    private interface OnShowingClickListener {
        /**
         * This is setting for ImageBrowser showing callback.
         */
        void onShowing();

        /**
         * Image index to be displayed
         *
         * @param position The urls' index
         */
        void onChildChange(int position);
    }

    public static class Mode {
        public static final int NONE = -1;
        public static final int DOWNLOAD = -2;
        public static final int DELETE = -3;
        public static final int CUSTOM = -4;
    }

    public static class Builder {
        OnPhotoLongClickListener l;
        private final Context context;
        private int mode = Mode.NONE;
        private ArrayList<String> urls;
        private ArrayList<String> viewDes;
        private int position;
        private ArrayList<String> thumbUrls;
        private int thumbSize;
        ViewGroup parent;
        ViewRectFInfo viewRectFInfo;
        int imageViewId;
        int[] imageViewIds;
        private OnDownloadClickListener downloadListener;
        private OnDeleteClickListener deleteListener;
        private View.OnClickListener customImgListener;
        private View.OnClickListener customTxtListener;
        private int customImgRes;
        private int customTextRes;
        private CharSequence customChar;
        private View child;
        private boolean showTitle;
        boolean linkage;
        private ImageView.ScaleType scaleType;

        public Builder(Context context) {
            this.context = context;
        }

        public Builder mode(int mode) {
            this.mode = mode;
            return this;
        }

        public Builder url(String url) {
            if (urls == null) {
                urls = new ArrayList<>();
            }
            urls.clear();
            urls.add(url);
            position = 0;
            return this;
        }

        public Builder thumbUrl(String thumbUrl) {
            if (thumbUrls == null) {
                thumbUrls = new ArrayList<>();
            }
            thumbUrls.clear();
            thumbUrls.add(thumbUrl);
            return this;
        }

        public Builder showTitle(boolean showTitle) {
            this.showTitle = showTitle;
            return this;
        }

        public Builder urls(ArrayList<String> urls) {
            this.urls = urls;
            return this;
        }

        public Builder urls(String... urls) {
            this.urls = new ArrayList<>(Arrays.asList(urls));
            return this;
        }

        public Builder viewDes(ArrayList<String> viewDes) {
            this.viewDes = viewDes;
            return this;
        }

        public Builder linkage(boolean linkage) {
            this.linkage = linkage;
            return this;
        }

        public Builder thumbUrls(ArrayList<String> thumbUrls) {
            this.thumbUrls = thumbUrls;
            return this;
        }

        public Builder photoLongClickListener(OnPhotoLongClickListener l) {
            this.l = l;
            return this;
        }

        /**
         * Now it seems very important if we want the animation is playing correct.
         */
        public Builder position(int index) {
            this.position = index;
            return this;
        }

        private Builder thumbSize(int thumbSize) {
            this.thumbSize = thumbSize;
            return this;
        }

        public Builder targetParent(ViewGroup parent) {
            this.parent = parent;
            return this;
        }

        public Builder imageViewId(@IdRes int imageViewId) {
            this.imageViewId = imageViewId;
            return this;
        }

        /**
         * If your images are not showing in a recycler view or list view,so the views' ids are different,you can set the ids by this method.
         * Once you use this means that {@link #imageViewId(int)} will not effect.
         */
        public Builder imageViewIds(int... imageViewIds) {
            this.imageViewIds = imageViewIds;
            return this;
        }

        /**
         * From V1.2.0 this property will obtain from parent view if the parent view and the position has been set.
         * Write by the past date:Once I call this is want to set the origin view's alpha so the animation will be playing like the origin view is
         * really moving.But it works bad,so just ignore this method. (ps:HaHa...I just suppose one day I'll make it work,so I didn't delete it.)
         * <p>
         * Note :I hide the target image view when the photo view on animation start by set it's alpha to 0. And change the child's reference
         * after the viewpager on selected.OK! Now it works.
         * After ver 1.2.1.if we just want this work on a single image view ,just call this method and all things are being ok.
         */
        public Builder target(View child) {
            this.child = child;
            return this;
        }

        /**
         * From V1.2.0 this property will obtain from target imageView if the imageViewId has being set.So it is not required.
         */
        public Builder scaleType(ImageView.ScaleType scaleType) {
            this.scaleType = scaleType;
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

        public ImageBrowser build() {
            if (parent != null) {
                if (parent instanceof RecyclerView) {
                    viewRectFInfo = RecyclerViewHelper.measureChild((RecyclerView) parent, imageViewId);
                } else if (parent instanceof AbsListView) {
                    viewRectFInfo = AbsListViewHelper.measureChild((AbsListView) parent, imageViewId);
                } else {
                    if (imageViewIds != null && imageViewIds.length > 0) {
                        viewRectFInfo = ViewGroupHelper.measureChild(parent, imageViewIds);
                    } else {
                        viewRectFInfo = ViewGroupHelper.measureChild(parent, imageViewId);
                    }
                }
                if (child == null) {
                    child = findChild(position);
                }
            } else {
                if (child != null) {
                    viewRectFInfo = ViewHelper.measureChild(child);
                }
            }

            if (thumbSize == 0 && viewRectFInfo != null && viewRectFInfo.imgLocations != null) {
                thumbSize = (int) viewRectFInfo.imgLocations[0].rectF.width();
            }

            final ImageBrowser imageBrowser = new ImageBrowser();
            Bundle bundle = new Bundle();
            bundle.putStringArrayList(IB_URLS, urls);
            bundle.putStringArrayList(IB_DES, viewDes);
            bundle.putStringArrayList(IB_THUMB_URLS, thumbUrls);
            bundle.putInt(IB_POSITION, position);
            bundle.putInt(IB_MODE, mode);
            bundle.putInt(IB_THUMB_SIZE, thumbSize);
            bundle.putInt(IB_CUSTOM_IMG_RES, customImgRes);
            bundle.putInt(IB_CUSTOM_TEXT_RES, customTextRes);
            bundle.putString(IMG_SCALE_TYPE, scaleType == null ? ImageView.ScaleType.MATRIX.name() : scaleType.name());
            bundle.putBoolean(IB_SHOW_TITLE, showTitle);
            bundle.putCharSequence(IB_CUSTOM_TEXT, customChar);
            bundle.putParcelableArray(IB_LOCATIONS, viewRectFInfo == null ? null : viewRectFInfo.imgLocations);
            imageBrowser.setArguments(bundle);
            imageBrowser.setBuilder(this);
            imageBrowser.setOnDownloadClickListener(this.downloadListener);
            imageBrowser.setOnDeleteClickListener(this.deleteListener);
            imageBrowser.setOnCustomImgClickListener(this.customImgListener);
            imageBrowser.setOnCustomTxtClickListener(this.customTxtListener);
            imageBrowser.setOnLongClickListener(l);
            if (child != null) {
                imageBrowser.setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss() {
                        if (child != null) {
                            child.setAlpha(1);
                        }
                    }
                });
                imageBrowser.setOnShowingListener(new OnShowingClickListener() {
                    @Override
                    public void onShowing() {
                        child.setAlpha(0.0f);
                    }

                    @Override
                    public void onChildChange(int position) {
                        if (parent != null) {
                            changeChild(position);
                        }
                    }
                });
            }
            return imageBrowser;
        }

        public void show() {
            ImageBrowser imageBrowser = build();
            imageBrowser.show(((FragmentActivity) context).getSupportFragmentManager(), "ImageBrowserTag");
        }

        void changeChild(int position) {
            if (child != null) {
                child.setAlpha(1);
            }
            child = findChild(position);
            if (child != null) {
                child.setAlpha(0);
            }
        }

        private View findChild(int position) {
            if (parent == null) {
                return null;
            }
            if (parent instanceof AbsListView) {
                return AbsListViewHelper.findChildByPosition((AbsListView) parent, imageViewId, position);
            } else if (parent instanceof RecyclerView) {
                return RecyclerViewHelper.findChildByPosition((RecyclerView) parent, imageViewId, position);
            } else {
                if (imageViewIds != null && imageViewIds.length > 0) {
                    return ViewGroupHelper.findChildByPosition(parent, imageViewIds, position);
                } else {
                    return ViewGroupHelper.findChildByPosition(parent, imageViewId, position);
                }
            }
        }
    }

    private void setOnShowingListener(OnShowingClickListener l) {
        this.mOnShowingClickListener = l;
    }

    private void setOnLongClickListener(OnPhotoLongClickListener l) {
        this.l = l;
    }

    private static String TAG = "";

    public void show(FragmentManager fragmentManager, String tag) {
        TAG = tag;
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    private boolean isDismiss = false;

    public void dismissWithoutAnimation() {
        onDismiss();
    }

    public void dismiss() {
        dismiss(ImageBrowser.ANIMATION_DURATION);
    }

    public void dismiss(long duration) {
        shadowView.animate().alpha(0).setDuration(duration).start();
        tvIndicator.animate().translationY(tvIndicator.getHeight()).setDuration(duration).start();
        tvTitle.animate().translationY(-tvTitle.getHeight()).setDuration(duration).start();
        circlePageIndicator.animate().translationY(circlePageIndicator.getHeight()).setDuration(duration).start();
        tvDescriptions.animate().alpha(0).setDuration(duration).start();
        if (mOnShowingClickListener != null) {
            mOnShowingClickListener.onChildChange(position);
        }
        viewList.get(position).endAnimation(duration);
    }

    protected void onDismiss() {
        if (isDismiss) {
            return;
        }
        isDismiss = true;
        if (mOnDismissListener != null) {
            mOnDismissListener.onDismiss();
        }
        getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
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
        /**
         * 下载按钮点击事件回调
         *
         * @param bitmap 位图
         */
        void onDownloadBtnClick(Bitmap bitmap);
    }

    public interface OnDeleteClickListener {
        /**
         * 删除按钮点击事件回调
         *
         * @param index 图片索引
         * @param url   图片地址
         */
        void onClick(int index, String url);
    }

    public interface OnDismissListener {
        /**
         * 浏览器关闭时的回调
         */
        void onDismiss();
    }

    public void setOnDismissListener(OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }

    private class ImagePagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, @NonNull Object object) {
            container.removeView(((WrapImageView) object).targetView);
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @NonNull
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            WrapImageView view = viewList.get(position);
            view.startLoading();
            container.addView(view.targetView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return view;
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == ((WrapImageView) object).targetView;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }
}
