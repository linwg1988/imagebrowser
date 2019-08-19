package o.lin.imagebrowser;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import org.wglin.imagepicker.ImageLoader;
import org.wglin.imagepicker.ImagePicker;
import org.wglin.imagepicker.ScreenUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import linwg.GlideLoaderStrategy;
import linwg.ImageBrowser;
import linwg.strategy.ImageLoaderFactory;

public class MainActivity extends AppCompatActivity implements ImagePicker.OnImagePickerListener {
    ArrayList<String> imageUrls = new ArrayList<>();
    private ImageAdapter baseAdapter;
    static int columnCount = 1;
    List<String> urlList = Arrays.asList(
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566205949866&di=60039e4e23d60c7d0a279e1925bd17e3&imgtype=0&src=http%3A%2F%2Fp5.qhimg.com%2Ft01e4e0ff65fb5540d6.jpg",
            "http://imgsrc.baidu.com/forum/w%3D580%3Bcp%3Dtieba%2C10%2C484%3Bap%3D%C3%C9%B3%F5%D6%AE%B4%CD%B0%C9%2C90%2C492/sign=56d4c57bf11f3a295ac8d5c6a91edf41/a8ec8a13632762d02d83c01aa1ec08fa503dc6d2.jpg",
            "http://img4.imgtn.bdimg.com/it/u=108703274,3260405534&fm=214&gp=0.jpg",
            "http://imgsrc.baidu.com/baike/pic/item/a8014c086e061d95b8e66cc57bf40ad163d9caaf.jpg",
            "http://img3.duitang.com/uploads/item/201412/30/20141230214525_AGRCa.thumb.700_0.jpeg",
            "http://i2.hdslb.com/u_user/e254ddf4f07a0c605f755a71dccf4495.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566206461944&di=c8cc6d632ca9c17d593c6bd491aada50&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201807%2F14%2F20180714214342_s2tZT.thumb.700_0.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566205949857&di=7be0d7174e3094a55c4b0e4472e9f64d&imgtype=0&src=http%3A%2F%2Fc1.haibao.cn%2Fimg%2F1080_720_100_1%2F1521708878.4879%2Fd4d9c08c4e9d8c9a956d540201b73b58.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566205949862&di=8599b017f5a491fbc4332e9a89abf7f2&imgtype=0&src=http%3A%2F%2Fa1.att.hudong.com%2F77%2F40%2F20300001128119146639404948542.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566205949865&di=10c2a12ac97d2124509cee8d72f12322&imgtype=0&src=http%3A%2F%2Fpics7.baidu.com%2Ffeed%2Fb3b7d0a20cf431ad1fb4d0b39c8f8daa2fdd9811.png%3Ftoken%3D7bf0af80d08a2548385742e9c65fa178%26s%3D9C81895F32B377949C2C4DBE03005042",
            "http://img5.duitang.com/uploads/item/201412/29/20141229095634_mBmFj.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566206770159&di=a4993951049bd98813f5d2efdc053a49&imgtype=0&src=http%3A%2F%2Fwww.haoqilu.cn%2Fuploadfile%2F2018%2F1217%2F20181217094826339.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566206461944&di=2df61ae39c45c87edc9b0acb66cc7591&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201811%2F16%2F20181116002224_PckAQ.thumb.700_0.jpeg",
            "http://img5.duitang.com/uploads/item/201502/08/20150208195826_L4XPc.thumb.700_0.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566206461943&di=89f7b5c0ac811cdc0cc3f291bb044895&imgtype=0&src=http%3A%2F%2Fc1.haibao.cn%2Fimg%2F1080_1080_100_0%2F1504521577.7657%2F391efde1139d75ba04a1f0ec7372d231.jpg",
            "http://img4q.duitang.com/uploads/item/201502/24/20150224110429_cGxLw.thumb.700_0.jpeg",
            "http://img5q.duitang.com/uploads/item/201412/30/20141230214909_zVUyB.thumb.700_0.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566206770155&di=d98e2d562eadc83d4df7664365d062d7&imgtype=0&src=http%3A%2F%2Fimg2015.zdface.com%2F20190301%2Fa67a1bbc0defe1e7f029721548a91c82.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566206743132&di=023c67364c77f5d66c95bf93fdce721f&imgtype=0&src=http%3A%2F%2Fimg5q.duitang.com%2Fuploads%2Fitem%2F201404%2F11%2F20140411220728_UQNjQ.thumb.700_0.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566206650374&di=0842c2442a23580548b2dd04a1cd8c63&imgtype=0&src=http%3A%2F%2Fimg1.gtimg.com%2Ffashion%2Fpics%2Fhv1%2F219%2F73%2F2302%2F149706384.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566206585012&di=bae019d295fe26b4e8521bc4318a292a&imgtype=0&src=http%3A%2F%2Fimg.mp.itc.cn%2Fupload%2F20170419%2Fc8e4f2407b8b4b3fae2b36ba792b07f2_th.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566206461937&di=1654d32608ac351fc8638038b04199cb&imgtype=0&src=http%3A%2F%2F5b0988e595225.cdn.sohucs.com%2Fq_mini%2Cc_zoom%2Cw_640%2Fimages%2F20171103%2F3dbe7f3bf3854431ac27ffb319eade8a.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566206461940&di=397ce04d408fadd4406eea421e048124&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201807%2F14%2F20180714214344_eF5yC.jpeg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566205949865&di=deebbf1995884a3bfeba00ea7d25b95f&imgtype=0&src=http%3A%2F%2Fi0.hdslb.com%2Fbfs%2Farticle%2F09292df948b1a08d5046ea657bb24501c7863d5f.jpg",
            "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1566205949865&di=3142def4ac39f26ca1e849996ba0a6b4&imgtype=0&src=http%3A%2F%2Fb-ssl.duitang.com%2Fuploads%2Fitem%2F201809%2F05%2F20180905220643_vrgyv.jpg",
            "http://imgsrc.baidu.com/forum/w%3D580%3B/sign=9f1bb672f91986184147ef8c7ad62e73/09fa513d269759ee116d53d8b3fb43166d22dfe7.jpg"
            );
    RecyclerView recyclerView;
    ArrayList<String> urls = new ArrayList<>();
    private MAdapter mAdapter;
    ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;
    RelativeLayout rlImages;
    String[] scaleTypeArr = new String[]{"CENTER_CROP", "FIT_XY", "FIT_CENTER", "CENTER_INSIDE"};
    String[] parentTypeArr = new String[]{"GridView", "RecyclerView", "ViewGroup"};

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
        final GridView grid = findViewById(R.id.grid);
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
        final ImageView ivImageA = findViewById(R.id.ivImageA);
        final ImageView ivImageB = findViewById(R.id.ivImageB);
        final ImageView ivImageC = findViewById(R.id.ivImageC);
        final ImageView ivImageD = findViewById(R.id.ivImageD);
        findViewById(R.id.ivImageA).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageBrowser.Builder(MainActivity.this)
                        .urls(urlList.get(0), urlList.get(0), urlList.get(0), urlList.get(0))
                        .targetParent(rlImages)
                        .imageViewIds(R.id.ivImageA, R.id.ivImageB, R.id.ivImageC, R.id.ivImageD)
                        .linkage(true)
                        .position(0)
                        .show();
            }
        });
        findViewById(R.id.ivImageB).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageBrowser.Builder(MainActivity.this)
                        .urls(urlList.get(0), urlList.get(0), urlList.get(0), urlList.get(0))
                        .targetParent(rlImages)
                        .imageViewIds(R.id.ivImageA, R.id.ivImageB, R.id.ivImageC, R.id.ivImageD)
                        .linkage(true)
                        .position(1)
                        .show();
            }
        });
        findViewById(R.id.ivImageC).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageBrowser.Builder(MainActivity.this)
                        .urls(urlList.get(0), urlList.get(0), urlList.get(0), urlList.get(0))
                        .targetParent(rlImages)
                        .imageViewIds(R.id.ivImageA, R.id.ivImageB, R.id.ivImageC, R.id.ivImageD)
                        .linkage(true)
                        .position(2)
                        .show();
            }
        });
        findViewById(R.id.ivImageD).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageBrowser.Builder(MainActivity.this)
                        .urls(urlList.get(0), urlList.get(0), urlList.get(0), urlList.get(0))
                        .targetParent(rlImages)
                        .imageViewIds(R.id.ivImageA, R.id.ivImageB, R.id.ivImageC, R.id.ivImageD)
                        .linkage(true)
                        .position(3)
                        .show();
            }
        });
        findViewById(R.id.tvColumnCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setItems(new String[]{"1", "2", "3", "4", "5", "6"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((TextView) findViewById(R.id.tvColumnCount)).setText("列数：" + (which + 1));
                        columnCount = which + 1;
                        baseAdapter.width = ScreenUtils.getScreenWidth(MainActivity.this) / columnCount;
                        mAdapter.width = ScreenUtils.getScreenWidth(MainActivity.this) / columnCount;
                        grid.setNumColumns(which + 1);
                        gridLayoutManager.setSpanCount(which + 1);
                        baseAdapter.notifyDataSetChanged();
                        mAdapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });

        findViewById(R.id.tvScaleType).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setItems(scaleTypeArr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        scaleType = ImageView.ScaleType.valueOf(scaleTypeArr[which]);
                        baseAdapter.setScaleType(scaleType);
                        mAdapter = new MAdapter(MainActivity.this, recyclerView);
                        mAdapter.setScaleType(scaleType);
                        mAdapter.setData(imageUrls);
                        recyclerView.setAdapter(mAdapter);
                    }
                }).show();
            }
        });
        findViewById(R.id.tvParentType).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setItems(parentTypeArr, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            grid.setVisibility(View.VISIBLE);
                            recyclerView.setVisibility(View.GONE);
                            rlImages.setVisibility(View.GONE);
                        } else if (which == 1) {
                            grid.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            rlImages.setVisibility(View.GONE);
                        } else if (which == 2) {
                            grid.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.GONE);
                            rlImages.setVisibility(View.VISIBLE);
                            Glide.with(MainActivity.this).load(urlList.get(0)).into(ivImageA);
                            Glide.with(MainActivity.this).load(urlList.get(0)).into(ivImageB);
                            Glide.with(MainActivity.this).load(urlList.get(0)).into(ivImageC);
                            Glide.with(MainActivity.this).load(urlList.get(0)).into(ivImageD);
                        }
                    }
                }).show();
            }
        });
        findViewById(R.id.tvSource).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setItems(new String[]{"本地图片", "网络图片"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            pickImage();
                        } else if (which == 1) {
                            urls.clear();
                            urls.addAll(urlList);
                            imageUrls.clear();
                            imageUrls.addAll(urls);
                            baseAdapter.setImageUrls(urls);
                            mAdapter.setData(imageUrls);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }).show();
            }
        });

        final ImageView ivSingle = findViewById(R.id.ivSingle);
        Glide.with(this).load(urlList.get(0)).into(ivSingle);
        ivSingle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ImageBrowser.Builder(MainActivity.this)
                        .urls(urlList.get(0)).target(v)
                        .show();
            }
        });
    }

    static class MAdapter extends RecyclerView.Adapter<VH> {
        Context context;
        ArrayList<String> imageUrls = new ArrayList();
        int width;

        public void setScaleType(ImageView.ScaleType scaleType) {
            this.scaleType = scaleType;
        }

        private ImageView.ScaleType scaleType = ImageView.ScaleType.CENTER_CROP;
        private RecyclerView recyclerView;

        public MAdapter(Context context, RecyclerView recyclerView) {
            this.context = context;
            this.recyclerView = recyclerView;
            width = ScreenUtils.getScreenWidth(context) / columnCount;
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(final VH holder, int position) {
            holder.ivImage.setScaleType(scaleType);
            ViewGroup.LayoutParams layoutParams = holder.ivImage.getLayoutParams();
            layoutParams.width = width;
            layoutParams.height = width;
            holder.ivImage.setLayoutParams(layoutParams);
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

    public void pickImage() {
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
            width = ScreenUtils.getScreenWidth(context) / columnCount;
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
            }
            convertView.setLayoutParams(new AbsListView.LayoutParams(width, width));
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
