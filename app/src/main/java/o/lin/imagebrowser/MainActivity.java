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
    private BaseAdapter baseAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ImagePicker.setImageLoader(new ImageLoader() {
            @Override
            public void loadImage(Context context, String imgPath, ImageView targetView) {
                Glide.with(context).load(imgPath).into(targetView);
            }
        });
        ImageLoaderFactory.set(new GlideLoaderStrategy());
        GridView grid = (GridView) findViewById(R.id.grid);
        baseAdapter = new ImageAdapter();
        grid.setAdapter(baseAdapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                new ImageBrowser.Builder(MainActivity.this)
                        .urls(imageUrls)
                        .thumbSize(120)
                        .targetParent(parent)
                        .originIsCenterCrop(true)
                        .position(position)
                        .show();
            }
        });
    }

    public void pickImage(View view){
        new ImagePicker.Builder().maxPictureNumber(39).build().show(getSupportFragmentManager(),"imagePicker");
    }

    @Override
    public void onImagesPicked(List<String> imgPaths, String selectedDir) {
        imageUrls.clear();
        imageUrls.addAll(imgPaths);
        baseAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCameraCallBack(String imgPath) {

    }

    class ImageAdapter extends BaseAdapter{
        int width;

        public ImageAdapter() {
            width = ScreenUtils.getScreenWidth(getApplicationContext()) / 3;
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
                convertView = new ImageView(getApplicationContext());
                convertView.setLayoutParams(new AbsListView.LayoutParams(width, width));

            }
            ImageView img = (ImageView) convertView;
            img.setScaleType(ImageView.ScaleType.CENTER_CROP);
            Glide.with(MainActivity.this).load(imageUrls.get(position)).into(img);
            return convertView;
        }
    }
}
