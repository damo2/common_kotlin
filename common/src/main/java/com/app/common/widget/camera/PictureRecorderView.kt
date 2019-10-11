package com.app.common.widget.camera

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.ImageFormat
import android.hardware.Camera
import android.media.MediaRecorder
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.app.common.R
import com.app.common.utils.StorageUtils
import kotlinx.android.synthetic.main.movie_recorder_view.view.*
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


/**
 * Created by wr
 * Date: 2019/10/10  10:00
 * mail: 1902065822@qq.com
 * describe:
 * 拍照和拍摄视频
 *
 * 设置parameters 时，先要lock()，设置完成后unlock()
 */
class PictureRecorderView(context: Context?, attrs: AttributeSet?) : LinearLayout(context, attrs) {
    private var mCamera: Camera? = null
    private var mMediaRecorder: MediaRecorder? = null

    private var mTimeCount: Int = 0// 时间计数
    private var mTimer: Timer? = null// 计时器
    private var mFile: File? = null// 文件

    var videoWidth: Int = 640// 视频分辨率宽度
    var videoHeight: Int = 480// 视频分辨率高度
    var videoMaxTime: Int = 60// 一次拍摄最长时间

    private var mVideoCallback: ((isSuc: Boolean, progress: Int, path: String) -> Unit)? = null
    private var mPictureCallback: ((path: String) -> Unit)? = null

    companion object {
        const val PREVIEW_FRAME_RATE_DEFAULT = 15//预览帧速率
    }

    init {
        LayoutInflater.from(context)
                .inflate(R.layout.movie_recorder_view, this)
        initView()
        initListener()
    }

    private fun initView() {
        mCamera = Camera.open()
        cameraPreview.camera = mCamera
    }

    private fun initListener() {
    }


    private fun createDir(fileName: String) {
        try {
            mFile = File(StorageUtils.getPublicStoragePath(fileName, null))
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 初始化拍摄视频
     * @throws IOException
     */
    @SuppressLint("NewApi")
    @Throws(IOException::class)
    private fun initRecord() {
        mCamera?.let {
            mMediaRecorder = MediaRecorder()
            mMediaRecorder?.reset()
            mMediaRecorder?.setCamera(it)
            mMediaRecorder?.setOnErrorListener { mr, what, extra ->
                try {
                    mr?.reset()
                } catch (e: IllegalStateException) {
                    e.printStackTrace()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            mMediaRecorder?.setPreviewDisplay(cameraPreview.surfaceHolder.surface)
            mMediaRecorder?.setVideoSource(MediaRecorder.VideoSource.CAMERA)// 视频源
            mMediaRecorder?.setAudioSource(MediaRecorder.AudioSource.MIC)// 音频源
            mMediaRecorder?.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)// 视频输出格式
            mMediaRecorder?.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)// 音频格式
            val size = CameraUtils.getBestPreviewSize(videoWidth, videoHeight, it.parameters)
            mMediaRecorder?.setVideoSize(videoWidth, videoHeight)// 设置分辨率
            mMediaRecorder?.setVideoFrameRate(CameraUtils.getBestPreviewFrameRate(PREVIEW_FRAME_RATE_DEFAULT, it.parameters))
            mMediaRecorder?.setVideoEncodingBitRate(1 * 1280 * 720)// 设置帧频率，清晰度
            mMediaRecorder?.setOrientationHint(90)// 输出旋转90度，保持竖屏录制
            mMediaRecorder?.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP)// 视频录制格式
            // mediaRecorder.setMaxDuration(Constant.MAXVEDIOTIME * 1000);
            mMediaRecorder?.setOutputFile(mFile?.absolutePath)
            mMediaRecorder?.prepare()

            mCamera?.unlock()
            try {
                mMediaRecorder?.start()
            } catch (e: IllegalStateException) {
                e.printStackTrace()
            } catch (e: RuntimeException) {
                e.printStackTrace()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //停止录制
    private fun stopRecord() {
        progressBar.progress = 0
        mTimer?.cancel()
        //下面三个参数必须加，不加的话会奔溃，在mediarecorder.stop();
        //报错为：RuntimeException:stop failed
        mMediaRecorder?.setOnErrorListener(null)
        mMediaRecorder?.setPreviewDisplay(null)
        mMediaRecorder?.setOnInfoListener(null)

        try {
            mMediaRecorder?.stop()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: RuntimeException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //释放资源
    private fun releaseRecord() {
        mMediaRecorder?.setOnErrorListener(null)
        try {
            mMediaRecorder?.release()
        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        mMediaRecorder = null
    }

    /**
     * 开始录制视频
     * @param fileName
     * 视频储存位置
     * @param onRecordFinishListener
     * 达到指定时间之后回调接口
     */
    fun record(fileName: String = "damo/video/${SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())}.mp4", videoCallback: (isSuc: Boolean, progress: Int, path: String) -> Unit) {
        this.mVideoCallback = videoCallback
        createDir(fileName)
        try {
            initRecord()
            mTimeCount = 0// 时间计数器重新赋值
            mTimer = Timer()
            mTimer?.schedule(object : TimerTask() {
                override fun run() {
                    mTimeCount++
                    mVideoCallback?.invoke(false, mTimeCount, mFile?.path ?: "")
                    progressBar.progress = mTimeCount// 设置进度条
                    if (mTimeCount == videoMaxTime) {// 达到指定时间，停止拍摄
                        stop()
                    }
                }
            }, 0, 1000)
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    /**
     * 停止拍摄
     */
    fun stop() {
        stopRecord()
        releaseRecord()
        cameraPreview.freeCameraResource()
        mVideoCallback?.invoke(true, 100, mFile?.path ?: "")
    }

    /**
     * 拍照
     */
    fun takePicture(fileName: String = "damo/picture/${SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.US).format(Date())}.jpg") {
        createDir(fileName)
        try {
            initPicture()
            mCamera?.unlock()
            mCamera?.takePicture(null, null) { data, camera ->
                mFile?.writeBytes(data)
                mPictureCallback?.invoke(mFile?.path ?: "")
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    //初始化拍照
    @Throws(IOException::class)
    private fun initPicture() {
        mCamera?.parameters = mCamera?.parameters?.apply {
            this.pictureFormat = ImageFormat.JPEG//图片的格式
            CameraUtils.getBestPreviewSize(resources.displayMetrics.widthPixels, resources.displayMetrics.heightPixels, this)?.let {
                this.setPreviewSize(it.width, it.height)//设置预览分辨率
                this.setPictureSize(it.width, it.height)//设置保存图片的大小
            }
            this.setRotation(90)//设置保存图片竖直
            this.focusMode = Camera.Parameters.FOCUS_MODE_AUTO//设置对焦模式，自动对焦
        }
    }

}