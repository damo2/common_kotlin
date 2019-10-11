package com.app.common.widget.camera

import android.content.Context
import android.graphics.Rect
import android.hardware.Camera
import android.os.Build
import android.util.AttributeSet
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View.OnTouchListener
import com.app.common.logger.logd
import kotlinx.android.synthetic.main.movie_recorder_view.view.*
import java.io.IOException
import java.util.*

/**
 * Created by wr
 * Date: 2019/10/11  9:51
 * mail: 1902065822@qq.com
 * describe:
 * 相机预览
 */
class CameraPreview(context: Context, attrs: AttributeSet?) : SurfaceView(context, attrs) {
    lateinit var surfaceHolder: SurfaceHolder
    private var mIsPreviewActive = false

    var mIsOpenCamera: Boolean = true// 是否一开始就打开摄像头
    var camera: Camera? = null
        set(value) {
            field = value
            initSurfaceHolder()
            initListener()
        }

    private fun initListener() {
        cameraPreview.setOnTouchListener(OnTouchListener { v, event ->
            focusOnTouch(event.x.toInt(), event.y.toInt())
            false
        })
    }


    private fun initSurfaceHolder() {
        surfaceHolder = holder
        surfaceHolder.addCallback(surfaceCallback())
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
    }

    private fun surfaceCallback(): SurfaceHolder.Callback {
        return object : SurfaceHolder.Callback {
            override fun surfaceCreated(holder: SurfaceHolder?) {
                if (!mIsOpenCamera)
                    return
                try {
                    initCamera()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }

            override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
                // 将holder，这个holder为开始在oncreat里面取得的holder，将它赋给surfaceHolder
                holder?.let {
                    surfaceHolder = holder
                }
                doAutoFocus()//实现自动对焦
            }

            override fun surfaceDestroyed(holder: SurfaceHolder?) {
                freeCameraResource()
            }
        }
    }

    // 自动对焦
    private fun doAutoFocus() {
        if (camera != null) {
            var cameraParameters = camera!!.parameters
            cameraParameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
            camera?.parameters = cameraParameters
            camera?.autoFocus { success, camera ->
                if (mIsPreviewActive && success) {
                    camera.cancelAutoFocus()// 只有加上了这一句，才会自动对焦。
                    if (Build.MODEL != "KORIDY H30") {
                        cameraParameters = camera.parameters
                        cameraParameters.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE// 1连续对焦
                        camera.parameters = cameraParameters
                    } else {
                        cameraParameters = camera.parameters
                        cameraParameters.focusMode = Camera.Parameters.FOCUS_MODE_AUTO
                        camera.parameters = cameraParameters
                    }
                }
            }
        }
    }

    //触发点击聚焦
    private fun focusOnTouch(x: Int, y: Int) {
        val rect = Rect(x - 100, y - 100, x + 100, y + 100)
        var left = rect.left * 2000 / cameraPreview.width - 1000
        var top = rect.top * 2000 / cameraPreview.height - 1000
        var right = rect.right * 2000 / cameraPreview.width - 1000
        var bottom = rect.bottom * 2000 / cameraPreview.height - 1000
        // 如果超出了(-1000,1000)到(1000, 1000)的范围，则会导致相机崩溃
        left = if (left < -1000) -1000 else left
        top = if (top < -1000) -1000 else top
        right = if (right > 1000) 1000 else right
        bottom = if (bottom > 1000) 1000 else bottom
        submitFocusAreaRect(Rect(left, top, right, bottom))
    }

    private fun submitFocusAreaRect(rect: Rect) {
        if (camera != null) {
            try {
                camera?.cancelAutoFocus() // 先要取消掉进程中所有的聚焦功能
                camera?.parameters = camera?.parameters?.apply {
                    this.focusMode = Camera.Parameters.FOCUS_MODE_AUTO // 设置聚焦模式
                    if (this.maxNumFocusAreas > 0) {
                        val focusAreas = ArrayList<Camera.Area>()
                        focusAreas.add(Camera.Area(rect, 1000))
                        this.focusAreas = focusAreas
                    } else {
                        logd("parameters.getMaxNumFocusAreas() : " + this.maxNumFocusAreas)
                    }
                } // 一定要记得把相应参数设置给相机
                camera?.autoFocus { success, camera -> logd("onAutoFocus : $success") }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 初始化摄像头
     */
    @Throws(IOException::class)
    fun initCamera() {
//        freeCameraResource()
        if (camera == null)
            return

        camera?.parameters = camera?.parameters?.apply {
            this.focusMode = Camera.Parameters.FOCUS_MODE_AUTO//自动对焦
            this.previewFrameRate = 20//fps
        }
        camera?.setDisplayOrientation(90)//设置相机预览方向
        camera?.setPreviewDisplay(surfaceHolder)
        camera?.startPreview()
//        camera?.unlock()
        mIsPreviewActive = true
    }

    /**
     * 释放摄像头资源
     */
    fun freeCameraResource() {
        if (camera != null) {
            camera?.setPreviewCallback(null)
            camera?.stopPreview()
            camera?.lock()
            camera?.release()
            camera = null
            mIsPreviewActive = false
        }
    }
}