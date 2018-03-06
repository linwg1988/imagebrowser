package o.lin.imagebrowser;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

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
    RecyclerView recyclerView;
    ArrayList<String> urls = new ArrayList<>();
    private MAdapter mAdapter;

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

        baseAdapter = new ImageAdapter(imageUrls,this);
        grid.setAdapter(baseAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new ImageBrowser.Builder(MainActivity.this)
                        .urls(baseAdapter.getImageUrls())
                        .targetParent(parent)
                        .imageViewId(R.id.ivImage2)
                        .target(view)
                        .linkage(true)
                        .originIsCenterCrop(true)
                        .position(position)
                        .show();
            }
        });
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        mAdapter = new MAdapter();
        recyclerView.setAdapter(mAdapter);

        findViewById(R.id.label).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                urls.clear();
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
                imageUrls.clear();
                imageUrls.addAll(urls);
                baseAdapter.setImageUrls(urls);
                mAdapter.notifyDataSetChanged();
            }
        });
        findViewById(R.id.gridViewBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grid.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
            }
        });
        findViewById(R.id.recyclerViewBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                grid.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        });
        findViewById(R.id.tvColumnCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(MainActivity.this).setItems(new String[]{"1", "2", "3"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ((TextView)findViewById(R.id.tvColumnCount)).setText(which+1+"");
                        grid.setNumColumns(which+1);
                        gridLayoutManager.setSpanCount(which+1);
                        baseAdapter.notifyDataSetChanged();
                        mAdapter.notifyDataSetChanged();
                    }
                }).show();
            }
        });
    }

    class MAdapter extends RecyclerView.Adapter<VH> {

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.item_image, parent, false);
            return new VH(view);
        }

        @Override
        public void onBindViewHolder(final VH holder, int position) {
            Glide.with(MainActivity.this).load(imageUrls.get(holder.getLayoutPosition())).into(holder.ivImage);
            holder.ivImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ImageBrowser.Builder(MainActivity.this)
                            .urls(imageUrls)
                            .targetParent(recyclerView)
                            .imageViewId(R.id.ivImage)
                            .originIsCenterCrop(true)
                            .target(holder.ivImage)
                            .linkage(true)
                            .position(holder.getLayoutPosition())
                            .show();
                }
            });
        }

        @Override
        public int getItemCount() {
            return imageUrls.size();
        }
    }

    class VH extends RecyclerView.ViewHolder {
        ImageView ivImage;

        public VH(View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
        }
    }

    public void pickImage(View view) {
        new ImagePicker.Builder().maxPictureNumber(99).build().show(getSupportFragmentManager(), "imagePicker");
    }

    @Override
    public void onImagesPicked(List<String> imgPaths, String selectedDir) {
        imageUrls.clear();
        imageUrls.addAll(imgPaths);
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
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(context).load(imageUrls.get(position)).into(img);
            return convertView;
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
