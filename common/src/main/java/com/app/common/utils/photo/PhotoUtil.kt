package com.app.common.utils.photo

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.text.TextUtils
import android.widget.Toast
import com.app.common.by.Weak
import com.app.common.extensions.getApplicationIdExt
import com.app.common.file.UpdateFile
import com.app.common.logger.Logger
import com.app.common.utils.StorageUtils
import com.app.common.utils.UriUtil
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/**
 * Created by wr
 * Date: 2017/9/22  10:14
 * mail: 1902065822@qq.com
 * describe:
 * 裁剪默认大小为256[PhotoConfig.CUT_WIDTH_HEIGHT_DEFAULT]
 */

class PhotoUtil {
    companion object {
        var instance: PhotoUtil = PhotoUtil()
        val PHOTO_REQUEST_CAMERA = 5501
        val PHOTO_REQUEST_GALLERY = 5502
        val PHOTO_REQUEST_CUT = 5503
        val CROP_SISE_DEFAULT = 1f
        private val TAG = PhotoUtil::class.java.simpleName
    }

    private var simplePhoto: PhotoConfig? = null
    private var context: Context? by Weak()
    var cutPath = getPhotoCutPath()
    var cameraPath = getPhotoPath()

    fun setConfig(simplePhoto: PhotoConfig) {
        this.simplePhoto = simplePhoto
        context = simplePhoto.activity ?: simplePhoto.fragment?.context
        when (simplePhoto.chooseType) {
            PhotoConfig.CHOOSE_CAMERA -> camera()
            PhotoConfig.CHOOSE_PHOTO -> gallery()
        }
    }

    /*
     * 从相册获取
	 */
    fun gallery() {
        // 激活系统图库，选择一张图片
        val intent = Intent()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//ACTION_OPEN_DOCUMENT
            intent.action = Intent.ACTION_PICK
        } else {
            intent.action = Intent.ACTION_GET_CONTENT
        }
        intent.type = "image/*"
        startIntent(intent, PHOTO_REQUEST_GALLERY)
    }

    /**
     * 拍照获取
     */
    private fun camera() {
        if (simplePhoto == null) return
        cameraPath = simplePhoto?.cameraPath ?: getPhotoPath()
        val cameraFile = File(cameraPath)
        if (cameraFile.exists()) cameraFile.delete()
        context?.let {
            val cameraUri = UriUtil.getUri(it, cameraFile)
            // 激活相机
            val intent = Intent("android.media.action.IMAGE_CAPTURE")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) //添加这一句表示对目标应用临时授权该Uri所代表的文件
            }
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri)
            startIntent(intent, PHOTO_REQUEST_CAMERA)
        }
    }


    //裁切图片
    private fun crop(uri: Uri?) {
        if (simplePhoto == null) return
        if (context == null) return
        if (uri == null) return
        cutPath = simplePhoto?.cutPath ?: getPhotoCutPath()
        val uriOutput = UriUtil.getUri(context!!, File(cutPath))
        val pair = widthHeightCut(uri)
        val cutHeight = pair.first
        val cutWidth = pair.second

        // 裁剪图片意图
        val intent = Intent("com.android.camera.action.CROP")
        intent.setDataAndType(uri, "image/*")
        intent.putExtra("crop", "true")
        //aspectX，aspectY，outputX，outputY必须为int型，否则无效
        // 裁剪框的比例
        intent.putExtra("aspectX", cutWidth)
        intent.putExtra("aspectY", cutHeight)
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", cutWidth)
        intent.putExtra("outputY", cutHeight)

        //授予Uri的访问权限
        val resInfoList = context!!.packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
        if (resInfoList.size == 0) {
            Toast.makeText(context!!, "没有适合的应用程序", Toast.LENGTH_SHORT).show()
            return
        }
        val resInfoIterator = resInfoList.iterator()
        while (resInfoIterator.hasNext()) {
            val resolveInfo = resInfoIterator.next() as ResolveInfo
            val packageName = resolveInfo.activityInfo.packageName
            context!!.grantUriPermission(packageName, uriOutput, Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        }

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uriOutput)
        intent.putExtra("scaleUpIfNeeded", true)//去除黑边
        intent.putExtra("return-data", false)// 若为false则表示不返回数据，防止超过1M崩溃
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString())
        intent.putExtra("noFaceDetection", true)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)//7.0 来对目标应用临时授权该Uri所代表的文件，不加会出现“无法加载此图片”错误提示
        startIntent(intent, PHOTO_REQUEST_CUT)
    }

    private fun widthHeightCut(uri: Uri?): Pair<Int, Int> {
        if (simplePhoto == null) return Pair(0, 0)
        var ratioHWCut = CROP_SISE_DEFAULT
        var cutHeight = simplePhoto!!.cutHeight
        var cutWidth = simplePhoto!!.cutWidth
        if (cutWidth > 0) {
            ratioHWCut = (cutHeight / cutWidth).toFloat()//高宽比例（高/宽）
        }
        if (context != null && uri != null) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            val bitmap = BitmapFactory.decodeFile(UriUtil.getPath(context!!, uri), options) // 此时返回的bitmap为null
            val width = options.outWidth
            val height = options.outHeight
            if (width > 0) {
                val ratioHW = (height / width).toFloat()
                if (width < cutWidth || height < cutHeight) {//实际宽高小于裁剪宽高
                    if (ratioHW >= ratioHWCut) {//高度所占比率大写，以宽度为标准
                        cutWidth = width
                        cutHeight = (width * ratioHWCut).toInt()
                    } else {
                        cutHeight = height
                        cutWidth = (cutHeight / ratioHWCut).toInt()
                    }
                }
            }
        }
        return Pair(cutHeight, cutWidth)
    }

    private fun startIntent(intent: Intent, requestCode: Int) {
        if (simplePhoto?.fragment?.isAdded ?: false) {
            simplePhoto?.fragment?.startActivityForResult(intent, requestCode)
        } else if (simplePhoto?.activity?.isFinishing?.not() ?: false) {
            simplePhoto?.activity?.startActivityForResult(intent, requestCode)
        }
    }

    fun onPhotoResult(requestCode: Int, resultCode: Int, data: Intent?) {
        //是否是拍照、裁剪或图库返回
        val isPhoto = (requestCode == PHOTO_REQUEST_GALLERY || requestCode == PHOTO_REQUEST_CAMERA || requestCode == PHOTO_REQUEST_CUT) && resultCode == Activity.RESULT_OK
        if (!isPhoto) return
        if (simplePhoto == null) {
            Logger.d("onPhotoResult#simplePhoto == null")
            return
        }
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PHOTO_REQUEST_GALLERY) {//相册
                if (data != null) {// 从相册返回的数据
                    val uri = data.data// 得到图片的全路径
                    if (simplePhoto?.isCuted == true) {//裁剪
                        crop(uri)
                    } else {
                        context?.let { ctx ->
                            UriUtil.getPath(ctx, uri)
                        }?.let { galleryPath ->
                            simplePhoto?.onPathCallback?.invoke(galleryPath)
                        }
                    }
                } else {
                    context?.let {
                        Toast.makeText(it, "获取相册照片失败", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            if (requestCode == PHOTO_REQUEST_CAMERA) {//拍照
                if (!TextUtils.isEmpty(cameraPath)) {
                    simplePhoto?.let {
                        if (simplePhoto?.isCuted == true && context != null) {//裁剪
                            crop(UriUtil.getUri(context!!, File(cameraPath)))
                        } else {//不裁剪
                            it.onPathCallback?.let { it(cameraPath) }
                            //通知系统图库更新
                            if (context != null) {
                                UpdateFile.updateImageSysStatu(context, cameraPath, context!!.getApplicationIdExt())
                            }
                        }
                    }

                }
            }

            if (requestCode == PHOTO_REQUEST_CUT) {
                simplePhoto?.let {
                    if (simplePhoto!!.chooseType == PhotoConfig.CHOOSE_CAMERA && simplePhoto!!.isDeleteOld) {
                        //                deleteFile(simplePhoto.getCameraPath());//删除拍照图片
                    }
                    if (!TextUtils.isEmpty(cutPath)) {
                        it.onPathCallback?.let { it(cutPath) }
                        //通知系统图库更新
                        if (context != null) {
                            UpdateFile.updateImageSysStatu(context, cameraPath, context!!.getApplicationIdExt())
                        }
                    }
                }
            }
        }

    }


    private fun getPhotoPath() = StorageUtils.getPublicStorageDir("photo", null) + "/" + "img_" + getTimeTemp() + ".jpg"

    private fun getPhotoCutPath() = StorageUtils.getPublicStorageDir("photoCut", null) + "/" + "imgCut_" + getTimeTemp() + ".jpg"

    private fun getTimeTemp() = SimpleDateFormat("yyyyMMddHHmmss", Locale.US).format(Date())

}




