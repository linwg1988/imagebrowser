package o.lin.imagebrowser;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.wglin.imagepicker.ImageLoader;
import org.wglin.imagepicker.ImagePicker;
import org.wglin.imagepicker.ScreenUtils;

import java.util.ArrayList;
import java.util.List;

import linwg.GlideLoaderStrategy;
import linwg.ImageBrowser;
import linwg.strategy.ImageLoaderFactory;

public class MainActivity extends AppCompatActivity implements ImagePicker.OnImagePickerListener{
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

    ArrayList<String> urls = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImagePicker.setImageLoader(new ImageLoader() {
            @Override
            public void loadImage(Context context, String imgPath, ImageView targetView) {
                Glide.with(context).load(imgPath).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).into(targetView);
            }
        });
        ImageLoaderFactory.set(new GlideLoaderStrategy());
        final GridView grid = (GridView) findViewById(R.id.grid);

        baseAdapter = new ImageAdapter(imageUrls,this);
        grid.setAdapter(baseAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new ImageBrowser.Builder(MainActivity.this)
                        .urls(baseAdapter.getImageUrls())
                        .targetParent(parent)
                        .originIsCenterCrop(true)
                        .setDownloadListener(new ImageBrowser.OnDownloadClickListener() {
                            @Override
                            public void onDownloadBtnClick(Bitmap bitmap) {

                            }
                        })
                        .position(position)
                        .show();
            }
        });

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
                baseAdapter.setImageUrls(urls);
                baseAdapter.notifyDataSetChanged();
            }
        });
    }

    public void pickImage(View view){
        new ImagePicker.Builder().maxPictureNumber(99).build().show(getSupportFragmentManager(),"imagePicker");
    }

    @Override
    public void onImagesPicked(List<String> imgPaths, String selectedDir) {
        imageUrls.clear();
        imageUrls.addAll(imgPaths);
        baseAdapter.setImageUrls(imageUrls);
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCameraCallBack(String imgPath) {

    }

    private static class ImageAdapter extends BaseAdapter{
        int width;

        private ArrayList<String> imageUrls;
        private Context context;

        public void setImageUrls(ArrayList<String> imageUrls) {
            this.imageUrls = imageUrls;
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
                convertView.setLayoutParams(new AbsListView.LayoutParams(width, width));

            }
            ImageView img = (ImageView) convertView;
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(context).load(imageUrls.get(position)).asBitmap().into(img);
            return convertView;
        }
    }

    @Override
    public void onBackPressed() {
        if(ImageBrowser.onBackPressed(this)){
            return;
        }
        super.onBackPressed();
    }
}
