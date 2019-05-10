package com.app.common.utils

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import com.app.common.extensions.imgGetWidthExt
import java.io.*

/**
 * 图片工具
 */
object CompressConst {
    const val IMAGE_QUALITY = 90
    const val IMAGE_MAXSIZE = 400 //默认图片最大大小 500k
    const val IMAGE_MAXSIZE_SYS = 500 //默认图片最大大小 500k
    const val IMAGE_HEIGHT_MAX = 1280 //默认图片最大高
    const val IMAGE_WIDTH_MAX = 1280 //默认图片最大宽
}
object BitmapUtil {
    private val TAG = "BitmapUtil"

    /**
     * 获取资源drawable的Bitmap
     *
     * @param context
     * @param vectorDrawableId
     * @return
     */
    private fun getBitmap(context: Context, vectorDrawableId: Int): Bitmap? {
        var bitmap: Bitmap? = null
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            val vectorDrawable = context.getDrawable(vectorDrawableId)
            if (vectorDrawable != null) {
                bitmap = Bitmap.createBitmap(vectorDrawable.intrinsicWidth,
                        vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap!!)
                vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
                vectorDrawable.draw(canvas)
            }
        } else {
            bitmap = BitmapFactory.decodeResource(context.resources, vectorDrawableId)
        }
        return bitmap
    }

    //节省每次创建时产生的开销，但要注意多线程操作synchronized
    private val sCanvas = Canvas()

    /**
     * 从一个view创建Bitmap。
     * 注意点：绘制之前要清掉 View 的焦点，因为焦点可能会改变一个 View 的 UI 状态。
     * 来源：https://github.com/tyrantgit/ExplosionField
     *
     * @param view  传入一个 View，会获取这个 View 的内容创建 Bitmap。
     * @param scale 缩放比例，对创建的 Bitmap 进行缩放，数值支持从 0 到 1。
     */
    fun createBitmapFromView(view: View, scale: Float): Bitmap? {
        if (view is ImageView) {
            val drawable = view.drawable
            if (drawable != null && drawable is BitmapDrawable) {
                return drawable.bitmap
            }
        }
        view.clearFocus()
        val bitmap = createBitmapSafely((view.width * scale).toInt(),
                (view.height * scale).toInt(), Bitmap.Config.ARGB_8888, 1)
        if (bitmap != null) {
            synchronized(sCanvas) {
                val canvas = sCanvas
                canvas.setBitmap(bitmap)
                canvas.save()
                canvas.drawColor(Color.WHITE) // 防止 View 上面有些区域空白导致最终 Bitmap 上有些区域变黑
                canvas.scale(scale, scale)
                view.draw(canvas)
                canvas.restore()
                canvas.setBitmap(null)
            }
        }
        return bitmap
    }

    fun createBitmapFromView(view: View): Bitmap? {
        return createBitmapFromView(view, 1f)
    }

    /**
     * 从一个view创建Bitmap。把view的区域截掉leftCrop/topCrop/rightCrop/bottomCrop
     */
    fun createBitmapFromView(view: View, leftCrop: Int, topCrop: Int, rightCrop: Int, bottomCrop: Int): Bitmap? {
        val originBitmap = createBitmapFromView(view) ?: return null
        val cutBitmap = createBitmapSafely(view.width - rightCrop - leftCrop, view.height - topCrop - bottomCrop, Bitmap.Config.ARGB_8888, 1)
                ?: return null
        val canvas = Canvas(cutBitmap)
        val src = Rect(leftCrop, topCrop, view.width - rightCrop, view.height - bottomCrop)
        val dest = Rect(0, 0, view.width - rightCrop - leftCrop, view.height - topCrop - bottomCrop)
        canvas.drawColor(Color.WHITE) // 防止 View 上面有些区域空白导致最终 Bitmap 上有些区域变黑
        canvas.drawBitmap(originBitmap, src, dest, null)
        originBitmap.recycle()
        return cutBitmap
    }


    /**
     * 安全的创建bitmap。
     * 如果新建 Bitmap 时产生了 OOM，可以主动进行一次 GC - System.gc()，然后再次尝试创建。
     *
     * @param width      Bitmap 宽度。
     * @param height     Bitmap 高度。
     * @param config     传入一个 Bitmap.Config。
     * @param retryCount 创建 Bitmap 时产生 OOM 后，主动重试的次数。
     * @return 返回创建的 Bitmap。
     */
    fun createBitmapSafely(width: Int, height: Int, config: Bitmap.Config, retryCount: Int): Bitmap? {
        try {
            return Bitmap.createBitmap(width, height, config)
        } catch (e: OutOfMemoryError) {
            e.printStackTrace()
            if (retryCount > 0) {
                System.gc()
                return createBitmapSafely(width, height, config, retryCount - 1)
            }
            return null
        }

    }

    /**
     * 质量压缩
     */
    fun compressImage(image: Bitmap): Bitmap {
        val baos = ByteArrayOutputStream()
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        var options = 100
        while (baos.toByteArray().size / 1024 > CompressConst.IMAGE_MAXSIZE_SYS && options > 8) { // 循环判断如果压缩后图片是否大于200kb,大于继续压缩
            baos.reset()// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos)// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 8// 每次都减少10
        }
        val isBm = ByteArrayInputStream(baos.toByteArray())// 把压缩后的数据baos存放到ByteArrayInputStream中
        return BitmapFactory.decodeStream(isBm, null, null)
    }

    /**
     * 获取Bitmap
     *
     * @param srcPath 图片路径
     * @return
     */
    fun getBitmapByPath(srcPath: String): Bitmap {
        return BitmapFactory.decodeFile(srcPath)
    }

    /**
     * 按尺寸等比例压缩（根据路径获取图片并压缩）
     */
    fun compressByPx(srcPath: String): Bitmap {
        val newOpts = BitmapFactory.Options()
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true
        var bitmap = BitmapFactory.decodeFile(srcPath, newOpts)// 此时返回bm为空
        newOpts.inJustDecodeBounds = false
        val w = newOpts.outWidth
        val h = newOpts.outHeight
        newOpts.inSampleSize = getRatioSize(w, h)// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts)
        return compressImage(bitmap)// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 按大小等比例压缩（根据Bitmap图片压缩）
     */
    fun compressBySize(image: Bitmap): Bitmap {
        val baos = ByteArrayOutputStream()
        var option = 100
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val length = baos.toByteArray().size / 1024//原图大小
        while (length > CompressConst.IMAGE_MAXSIZE_SYS && option > 8) {// 判断如果图片大于IMG_MAX_SIZE,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
            baos.reset()// 重置baos即清空baos
            option -= 8
            image.compress(Bitmap.CompressFormat.JPEG, option, baos)// 压缩后的数据存放到baos中
        }
        var isBm = ByteArrayInputStream(baos.toByteArray())
        val newOpts = BitmapFactory.Options()
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true
        var bitmap = BitmapFactory.decodeStream(isBm, null, newOpts)
        newOpts.inJustDecodeBounds = false
        val w = newOpts.outWidth
        val h = newOpts.outHeight
        newOpts.inSampleSize = getRatioSize(w, h)// 设置缩放比例
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = ByteArrayInputStream(baos.toByteArray())
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts)
        return compressImage(bitmap)// 压缩好比例大小后再进行质量压缩
    }

    /**
     * drawable 转 Bitmap
     */
    fun drawableToBitmap(drawable: Drawable): Bitmap {
        // 取 drawable 的长宽
        val w = drawable.intrinsicWidth
        val h = drawable.intrinsicHeight
        // 取 drawable 的颜色格式
        val config = if (drawable.opacity != PixelFormat.OPAQUE) Bitmap.Config.ARGB_8888 else Bitmap.Config.RGB_565
        // 建立对应 bitmap
        val bitmap = Bitmap.createBitmap(w, h, config)
        // 建立对应 bitmap 的画布
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, w, h)
        // 把 drawable 内容画到画布中
        drawable.draw(canvas)
        return bitmap
    }

    fun uriToBitmap(context: Context, bitmapUri: Uri): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(bitmapUri))
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }

        return bitmap
    }

    /**
     * 计算缩放比
     *
     * @param bitWidth  当前图片宽度
     * @param bitHeight 当前图片高度
     * @return
     * @Description: 函数描述
     */
    fun getRatioSize(bitWidth: Int, bitHeight: Int): Int {
        // 缩放比
        var ratio = 1
        // 缩放比,由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        if (bitWidth > bitHeight && bitWidth > CompressConst.IMAGE_WIDTH_MAX) {
            // 如果图片宽度比高度大,以宽度为基准
            ratio = bitWidth / CompressConst.IMAGE_WIDTH_MAX
        } else if (bitWidth < bitHeight && bitHeight > CompressConst.IMAGE_HEIGHT_MAX) {
            // 如果图片高度比宽度大，以高度为基准
            ratio = bitHeight / CompressConst.IMAGE_HEIGHT_MAX
        }
        // 最小比率为1
        if (ratio <= 0) {
            ratio = 1
        }
        return ratio
    }

    //保存文件（需要判断权限）
    fun saveFile(bitmap: Bitmap,path: String,sucCallback:(()->Unit)?=null) {
        val file = File(path)
        if (file.parentFile != null && file.parentFile.exists()) {
            file.parentFile.mkdirs()
        }
        BufferedOutputStream(FileOutputStream(file)).use { bos ->
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos)
            //TODO 保存图片后记得发送广播通知更新数据库
            sucCallback?.invoke()
        }
    }
    /**
     * uri 获取图片路径
     *
     * @param context
     * @param uri
     * @return
     */
    fun getPhotoPath(context: Context, uri: Uri?): String {
        var filePath = ""
        if (uri != null) {
            Log.d(TAG, uri.toString())
            val scheme = uri.scheme
            if (TextUtils.equals("content", scheme)) {// android 4.4以上版本处理方式
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
                    val wholeID = DocumentsContract.getDocumentId(uri)
                    val id = wholeID.split(":".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
                    val column = arrayOf(MediaStore.Images.Media.DATA)
                    val sel = MediaStore.Images.Media._ID + "=?"
                    val cursor = context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, arrayOf(id), null)
                    if (cursor != null && cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndex(column[0])
                        filePath = cursor.getString(columnIndex)
                        cursor.close()
                    }
                } else {// android 4.4以下版本处理方式
                    val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
                    val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
                    if (cursor != null && cursor.moveToFirst()) {
                        val columnIndex = cursor.getColumnIndex(filePathColumn[0])
                        filePath = cursor.getString(columnIndex)
                        Log.d(TAG, "filePath" + filePath)
                        cursor.close()
                    }
                }
            } else if (TextUtils.equals("file", scheme)) {// 小米云相册处理方式
                filePath = uri.path
            }

        }
        return filePath
    }

    fun imgGetWidth(path: String): Int {
        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options);
            return options.outWidth
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }

    fun imgGetHeight(path: String): Int {
        try {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            BitmapFactory.decodeFile(path, options);
            return options.outHeight
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return -1
    }

    //图片自适应
    fun imgWrapExt(width: Int, height: Int, minWidth: Int, minHeight: Int, maxWidth: Int, maxHeight: Int, finalWidthHeightBack: (finalWidth: Int, finalHeight: Int) -> Unit) {
        val ratioWH = width.toFloat() / height.toFloat()
        val ratioWHMax = maxWidth.toFloat() / minHeight.toFloat()
        val ratioWHMin = minWidth.toFloat() / maxHeight.toFloat()
        val ratioWrap = maxWidth.toFloat() / maxHeight.toFloat()
        var finalWidth = width
        var finalHeight = height
        if (ratioWH > ratioWHMax) {
            finalWidth = maxWidth
            finalHeight = minHeight
        } else if (ratioWH < ratioWHMin) {

            finalWidth = minWidth
            finalHeight = maxHeight
        } else {
            if (ratioWH < ratioWrap) {
                if (height < minHeight) {
                    finalHeight = minHeight
                } else if (height > maxHeight) {
                    finalHeight = maxHeight
                } else {
                    finalHeight = height
                }
                finalWidth = (finalHeight * ratioWH).toInt()
            } else {
                if (width < minWidth) {
                    finalWidth = minWidth
                } else if (width > maxWidth) {
                    finalWidth = maxWidth
                } else {
                    finalWidth = width
                }
                finalHeight = (finalWidth / ratioWH).toInt()
            }
        }
        finalWidthHeightBack(finalWidth, finalHeight)
    }



    // Bitmap、Drawable、InputStream、byte[] 之间转换

    /** */
    // 1. Bitmap to InputStream
    fun bitmap2Input(bitmap: Bitmap, quality: Int=100): InputStream {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos)
        return ByteArrayInputStream(baos.toByteArray())
    }

    // 2. Bitmap to byte[]
    fun bitmap2ByteArray(bitmap: Bitmap, quality: Int=100): ByteArray {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, baos)
        return baos.toByteArray()
    }

    // 3. Drawable to byte[]
    fun drawable2ByteArray(drawable: Drawable, quality: Int=100): ByteArray {
        val bitmap = (drawable as BitmapDrawable).bitmap
        val out = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, quality, out)
        return out.toByteArray()
    }

    // 4. byte[] to Bitmap
    fun byteArray2Bitmap(bytes: ByteArray): Bitmap {
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }
}
