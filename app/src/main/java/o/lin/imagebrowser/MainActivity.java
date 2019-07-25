package o.lin.imagebrowser;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestFutureTarget;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import org.wglin.imagepicker.ImageLoader;
import org.wglin.imagepicker.ImagePicker;
import org.wglin.imagepicker.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import linwg.GlideLoaderStrategy;
import linwg.ImageBrowser;
import linwg.strategy.ImageLoaderFactory;

public class MainActivity extends AppCompatActivity implements ImagePicker.OnImagePickerListener {
    ArrayList<String> imageUrls = new ArrayList<>();
    private ImageAdapter baseAdapter;
    String imgUrl0 = "http://img4.imgtn.bdimg.com/it/u=108703274,3260405534&fm=214&gp=0.jpg";
    String imgUrl1 = "http://imgsrc.baidu.com/baike/pic/item/a8014c086e061d95b8e66cc57bf40ad163d9caaf.jpg";
    String imgUrl2 = "http://img3.duitang.com/uploads/item/201412/30/20141230214525_AGRCa.thumb.700_0.jpeg";
    String imgUrl3 = "http://img4q.duitang.com/uploads/item/201502/24/20150224110429_cGxLw.thumb.700_0.jpeg";
    String imgUrl4 = "http://img5q.duitang.com/uploads/item/201412/30/20141230214909_zVUyB.thumb.700_0.jpeg";
    String imgUrl5 = "http://i2.hdslb.com/u_user/e254ddf4f07a0c605f755a71dccf4495.jpg";
    String imgUrl6 = "http://img5.duitang.com/uploads/item/201502/08/20150208195826_L4XPc.thumb.700_0.jpeg";
    String imgUrl7 = "http://img5.duitang.com/uploads/item/201412/29/20141229095634_mBmFj.jpeg";
    String imgUrl8 = "http://imgsrc.baidu.com/forum/w%3D580%3Bcp%3Dtieba%2C10%2C484%3Bap%3D%C3%C9%B3%F5%D6%AE%B4%CD%B0%C9%2C90%2C492/sign=56d4c57bf11f3a295ac8d5c6a91edf41/a8ec8a13632762d02d83c01aa1ec08fa503dc6d2.jpg";
    String imgUrl9 = "http://imgsrc.baidu.com/forum/w%3D580%3B/sign=9f1bb672f91986184147ef8c7ad62e73/09fa513d269759ee116d53d8b3fb43166d22dfe7.jpg";
    String imgUrl11 = "http://imgsrc.baidu.com/baike/pic/item/a8014c086e061d95b8e66cc57bf40ad163d9caaf.jpg";
    String imgUrl22 = "http://img3.duitang.com/uploads/item/201412/30/20141230214525_AGRCa.thumb.700_0.jpeg";
    String imgUrl33 = "http://img4q.duitang.com/uploads/item/201502/24/20150224110429_cGxLw.thumb.700_0.jpeg";
    String imgUrl44 = "http://img5q.duitang.com/uploads/item/201412/30/20141230214909_zVUyB.thumb.700_0.jpeg";
    String imgUrl55 = "http://i2.hdslb.com/u_user/e254ddf4f07a0c605f755a71dccf4495.jpg";
    String imgUrl66 = "http://img5.duitang.com/uploads/item/201502/08/20150208195826_L4XPc.thumb.700_0.jpeg";
    String imgUrl77 = "http://img5.duitang.com/uploads/item/201412/29/20141229095634_mBmFj.jpeg";
    String imgUrl88 = "http://imgsrc.baidu.com/forum/w%3D580%3Bcp%3Dtieba%2C10%2C484%3Bap%3D%C3%C9%B3%F5%D6%AE%B4%CD%B0%C9%2C90%2C492/sign=56d4c57bf11f3a295ac8d5c6a91edf41/a8ec8a13632762d02d83c01aa1ec08fa503dc6d2.jpg";
    String imgUrl99 = "http://imgsrc.baidu.com/forum/w%3D580%3B/sign=9f1bb672f91986184147ef8c7ad62e73/09fa513d269759ee116d53d8b3fb43166d22dfe7.jpg";
    String imgUrl100 = "http://static.test.lsyc.yssc-cloud.com/review-img/lsyc/2019/07/16/8d83c2f5a346770e0c3a481685de18dd.jpg";
    RecyclerView recyclerView;
    ArrayList<String> urls = new ArrayList<>();
    private MAdapter mAdapter;
    ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;
    RelativeLayout rlImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImagePicker.setImageLoader(new ImageLoader() {
            @Override
            public void loadImage(Context context, String imgPath, ImageView targetView) {
                Glide.with(context).load(imgPath).apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL)).into(targetView);
            }
        });
        ImageLoaderFactory.set(new GlideLoaderStrategy());
        final GridView grid = (GridView) findViewById(R.id.grid);
        recyclerView = findViewById(R.id.recyclerView);
        rlImages = findViewById(R.id.rlImages);
        baseAdapter = new ImageAdapter(imageUrls, this);
        baseAdapter.setScaleType(scaleType);
        grid.setAdapter(baseAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new ImageBrowser.Builder(MainActivity.this)
                        .urls(baseAdapter.getImageUrls())
                        .targetParent(parent)
                        .imageViewId(R.id.ivImage2)
                        .target(view)
                        .scaleType(((ImageView) view).getScaleType())
                        .linkage(true)
                        .position(position)
                        .show();
            }
        });
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new MAdapter(this, recyclerView);
        recyclerView.setAdapter(mAdapter);
        RadioGroup rgType = findViewById(R.id.rgType);
        rgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                scaleType = checkedId == R.id.rbCenterCrop ? ImageView.ScaleType.CENTER_CROP : checkedId == R.id.rbFitXY ? ImageView.ScaleType.FIT_XY : checkedId == R.id.rbFitCenter ? ImageView.ScaleType.FIT_CENTER : ImageView.ScaleType.CENTER_INSIDE;
                imageUrls.add(imgUrl1);
                baseAdapter.setScaleType(scaleType);
                mAdapter = new MAdapter(MainActivity.this, recyclerView);
                mAdapter.setScaleType(scaleType);
                mAdapter.setData(imageUrls);
                recyclerView.setAdapter(mAdapter);
            }
        });

        findViewById(R.id.label).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urls.clear();
                urls.add(imgUrl0);
                urls.add(imgUrl1);
                urls.add(imgUrl2);
                urls.add(imgUrl3);
                urls.add(imgUrl4);
                urls.add(imgUrl5);
                urls.add(imgUrl6);
                urls.add(imgUrl7);
                urls.add(imgUrl8);
                urls.add(imgUrl9);
                urls.add(imgUrl11);
                urls.add(imgUrl22);
                urls.add(imgUrl33);
                urls.add(imgUrl44);
                urls.add(imgUrl55);
                urls.add(imgUrl66);
                urls.add(imgUrl77);
                urls.add(imgUrl88);
                urls.add(imgUrl99);
                urls.add(imgUrl100);
                imageUrls.clear();
                imageUrls.addAll(urls);
                baseAdapter.setImageUrls(urls);
                mAdapter.setData(imageUrls);
                mAdapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.gridViewBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grid.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                rlImages.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.recyclerViewBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grid.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                rlImages.setVisibility(View.GONE);
            }
        });
        final ImageView ivImageA = findViewById(R.id.ivImageA);
        final ImageView ivImageB = findViewById(R.id.ivImageB);
        final ImageView ivImageC = findViewById(R.id.ivImageC);
        final ImageView ivImageD = findViewById(R.id.ivImageD);
        findViewById(R.id.parentViewBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grid.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                rlImages.setVisibility(View.VISIBLE);
                Glide.with(MainActivity.this).load(imgUrl3).into(ivImageA);
                Glide.with(MainActivity.this).load(imgUrl4).into(ivImageB);
                Glide.with(MainActivity.this).load(imgUrl5).into(ivImageC);
                Glide.with(MainActivity.this).load(imgUrl6).into(ivImageD);
            }
        });
        findViewById(R.id.ivImageA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageBrowser.Builder(MainActivity.this)
                        .urls(imgUrl3,imgUrl4,imgUrl5,imgUrl6)
                        .targetParent(rlImages)
                        .imageViewIds(R.id.ivImageA,R.id.ivImageB,R.id.ivImageC,R.id.ivImageD)
                        .linkage(true)
                        .position(0)
                        .show();
            }
        });
        findViewById(R.id.ivImageB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageBrowser.Builder(MainActivity.this)
                        .urls(imgUrl3,imgUrl4,imgUrl5,imgUrl6)
                        .targetParent(rlImages)
                        .imageViewIds(R.id.ivImageA,R.id.ivImageB,R.id.ivImageC,R.id.ivImageD)
                        .linkage(true)
                        .position(1)
                        .show();
            }
        });
        findViewById(R.id.ivImageC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageBrowser.Builder(MainActivity.this)
                        .urls(imgUrl3,imgUrl4,imgUrl5,imgUrl6)
                        .targetParent(rlImages)
                        .imageViewIds(R.id.ivImageA,R.id.ivImageB,R.id.ivImageC,R.id.ivImageD)
                        .linkage(true)
                        .position(2)
                        .show();
            }
        });
        findViewById(R.id.ivImageD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageBrowser.Builder(MainActivity.this)
                        .urls(imgUrl3,imgUrl4,imgUrl5,imgUrl6)
                        .targetParent(rlImages)
                        .imageViewIds(R.id.ivImageA,R.id.ivImageB,R.id.ivImageC,R.id.ivImageD)
                        .linkage(true)
                        .position(3)
                        .show();
            }
        });
        findViewById(R.id.tvColumnCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setItems(new String[]{"1", "2", "3"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((TextView) findViewById(R.id.tvColumnCount)).setText("列数：" + (which + 1));
                        grid.setNumColumns(which + 1);
                        gridLayoutManager.setSpanCount(which + 1);
                        baseAdapter.notifyDataSetChanged();
                        mAdapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });

        final ImageView ivSingle = findViewById(R.id.ivSingle);
        Glide.with(this).load(imgUrl4).into(ivSingle);


        ivSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageBrowser.Builder(MainActivity.this)
                        .urls(imgUrl4).target(v)
                        .show();
//                Intent intent = new Intent(MainActivity.this, ActivityTransitionToActivity.class);
//                ActivityOptionsCompat options = ActivityOptionsCompat.
//                        makeSceneTransitionAnimation(MainActivity.this, ivSingle, getString(R.string.transition_test));
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//                    startActivity(intent, options.toBundle());
//                }
            }
        });
    }

    static class MAdapter extends RecyclerView.Adapter<VH> {
        Context context;
        ArrayList<String> imageUrls = new ArrayList();

        public void setScaleType(ImageView.ScaleType scaleType) {
            this.scaleType = scaleType;
        }

        private ImageView.ScaleType scaleType = ImageView.ScaleType.MATRIX;
        private RecyclerView recyclerView;

        public MAdapter(Context context, RecyclerView recyclerView) {
            this.context = context;
            this.recyclerView = recyclerView;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(final VH holder, int position) {
            holder.ivImage.setScaleType(scaleType);
            Glide.with(context).load(imageUrls.get(holder.getLayoutPosition())).into(holder.ivImage);
            holder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ImageBrowser.Builder(context)
                            .urls(imageUrls)
                            .targetParent(recyclerView)
                            .imageViewId(R.id.ivImage)
                            .scaleType(holder.ivImage.getScaleType())
                            .target(holder.ivImage)
                            .linkage(true)
                            .position(holder.getLayoutPosition())
                            .show();
                }
            });
        }

        public void setData(ArrayList<String> urls) {
            imageUrls.clear();
            imageUrls.addAll(urls);
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return imageUrls.size();
        }
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView ivImage;

        public VH(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }

    public void pickImage(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 123);
        } else {
            new ImagePicker.Builder().maxPictureNumber(99).build().show(getSupportFragmentManager(), "imagePicker");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 123) {
            List<String> deniedPermissions = new ArrayList<>();
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    deniedPermissions.add(permissions[i]);
                }
            }
            if (deniedPermissions.size() > 0) {
                Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show();
            } else {
                new ImagePicker.Builder().maxPictureNumber(99).build().show(getSupportFragmentManager(), "imagePicker");
            }
        }
    }

    @Override
    public void onImagesPicked(List<String> imgPaths, String selectedDir) {
        imageUrls.clear();
        imageUrls.addAll(imgPaths);
        mAdapter.setData(imageUrls);
        mAdapter.notifyDataSetChanged();
        baseAdapter.setImageUrls(imageUrls);
    }

    @Override
    public void onCameraCallBack(String imgPath) {

    }

    private static class ImageAdapter extends BaseAdapter {
        int width;

        private ArrayList<String> imageUrls;
        private Context context;
        private ImageView.ScaleType scaleType;

        public void setImageUrls(ArrayList<String> imageUrls) {
            this.imageUrls = imageUrls;
            notifyDataSetChanged();
        }

        public ImageAdapter(ArrayList<String> imageUrls, Context context) {
            this.imageUrls = imageUrls;
            this.context = context;
            width = ScreenUtils.getScreenWidth(context) / 3;
        }

        public ArrayList<String> getImageUrls() {
            return imageUrls;
        }

        @Override
        public int getCount() {
            return imageUrls.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = new ImageView(context);
                convertView.setId(R.id.ivImage2);
                convertView.setLayoutParams(new AbsListView.LayoutParams(width, width));
            }
            ImageView img = (ImageView) convertView;
            img.setScaleType(scaleType);
            Glide.with(context).load(imageUrls.get(position)).into(img);
            return convertView;
        }

        public void setScaleType(ImageView.ScaleType scaleType) {
            this.scaleType = scaleType;
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBackPressed() {
        if (ImageBrowser.onBackPressed(this)) {
            return;
        }
        super.onBackPressed();
    }
}
