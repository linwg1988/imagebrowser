# imagebrowser

这是一款图片浏览器.</br>
想要方便自己app中点击浏览大图。<br>

gradled中的引用：
~~~Java
gradle:
dependencies {
    compile 'org.linwg1988:imagebrowser:1.2.3'
}
~~~
支持图片列表类型为RecyclerView，ListView和GridView。在相对复杂的列表子项中提供ImageView的id配置，<br>
便于精确获取图片的初始位置，使过渡动画正确完成。此外移除了1.1.2版本以前的isCenterCrop参数设定，改<br>
为提供原始ImageView的ScaleType的方法，暂时只支持CENTER_CROP,FIT_CENTER,FIT_XY,CENTER_INSIDE这4个类<br>
型，其中CENTER_INSIDE在遇到图片比控件小的时候依然存在问题，建议暂不使用，前三个ScaleType相信已经够用了。<br>
由于需要响应back事件，我们需要在基类Activity中增加判断，代码如下：<br>
~~~Java
@Override
public void onBackPressed() {
    if(ImageBrowser.onBackPressed(this)){
        return;
    }
    super.onBackPressed();
}
~~~
好了，简单的东西说完了，那么开始比较复杂的东西吧！

首先说一下ImageLoaderFactory和IImageLoader：出于和[imagepicker](https://github.com/linwg1988/imagepicker)一样的考虑，<br>
每个开发者使用的图片加载框架不同，或UIL或Glide或Picasso或Fresco,那么提供一个统一的图片加载入口就很有必要了，这里我写的也很简单，<br>
在程序的入口处设置一个全局的IImageLoader即可：
~~~Java
ImageLoaderFactory.set(new IImageLoader() {
        ...
    });
~~~
本库默认使用Glide加载图片，所以在库中直接写了这个实现，大家可以照着实现即可：
~~~Java
public class GlideLoaderStrategy implements IImageLoader{

    //加载图片，最后一个参数是一个回调，当图片资源加载成功后主动调用，以达到动画缩放位移的效果（ps：回调很重要）
    void loadImage(Context context, String url, ImageView imageView,IResourceReadyCallback callback){
        ...
    }
    
    //判断图片是否已经成功加载到ImageView中
    boolean isDrawableLoadingCompleted(ImageView view){
        ...
    }
    
    //v1.2.2不再使用此方法加载缩略图
    //加载缩略图，可以自己实现加载缩略图的方法，并返回true，若果返回false的话，将默认使用loadImage加载缩略图
    //boolean loadThumb(Context context,String originUrl,String thumbUrl,ImageView imageView,
    //        IResourceReadyCallback callback){
    //    ...
    //}
    
    //不同图片加载框架获取ImageView上面的Bitmap不太一致，所以将获取Bitmap的方法提取出来（ps：如果你不需要用到浏览
    //器的下载功能的话，可以不实现这个方法）
    Bitmap getBitmapFromImageView(ImageView view){
        ...
    }
}
~~~
如果你也用Glide，那就可以直接使用库中自带的这个类。<br>
接下来是使用图片浏览器了，比较简单：
~~~Java
new ImageBrowser.Builder(MainActivity.this)
        .urls(imageUrls)
        .thumbUrls(thumbUrls)
        .mode(ImageBrowser.Mode.DOWNLOAD)
        .setDownloadListener(new ImageBrowser.OnDownloadClickListener() {
                @Override
                public void onDownloadBtnClick(Bitmap bitmap) {
                    //TODO DOWNLOAD
                }
            })
        .targetParent(parent)//RecycleView或AbsListView
        .target(view)//实际点击的View，通常为被点击的ImageView，或为parent的itemView
        .imageViewId(R.id.image_view)//如果target并不是实际的imageVIew，必须指定itemView中imageView的id
        .linkage(true)//设置联动，外层列表将跟随内部ViewPager的滑动进行更新滚动位置
        .position(position)//起始索引，默认是第一张的话就没必要设置了
        .show();
~~~

主要说一说重要的几个参数

###a)`mode` <br>
####提供了4个模式,<br>
*NONE*-默认无操作按钮，<br>
*DOWNLOAD*-提供下载按钮，这里实现IImageLoader的getBitmapFromImageView就有必要了；<br>
*DELETE*-移除按钮，提供移除按钮，仅移除当前浏览器图片，不影响原始容器，如果想移除原始容器的图片，请在回调中处理；<br>
*CUSTOM*-自定义按钮模式，提供文本和图片按钮，自行设置；<br>
###b)`targetParent` <br>
这个参数就是你原始图片的父容器，比如说GridView，ListView,RecycleView<br>
九宫格的图片父容器我想大家应该见得非常多了，没错就是这样的家伙，需要注意的是这个父容器里面的子view个数必须和传入<br>
的url数目一致（AdapterView的话适配器中的getCount()返回值必须和url的数目一致），否则动画效果难以保证；<br>
###c)`imageViewId` <br>
这个参数是实际列表控件中ImageView控件的id，有时候列表项有间隙或其它子项的时候，设置这个id将使图片的原始位置更加精确；<br>
###d)`linkage` <br>
此参数设置为true时，图片浏览器与外层的列表控件将形成联动，需要注意的是targetParent和imageViewId必须同时设置才能起到作用；<br>
###e)`target` <br>
此参数为被点击的图片实际控件，被设置后，浏览器在启动时将隐藏该图片，浏览器关闭时恢复相应位置的控件可见。<br>

好了，用法和注意点就是这样了，本库中浏览大图使用的控件是开源控件[PhotoView](https://github.com/chrisbanes/PhotoView)，我个人对<br>
里面的源码做了一定的修改，实现的方式也是效仿PhotoView中对图片的处理。欢迎大家使用，有什么问题的话，也请提出。<br>

#1.1.4
对于手机底部导航栏影响指示器的显示提供了设置底部指示器内间距的方法使用setIndicatorPaddingBottom(int paddingBottom)<br>
#1.2.0
Gradle版本升级到4.1.1, Glide版本到4.9.0<br>
#1.2.1
简化单张图片点击浏览的api，只需要设置url以及target即可.<br>
#1.2.2
修复RecycleView的直接itemView不是ImageView时解析图片位置的错误，修复RecycleView动画位置异常的bug，去除缩略图动画，使初始动画更加平滑.<br>
因为去除了缩略图占位，所以加载原图是网络大图时可能比较慢，除非该图片原图已经在本地缓存已经存在.<br>
#1.2.3
修复动画初始取值错误的问题<br>

最后上一下效果图：<br>
![](http://ofj4ai6ke.bkt.clouddn.com/local.gif)<br>
这是加载本地图片的时候<br>

![](http://ofj4ai6ke.bkt.clouddn.com/net.gif)<br>
这是加载网络图片的时候的效果<br>






