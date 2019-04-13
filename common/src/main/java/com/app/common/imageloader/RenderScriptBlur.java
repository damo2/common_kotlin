////package com.app.common.imageloader;
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.os.Build;
//import android.os.Handler;
//import android.os.Looper;
//import android.renderscript.Allocation;
//import android.renderscript.Element;
//import android.renderscript.RenderScript;
//import android.renderscript.ScriptIntrinsicBlur;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.DecelerateInterpolator;
//import android.widget.ImageView;
//
//import java.lang.ref.WeakReference;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executors;
//
//
///**
// * Created by hucanhui on 2017/11/13.
// */
//
//public class BitmapBlurUtil {
//    private static final float BITMAP_SCALE = 1f;//图片缩放值
//    private static final int BLUR_RADIUS = 20;//模糊度
//    private Handler handler;
//    private ExecutorService executorService;
//    private volatile static BitmapBlurUtil instance;
//
//    private BitmapBlurUtil(){
//        handler = new Handler(Looper.getMainLooper());
//        executorService = Executors.newCachedThreadPool();
//    }
//
//    public synchronized static BitmapBlurUtil getInstance(){
//        if (instance == null)
//            synchronized (BitmapBlurUtil.class){
//                if (instance == null){
//                    instance = new BitmapBlurUtil();
//                }
//            }
//        return instance;
//    }
//
//    private Bitmap blur(Context context, Bitmap bitmap) {
//        return blur(context, bitmap, BITMAP_SCALE, BLUR_RADIUS);
//    }
//
//    private Bitmap blur(Context context, Bitmap bitmap, float bitmap_scale) {
//        return blur(context, bitmap, bitmap_scale, BLUR_RADIUS);
//    }
//
//    private Bitmap blur(Context context, Bitmap bitmap, int blur_radius) {
//        return blur(context, bitmap, BITMAP_SCALE, blur_radius);
//    }
//
//    public void setImageBlurBitmap(Context context, ImageView imageView, Bitmap bitmap){
//        setImageBlurBitmap(context, imageView, bitmap, BITMAP_SCALE, BLUR_RADIUS);
//    }
//
//    public void setImageBlurBitmap(Context context, ImageView imageView, Bitmap bitmap, float bitmap_scale){
//        setImageBlurBitmap(context, imageView, bitmap, bitmap_scale, BLUR_RADIUS);
//    }
//
//    public void setImageBlurBitmap(Context context, ImageView imageView, Bitmap bitmap, int blur_radius){
//        setImageBlurBitmap(context, imageView, bitmap, BITMAP_SCALE, blur_radius);
//    }
//
//    /**
//     *
//     * @param context
//     * @param imageView
//     * @param bitmap
//     * @param bitmap_scale 需要缩放的倍数 图片压缩后可加快速度
//     * @param blur_radius 模糊度 越大越模糊
//     */
//    public void setImageBlurBitmap(Context context, final ImageView imageView, final Bitmap bitmap, final float bitmap_scale, final int blur_radius) {
//        if (bitmap == null || context == null) return;
//        final WeakReference<Context> weakReference = new WeakReference<Context>(context);
//        executorService.execute(new Runnable() {
//            @Override
//            public void run() {
//                if (weakReference.get() != null){
//                    final Bitmap blurBitmap = blur(weakReference.get(), bitmap, bitmap_scale, blur_radius);
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (imageView != null && weakReference.get() != null){
//                                imageView.setImageBitmap(blurBitmap);
//                                AlphaAnimation fadeImage = new AlphaAnimation(0.0F, 1.0F);
//                                fadeImage.setDuration(300);
//                                fadeImage.setInterpolator(new DecelerateInterpolator());
//                                imageView.startAnimation(fadeImage);
//                            }
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    /**
//     * 该方法耗时 最好放异步执行
//     * @param context
//     * @param bitmap
//     * @param bitmap_scale
//     * @param blur_radius
//     * @return
//     */
//    private Bitmap blur(Context context, Bitmap bitmap, float bitmap_scale, int blur_radius) {
//        //先对图片进行压缩然后再blur 缩短时间
//        Bitmap inputBitmap = bitmap;
//        if (bitmap_scale < 1){
//            inputBitmap = Bitmap.createScaledBitmap(bitmap, Math.round(bitmap.getWidth() * bitmap_scale),
//            Math.round(bitmap.getHeight() * bitmap_scale), false);
//        }
//        Bitmap outputBitmap;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {//rendScript只适用于Build.VERSION_CODES大于17
//            //创建空的Bitmap用于输出
//            outputBitmap = Bitmap.createBitmap(inputBitmap);
//            //初始化Renderscript
//            RenderScript rs = RenderScript.create(context);
//            ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
//            //native层分配内存空间
//            Allocation tmpIn = Allocation.createFromBitmap(rs, inputBitmap);
//            Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
//            //设置blur的半径然后进行blur
//            theIntrinsic.setRadius(blur_radius);
//            theIntrinsic.setInput(tmpIn);
//            theIntrinsic.forEach(tmpOut);
//            //拷贝blur后的数据到java缓冲区中
//            tmpOut.copyTo(outputBitmap);
//            //销毁Renderscript
//            rs.destroy();
//        } else {
//            //兼容低版本
//            outputBitmap = fastblur(inputBitmap, blur_radius);
//        }
//        return outputBitmap;
//    }
//
//    private static Bitmap fastblur(Bitmap sentBitmap, int radius) {
//        Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);
//        if(radius < 1) {
//            return null;
//        } else {
//            int w = bitmap.getWidth();
//            int h = bitmap.getHeight();
//            int[] pix = new int[w * h];
//            bitmap.getPixels(pix, 0, w, 0, 0, w, h);
//            int wm = w - 1;
//            int hm = h - 1;
//            int wh = w * h;
//            int div = radius + radius + 1;
//            int[] r = new int[wh];
//            int[] g = new int[wh];
//            int[] b = new int[wh];
//            int[] vmin = new int[Math.max(w, h)];
//            int divsum = div + 1 >> 1;
//            divsum *= divsum;
//            int[] dv = new int[256 * divsum];
//
//            int i;
//            for(i = 0; i < 256 * divsum; ++i) {
//                dv[i] = i / divsum;
//            }
//
//            int yi = 0;
//            int yw = 0;
//            int[][] stack = new int[div][3];
//            int r1 = radius + 1;
//
//            int rsum;
//            int gsum;
//            int bsum;
//            int x;
//            int y;
//            int p;
//            int stackpointer;
//            int stackstart;
//            int[] sir;
//            int rbs;
//            int routsum;
//            int goutsum;
//            int boutsum;
//            int rinsum;
//            int ginsum;
//            int binsum;
//            for(y = 0; y < h; ++y) {
//                bsum = 0;
//                gsum = 0;
//                rsum = 0;
//                boutsum = 0;
//                goutsum = 0;
//                routsum = 0;
//                binsum = 0;
//                ginsum = 0;
//                rinsum = 0;
//
//                for(i = -radius; i <= radius; ++i) {
//                    p = pix[yi + Math.min(wm, Math.max(i, 0))];
//                    sir = stack[i + radius];
//                    sir[0] = (p & 16711680) >> 16;
//                    sir[1] = (p & '\uff00') >> 8;
//                    sir[2] = p & 255;
//                    rbs = r1 - Math.abs(i);
//                    rsum += sir[0] * rbs;
//                    gsum += sir[1] * rbs;
//                    bsum += sir[2] * rbs;
//                    if(i > 0) {
//                        rinsum += sir[0];
//                        ginsum += sir[1];
//                        binsum += sir[2];
//                    } else {
//                        routsum += sir[0];
//                        goutsum += sir[1];
//                        boutsum += sir[2];
//                    }
//                }
//
//                stackpointer = radius;
//
//                for(x = 0; x < w; ++x) {
//                    r[yi] = dv[rsum];
//                    g[yi] = dv[gsum];
//                    b[yi] = dv[bsum];
//                    rsum -= routsum;
//                    gsum -= goutsum;
//                    bsum -= boutsum;
//                    stackstart = stackpointer - radius + div;
//                    sir = stack[stackstart % div];
//                    routsum -= sir[0];
//                    goutsum -= sir[1];
//                    boutsum -= sir[2];
//                    if(y == 0) {
//                        vmin[x] = Math.min(x + radius + 1, wm);
//                    }
//
//                    p = pix[yw + vmin[x]];
//                    sir[0] = (p & 16711680) >> 16;
//                    sir[1] = (p & '\uff00') >> 8;
//                    sir[2] = p & 255;
//                    rinsum += sir[0];
//                    ginsum += sir[1];
//                    binsum += sir[2];
//                    rsum += rinsum;
//                    gsum += ginsum;
//                    bsum += binsum;
//                    stackpointer = (stackpointer + 1) % div;
//                    sir = stack[stackpointer % div];
//                    routsum += sir[0];
//                    goutsum += sir[1];
//                    boutsum += sir[2];
//                    rinsum -= sir[0];
//                    ginsum -= sir[1];
//                    binsum -= sir[2];
//                    ++yi;
//                }
//
//                yw += w;
//            }
//
//            for(x = 0; x < w; ++x) {
//                bsum = 0;
//                gsum = 0;
//                rsum = 0;
//                boutsum = 0;
//                goutsum = 0;
//                routsum = 0;
//                binsum = 0;
//                ginsum = 0;
//                rinsum = 0;
//                int yp = -radius * w;
//
//                for(i = -radius; i <= radius; ++i) {
//                    yi = Math.max(0, yp) + x;
//                    sir = stack[i + radius];
//                    sir[0] = r[yi];
//                    sir[1] = g[yi];
//                    sir[2] = b[yi];
//                    rbs = r1 - Math.abs(i);
//                    rsum += r[yi] * rbs;
//                    gsum += g[yi] * rbs;
//                    bsum += b[yi] * rbs;
//                    if(i > 0) {
//                        rinsum += sir[0];
//                        ginsum += sir[1];
//                        binsum += sir[2];
//                    } else {
//                        routsum += sir[0];
//                        goutsum += sir[1];
//                        boutsum += sir[2];
//                    }
//
//                    if(i < hm) {
//                        yp += w;
//                    }
//                }
//
//                yi = x;
//                stackpointer = radius;
//
//                for(y = 0; y < h; ++y) {
//                    pix[yi] = -16777216 & pix[yi] | dv[rsum] << 16 | dv[gsum] << 8 | dv[bsum];
//                    rsum -= routsum;
//                    gsum -= goutsum;
//                    bsum -= boutsum;
//                    stackstart = stackpointer - radius + div;
//                    sir = stack[stackstart % div];
//                    routsum -= sir[0];
//                    goutsum -= sir[1];
//                    boutsum -= sir[2];
//                    if(x == 0) {
//                        vmin[y] = Math.min(y + r1, hm) * w;
//                    }
//
//                    p = x + vmin[y];
//                    sir[0] = r[p];
//                    sir[1] = g[p];
//                    sir[2] = b[p];
//                    rinsum += sir[0];
//                    ginsum += sir[1];
//                    binsum += sir[2];
//                    rsum += rinsum;
//                    gsum += ginsum;
//                    bsum += binsum;
//                    stackpointer = (stackpointer + 1) % div;
//                    sir = stack[stackpointer];
//                    routsum += sir[0];
//                    goutsum += sir[1];
//                    boutsum += sir[2];
//                    rinsum -= sir[0];
//                    ginsum -= sir[1];
//                    binsum -= sir[2];
//                    yi += w;
//                }
//            }
//
//            bitmap.setPixels(pix, 0, w, 0, 0, w, h);
//            return bitmap;
//        }
//    }
//}
//
//---------------------
//
//本文来自 辉son 的CSDN 博客 ，全文地址请点击：https://blog.csdn.net/u014798175/article/details/79570401?utm_source=copy