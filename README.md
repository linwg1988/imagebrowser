# imagebrowser

这是一款图片浏览器.</br>
网上的图片浏览器有很多，大家挑着使用哈。本库供学习使用，若你想用到应用中，我也不会阻拦，哈哈。<br>

话不多说，先看看gradled中的引用：
~~~Java
gradle:
dependencies {
    compile 'org.linwg1988:imagebrowser:1.0.2'
}
~~~
原先1.0.1版本还是使用 DialogFragment 来实现的，现在改成Fragment实现，相对来说减少了Theme对页面的影响<br>
（DialogFrament在显示的时候原始界面的布局可能会上拉），但也增加了一丁点的麻烦，由于需要响应back事件，<br>
我们需要在基类Activity中增加判断，代码如下：
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
ImageLoaderFactory.set(new IImageLoader() {...});
~~~
本库默认使用Glide加载图片，所以在库中直接写了这个实现，大家可以照着实现即可：
~~~Java
public class GlideLoaderStrategy implements IImageLoader{
    //加载图片，最后一个参数是一个回调，当图片资源加载成功后主动调用，以达到动画缩放位移的效果（ps：回调很重要）
    void loadImage(Context context, String url, ImageView imageView,IResourceReadyCallback callback){...}
    //判断图片是否已经成功加载到ImageView中
    boolean isDrawableLoadingCompleted(ImageView view){...}
    //加载缩略图，可以自己实现加载缩略图的方法，并返回true，若果返回false的话，将默认使用loadImage加载缩略图
    boolean loadThumb(Context context,String originUrl,String thumbUrl,ImageView imageView,IResourceReadyCallback callback){...}
    //不同图片加载框架获取ImageView上面的Bitmap不太一致，所以将获取Bitmap的方法提取出来（ps：如果你不需要用到浏览器的下载功能的话，
    可以不实现这个方法）
    Bitmap getBitmapFromImageView(ImageView view){...}
}
~~~
前戏已近搞定，是不是感觉有点麻烦，哈哈，想高潮前戏还是很重要的嘛~~（老司机开车，新手请跳过），万一你也用Glide，那就可以忽略了嘛。<br>
接下来是使用图片浏览器了，很简单，一句话搞定：
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
                   .targetParent(parent)//想要支持动画的话，这个很重要
                   .thumbSize(120)//缩略图的尺寸，不设值的话会自动检测原始图的大小，如果没有设置targetParent的话则默认100dp
                   .target(view)//这个暂时先不要用，效果不好
                   .originIsCenterCrop(true)//重要
                   .position(position)//起始索引，默认是第一张的话就没必要设置了
                   .show();
~~~
一些参数名言上看过去就明白的我就不再解释了，这里主要说一说重要的几个参数

a)`mode` ：提供了4个模式,默认无操作按钮，<br>
DOWNLOAD-提供下载按钮，这里实现IImageLoader的getBitmapFromImageView就有必要了；<br>
DELETE-移除按钮，提供移除按钮，仅移除当前浏览器图片，不影响原始容器，如果想移除原始容器的图片，请在回调中处理；<br>
CUSTOM-自定义按钮模式，提供文本和图片按钮，自行设置；
b)`targetParent` ：这个很重要，很重要，很重要，关系到动画效果，这个参数就是你原始图片的父容器，比如说GridView，<br>
九宫格的图片父容器我想大家应该见得非常多了，没错就是这样的家伙，需要注意的是这个父容器里面的子view个数必须和传入<br>
的url数目一致（AdapterView的话适配器中的getCount()返回值必须和url的数目一致），否则动画效果难以保证；
c)`originIsCenterCrop` ：通常在排列我们原始图片的时候我们为了整齐会将ImageView的ScaleType设置为CENTER_CROP，<br>
那么这里就必须将originIsCenterCrop设置为true了，以保证最后结束动画的效果，当然如果你使用其他的ScaleType，就请忽略掉这个设置吧；

好了，用法和注意点就是这样了，欢迎大家使用，有什么问题的话，也请提出，时时刻刻欢迎打脸啊...受虐倾向不要太明显...





