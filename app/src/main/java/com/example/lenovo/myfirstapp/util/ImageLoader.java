package com.example.lenovo.myfirstapp.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.DisplayMetrics;
import android.util.LruCache;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * 图片加载类
 */
public class ImageLoader {

    private static ImageLoader mInstance;

    /**
     * 图片缓存的核心对象
     */
    private LruCache<String, Bitmap> mLruCache;

    /**
     *  线程池
     */
    private ExecutorService mThreadPool;
    private static final int DEAFULT_THREAD_COUNT = 1;
    /**
     *  队列的调度方式
     */
    private Type mType = Type.LIFO;
    /**
     * 任务列表
     */
    private LinkedList<Runnable> mTaskQuene;

    /**
     *  后台轮询线程
     */
    private Thread mPoolThread;
    private Handler mPoolThreadHandler;
    private Semaphore mSemaphorePoolThreadHandler = new Semaphore(0);
    private Semaphore mSemaphoreThreadPool;
    /**
     * UI线程
     */
    private Handler mUIhandler;
    public enum Type {
        FIFO, LIFO;
    }


    private ImageLoader(int ThreadCount, Type type) {
        init(ThreadCount, type);

    }

    /**
     * 初始化操作
     * @param threadCount
     * @param type
     */
    private void init(int threadCount, Type type) {
        //后台轮询线程
        mPoolThread = new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                mPoolThreadHandler = new Handler() {

                    @Override
                    public void handleMessage(Message msg) {
                        //线程池去取一个任务进行执行
                        mThreadPool.execute(getTask());
                        try {
                            mSemaphoreThreadPool.acquire();  //限制并行任务量
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                };
                //释放一个信号量
                mSemaphorePoolThreadHandler.release();
                Looper.loop();
            }
        };
        mPoolThread.start();

        //获取我们应用的最大内存
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacaheMemory = maxMemory / 8;

        mLruCache = new LruCache<String, Bitmap>(cacaheMemory) {
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getRowBytes() * value.getHeight();
            }
        };

        //创建线程池
        mThreadPool = Executors.newFixedThreadPool(threadCount);
        mTaskQuene = new LinkedList<Runnable>();
        mType = type;
        mSemaphoreThreadPool = new Semaphore(threadCount);
    }

    /**
     * 从任务队列取出一个方法
     * @return
     */
    private Runnable getTask() {
        if(mType == Type.FIFO) {
            return mTaskQuene.removeFirst();
        } else if (mType == Type.LIFO) {
            return mTaskQuene.removeLast();
        }
        return null;
    }

    /**
     * 根据path为imageview设置图片
     * @param path
     * @param imageView
     */
    public void loadImage(final String path, final ImageView imageView) {
        imageView.setTag(path);
        if(mUIhandler == null) {
            mUIhandler = new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    //获取得到图片，为imageView回调设置图片
                    ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
                    Bitmap bm = holder.bitmap;
                    ImageView imageView = holder.imageView;
                    String path = holder.path;

                    if(imageView.getTag().toString().equals(path)) {
                        imageView.setImageBitmap(bm);
                    }
                }
            };
        }
        //根据path在缓存中获取bitmap
        Bitmap bm = getBitmapFromLruCache(path);
        if(bm != null) {
            refreashBitmap(path, imageView, bm);
        }else {
            addTask(new Runnable() {
                @Override
                public void run() {
                    //加载图片
                    //图片的压缩
                    //1.获得图片需要显示的大小
                    ImageSize imageSize= getImageViewSize(imageView);
                    //2.压缩图片
                    Bitmap bm = decodeSampledBitmapFromPath(imageSize.width, imageSize.height, path);
                    //3.把图片加入缓存
                    addBitmapToLruCache(path,bm);
                    refreashBitmap(path, imageView, bm);

                    mSemaphoreThreadPool.release();
                }
            });
        }

    }

    private void refreashBitmap(String path, ImageView imageView, Bitmap bm) {
        Message message = Message.obtain();
        ImgBeanHolder imageHolder = new ImgBeanHolder();
        imageHolder.bitmap = bm;
        imageHolder.imageView = imageView;
        imageHolder.path = path;
        message.obj = imageHolder;
        mUIhandler.sendMessage(message);
    }

    /**
     * 将图片加入LruCache
     * @param path
     * @param bm
     */
    private void addBitmapToLruCache(String path, Bitmap bm) {
        if (getBitmapFromLruCache(path) == null) {
            if(bm != null) {
                mLruCache.put(path, bm);
            }
        }
    }

    /**
     * 根据图片显示的宽和高进行压缩
     * @param width
     * @param height
     * @param path
     * @return
     */
    private Bitmap decodeSampledBitmapFromPath(int width, int height, String path) {
        //获取图片的宽和高，并不把图片加载到内存中
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, option);

        option.inSampleSize = caculateInSimpleSize(option,width,height);
        //使用获取到的inSampleSize再次解析图片
        option.inJustDecodeBounds = false;
        Bitmap bitmap = BitmapFactory.decodeFile(path, option);

        return bitmap;
    }

    /**
     * 根据需求的宽和高以及图片实际的宽和高计算压缩比
     * @param option
     * @param reswidth
     * @param resheight
     * @return
     */
    private int caculateInSimpleSize(BitmapFactory.Options option, int reswidth, int resheight) {
        int width = option.outWidth;
        int height = option.outHeight;

        int simpleSize = 1;

        if(width > reswidth || height > resheight) {
            int widthRadio = Math.round(width * 1.0f / reswidth);
            int heightRadio = Math.round(height * 1.0f / resheight);

            simpleSize = Math.max(widthRadio,heightRadio);
        }
        return simpleSize;
    }

    /**
     * 根据imageView获取适当的压缩的宽和高
     * @param imageView
     * @return
     */
    private ImageSize getImageViewSize(ImageView imageView) {
        ImageSize imageSize = new ImageSize();

        DisplayMetrics dm = imageView.getContext().getResources().getDisplayMetrics();

        ViewGroup.LayoutParams lp= imageView.getLayoutParams();
        int width = imageView.getWidth();
        if (width <= 0) {
            width = lp.width; //获取imageView在layout中声明的宽度
        }
        if (width <= 0) {
            width = imageView.getMaxWidth(); //检查最大值
        }
        if (width <= 0) {
            width = dm.widthPixels;
        }

        int height = imageView.getHeight();
        if (height <= 0) {
            height = lp.height; //获取imageView在layout中声明的宽度
        }
        if (height <= 0) {
            height = imageView.getMaxHeight(); //检查最大值
        }
        if (height <= 0) {
            height = dm.heightPixels;
        }

        imageSize.width = width;
        imageSize.height = height;
        return imageSize;
    }

    private synchronized void addTask(Runnable runnable) {
        mTaskQuene.add(runnable);
        try {
            if (mPoolThreadHandler == null)
                mSemaphorePoolThreadHandler.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mPoolThreadHandler.sendEmptyMessage(0x110);

    }

    /**
     * 根据path在缓存中获取bitmap
     * @param key
     * @return
     */
    private Bitmap getBitmapFromLruCache(String key) {
        return mLruCache.get(key);
    }

    private class ImageSize {
        int width;
        int height;
    }

    private class ImgBeanHolder {
        Bitmap bitmap;
        ImageView imageView;
        String path;
    }
    public static ImageLoader getmInstance(int threadCount, Type type) {
        if(mInstance == null) {
            synchronized (ImageLoader.class) {
                if(mInstance == null) {
                    mInstance = new ImageLoader(threadCount, type);
                }
            }
        }
        return mInstance;
    }
}
